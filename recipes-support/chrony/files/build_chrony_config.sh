#!/bin/sh
##########################################################################
# build_chrony_conf.sh — standalone ExecStartPre script for chronyd
#
# Generates /tmp/rdk_chrony.conf from syscfg NTP server settings.
# Run by chronyd.service as ExecStartPre before every chronyd start,
# including crash-triggered restarts via Restart=always.
#
# Exits 0 on success, 1 on fatal error (no usable NTP server).
##########################################################################

# Ensure log directory exists — /rdklogs is a tmpfs that starts empty on boot
mkdir -p /rdklogs/logs
NTPD_LOG=/rdklogs/logs/chrony.log

log_t() {
    echo "$(date +"%Y-%m-%dT%H:%M:%S") $*" >> "$NTPD_LOG"
}

# Source platform properties (MULTI_CORE, HOST_INTERFACE_IP, NTPD_IMMED_PEER_SYNC, PARTNER_ID)
# and WAN interface helper (getWanInterfaceName)
. /etc/device.properties
. /etc/waninfo.sh

# Load NTP server names, enable flag, partner ID, and per-server RFC settings from syscfg.
# utctx_cmd emits "SYSCFG_<key>=<value>" lines that are safe to eval.
FOO=$(utctx_cmd get ntp_server1 ntp_server2 ntp_server3 ntp_server4 ntp_server5 \
                   new_ntp_enabled PARTNER_ID chrony_makestep\
                   chrony_server1_settings chrony_server2_settings \
                   chrony_server3_settings chrony_server4_settings \
                   chrony_server5_settings)
eval "$FOO"

# Resolve PARTNER_ID: device.properties may set it directly; syscfg is the fallback
: "${PARTNER_ID:=$SYSCFG_PARTNER_ID}"

RDK_CHRONY_CONF=/etc/rdk_chrony.conf
WAN_IFACE=$(getWanInterfaceName)

> "$RDK_CHRONY_CONF"

# ──────────────────────────────────────────────────────────────────────────────
# _write_server_line idx url
#   Emit one pool/server directive for NTP server <url> using per-server
#   RFC settings from the pre-loaded SYSCFG_chrony_server<idx>_settings
#   variable.  Falls back to "pool,4,true,10,12" if the key is unset.
# ──────────────────────────────────────────────────────────────────────────────
_write_server_line() {
    local idx="$1"
    local url="$2"

    local settings_var="SYSCFG_chrony_server${idx}_settings"
    local settings
    eval "settings=\$$settings_var"

    local src_type maxsources iburst_flag minpoll maxpoll
    if [ -n "$settings" ]; then
        src_type=$(echo "$settings"   | cut -d',' -f1)
        maxsources=$(echo "$settings" | cut -d',' -f2)
        iburst_flag=$(echo "$settings"| cut -d',' -f3)
        minpoll=$(echo "$settings"    | cut -d',' -f4)
        maxpoll=$(echo "$settings"    | cut -d',' -f5)
    else
        src_type="pool"; maxsources="4"; iburst_flag="true"; minpoll="10"; maxpoll="12"
    fi

    local iburst_opt=""
    [ "$iburst_flag" = "true" ] && iburst_opt=" iburst"

    # maxsources is an option of chrony's "pool" directive only;
    # A plain "server" directive must not carry it
    if [ "$src_type" = "pool" ]; then
        local maxsources_opt=""
        case "$maxsources" in
            ''|*[!0-9]*) : ;;
            *) [ "$maxsources" -ge 1 ] && maxsources_opt=" maxsources ${maxsources}" ;;
        esac
        echo "pool ${url}${iburst_opt}${maxsources_opt} minpoll ${minpoll} maxpoll ${maxpoll}" >> "$RDK_CHRONY_CONF"
    else
        echo "server ${url}${iburst_opt} minpoll ${minpoll} maxpoll ${maxpoll}" >> "$RDK_CHRONY_CONF"
    fi
    #TBD - except SKY partners should we use ipv6 if map status enabled?
}

# ── Write NTP server directives ────────────────────────────────────────────────
if [ "$SYSCFG_new_ntp_enabled" = "true" ]; then
    # Multi-server pool mode — honour per-server RFC tuning
    i=1
    for srv in "$SYSCFG_ntp_server1" "$SYSCFG_ntp_server2" "$SYSCFG_ntp_server3" \
               "$SYSCFG_ntp_server4" "$SYSCFG_ntp_server5"; do
        if [ -n "$srv" ] && [ "$srv" != "no_ntp_address" ]; then
            _write_server_line "$i" "$srv"
        fi
        i=$((i + 1))
    done
else
    # Legacy single-server mode
    SRV="$SYSCFG_ntp_server1"
    if [ -z "$SRV" ] || [ "$SRV" = "no_ntp_address" ]; then
        if [ -f "/nvram/ETHWAN_ENABLE" ] || [ -z "$PARTNER_ID" ]; then
            SRV="time1.google.com"
            log_t "chrony-conf-update : NTP server not configured, using default"
        else
            log_t "chrony-conf-update : NTP server not configured and PartnerID set — aborting"
            exit 1
        fi
    fi
    _write_server_line "1" "$SRV"
fi

# Allow NTP requests from XLE backhaul subnet
echo "allow 192.168.245.0/24" >> "$RDK_CHRONY_CONF"

# ── Bind acquisition sockets to the WAN interface ─────────────────────────────
# Record the bound interface to /tmp/chrony_last_wan_ifname so service_chronyd.sh
# can detect a WAN interface change (failover) and rebind. This is the single
# writer of the marker, written at the exact moment bindacqdevice is emitted, so
# the marker always reflects the interface the running chronyd is bound to.
# When no interface is bound, clear the marker so no false change is detected.
CHRONY_WAN_IFACE_MARKER=/tmp/chrony_last_wan_ifname

if [ -n "$WAN_IFACE" ]; then
    echo "bindacqdevice $WAN_IFACE" >> "$RDK_CHRONY_CONF"
    log_t "SERVICE_CHRONYD : binding acquisition sockets to interface $WAN_IFACE"
    echo "$WAN_IFACE" > "$CHRONY_WAN_IFACE_MARKER"
else
    rm -f "$CHRONY_WAN_IFACE_MARKER"
fi


# ── Append makestep RFC override from syscfg chrony_makestep if present ────────
# syscfg persists across reboots; falls back to the static default in
# /etc/chrony.conf (makestep 1.0 3) when the syscfg key is absent.
if [ -n "$SYSCFG_chrony_makestep" ]; then
    echo "$SYSCFG_chrony_makestep" >> "$RDK_CHRONY_CONF"
    log_t "chrony-conf-update  : applied makestep override from syscfg chrony_makestep"
else
    echo "makestep 1.0 3" >> "$RDK_CHRONY_CONF"
    log_t "chrony-conf-update : applied default makestep 1.0 3"

fi

log_t "SERVICE_CHRONYD : rdk_chrony.conf built at $RDK_CHRONY_CONF"
exit 0

