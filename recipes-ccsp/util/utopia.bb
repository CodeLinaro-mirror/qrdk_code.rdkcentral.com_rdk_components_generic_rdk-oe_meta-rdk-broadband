SUMMARY = "CCSP Utopia"
HOMEPAGE = "http://github.com/belvedere-yocto/Utopia"

LICENSE = "Apache-2.0 & BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=baa21dec03307f641a150889224a157f"

export PARTNER_DEFAULT_EXT="${@bb.utils.contains('DISTRO_FEATURES', 'partner_default_ext','yes', 'no', d)}"

DEPENDS = "ccsp-common-library hal-cm hal-dhcpv4c hal-ethsw hal-moca hal-mso_mgmt hal-mta hal-platform hal-vlan hal-wifi zlib dbus libnetfilter-queue libupnp cjson rdk-wifi-halif libevent libsyswrapper"
DEPENDS:append:libc-musl = " libtirpc"
DEPENDS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'rdkb_wan_manager', ' nanomsg ', ' ', d)}"

DEPENDS:append = " libtirpc"
DEPENDS:remove_morty = " libtirpc"

RDEPENDS:${PN}:append = " bash"
RDEPENDS:${PN}:remove_morty = " bash"

require recipes-ccsp/ccsp/ccsp_common.inc

SRC_URI = "${CMF_GITHUB_ROOT}/utopia;protocol=https;${BRANCH_utopia};name=Utopia"

S = "${UNPACKDIR}/${PN}-${PV}"

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
SRC_URI:append = " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'partner_default_ext','file://ApplySystemDefaults.service','',d)} \
"
EXTRA_OECONF:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'pm_lan_mgr', '--enable-lanmgr_enabled', '', d)}"

EXTRA_OECONF:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'no_moca_support', '--enable-no_moca_support', '', d)}"

#This configuration is commented in utopia configure.ac file. As long as this is not enabled in configure file, passing the OE configuration is giving error in kirkstone.
#EXTRA_OECONF:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'WanFailOverSupportEnable', '--enable-wanfailover', '', d)}"

inherit autotools useradd update-alternatives pkgconfig

DEPENDS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' safec', " ", d)}"

CFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec',  ' `pkg-config --cflags libsafec`', '-fPIC', d)}"
LDFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' `pkg-config --libs libsafec`', '', d)}"
CFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', '', ' -DSAFEC_DUMMY_API', d)}"
DEPENDS:remove:class-native = " safec-native"
CFLAGS += " -Wall -Werror -Wextra -Wno-pointer-sign -Wno-sign-compare -Wno-deprecated-declarations -Wno-type-limits -Wno-unused-parameter -Wno-return-local-addr "
CFLAGS:append = " -Wno-format-overflow -Wno-misleading-indentation -Wno-enum-conversion "

CFLAGS:append = " \
    -I${STAGING_INCDIR}/ccsp \
    -DCONFIG_BUILD_TRIGGER \
    "

CFLAGS:append = " -I${STAGING_INCDIR}/tirpc "
CFLAGS:remove_morty = " -I${STAGING_INCDIR}/tirpc "

CFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'bci', '-DCISCO_CONFIG_TRUE_STATIC_IP -DCISCO_CONFIG_DHCPV6_PREFIX_DELEGATION', '', d)}"

CFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'meshwifi', '-DENABLE_FEATURE_MESHWIFI', '', d)}"

CFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'dslite', '-DDSLITE_FEATURE_SUPPORT', '', d)}"

CFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'wifimotion', '-DFEATURE_COGNITIVE_WIFIMOTION', '', d)}"

LDFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'rdkb_wan_manager', '-lnanomsg', '', d)}"

CFLAGS:append:libc-musl = " -I${STAGING_INCDIR}/tirpc"
LDFLAGS:append:libc-musl = " -ltirpc"

LDFLAGS:append = " -ltirpc -lrt"
LDFLAGS:remove_morty = " -ltirpc -lrt"

do_install:prepend  () {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'no_mta_support', 'true', 'false', d)}; then
       sed -i '/mta/Id' ${WORKDIR}/git/source/scripts/init/defaults/system_defaults_arm
    fi
}

do_install:append () {
#    install -D -m 0644 ${S}/source/include/autoconf.h ${D}${includedir}/utctx/autoconf.h
    install -D -m 0644 ${S}/source/ulog/ulog.h ${D}${includedir}/ulog/ulog.h
    install -D -m 0644 ${S}/source/utapi/lib/utapi.h ${D}${includedir}/utapi/utapi.h
    install -D -m 0644 ${S}/source/utapi/lib/utapi_wlan.h ${D}${includedir}/utapi/utapi_wlan.h
    install -D -m 0644 ${S}/source/utapi/lib/utapi_util.h ${D}${includedir}/utapi/utapi_util.h
    install -D -m 0644 ${S}/source/utctx/lib/utctx.h ${D}${includedir}/utctx/utctx.h
    install -D -m 0644 ${S}/source/utctx/lib/utctx_api.h ${D}${includedir}/utctx/utctx_api.h
    install -D -m 0644 ${S}/source/utctx/lib/utctx_rwlock.h ${D}${includedir}/utctx/utctx_rwlock.h
    install -D -m 0644 ${S}/source/syscfg/lib/syscfg.h ${D}${includedir}/syscfg/syscfg.h
    install -D -m 0644 ${S}/source/sysevent/lib/sysevent.h ${D}${includedir}/sysevent/sysevent.h
    install -D -m 0644 ${S}/source/sysevent/lib/libsysevent_internal.h ${D}${includedir}/sysevent/libsysevent_internal.h
    install -D -m 0644 ${S}/source/utapi/lib/utapi_tr_dhcp.h ${D}${includedir}/utapi/utapi_tr_dhcp.h
    install -m 0644 ${S}/source/utapi/lib/*.h ${D}${includedir}/utapi/
    install -d ${D}${sysconfdir}/cron

    install -d ${D}${sysconfdir}/cron/cron.everyminute
    install -d ${D}${sysconfdir}/cron/cron.every5minute
    install -d ${D}${sysconfdir}/cron/cron.every10minute
    install -d ${D}${sysconfdir}/cron/cron.hourly
    install -d ${D}${sysconfdir}/cron/cron.daily
    install -d ${D}${sysconfdir}/cron/cron.weekly
    install -d ${D}${sysconfdir}/cron/cron.monthly
    install -m 444 ${WORKDIR}/udhcpc.vendor_specific  ${D}${sysconfdir}/udhcpc.vendor_specific
    install -m 755 ${WORKDIR}/udhcpc.script ${D}${sysconfdir}/
    install -m 755 ${WORKDIR}/dhcpswitch.sh ${D}${sysconfdir}/

    install -m 0644 ${S}/source/scripts/init/service.d/cron.allow ${D}${sysconfdir}/cron/
    install -m 755 ${S}/source/scripts/init/service.d/ddns_daily.sh ${D}${sysconfdir}/cron/cron.daily/
    if ${@bb.utils.contains('DISTRO_FEATURES', 'no_mta_support', 'false', 'true', d)}; then
       install -m 755 ${S}/source/scripts/init/service.d/mta_overcurrent_status_daily.sh ${D}${sysconfdir}/cron/cron.daily/
    fi
    install -m 755 ${S}/source/scripts/init/service.d/remove_max_cpu_usage_file.sh ${D}${sysconfdir}/cron/cron.daily/
    install -m 755 ${S}/source/scripts/init/service.d/log_hourly.sh ${D}${sysconfdir}/cron/cron.hourly/
    install -m 755 ${S}/source/scripts/init/service.d/ntp_hourly.sh ${D}${sysconfdir}/cron/cron.hourly/
    install -m 755 ${S}/source/scripts/init/service.d/log_every10minute.sh ${D}${sysconfdir}/cron/cron.every10minute/
    install -m 755 ${S}/source/scripts/init/service.d/pmon_every5minute.sh ${D}${sysconfdir}/cron/cron.every5minute/
    install -m 755 ${S}/source/scripts/init/service.d/logrotate.sh ${D}${sysconfdir}/cron/cron.every5minute/
    install -m 755 ${S}/source/scripts/init/service.d/sysevent_tick.sh ${D}${sysconfdir}/cron/cron.everyminute/

    install -m 755 ${S}/source/scripts/init/service.d/waninfo.sh ${D}${sysconfdir}/

    install -d ${D}${includedir}/ccsp
    install -m 644 ${S}/source/util/print_uptime/print_uptime.h ${D}${includedir}/ccsp

    DISTRO_PARTNER_DEFAULT_EXT="${@bb.utils.contains('DISTRO_FEATURES','partner_default_ext','true','false',d)}"
    if [ $DISTRO_PARTNER_DEFAULT_EXT = 'true' ]; then
        install -D -m 755 ${WORKDIR}/ApplySystemDefaults.service ${D}${systemd_unitdir}/system/ApplySystemDefaults.service
    fi

    # Creating symbolic links to install files in specific directory as in legacy builds
    ln -sf /usr/bin/syscfg ${D}${bindir}/syscfg_create
    ln -sf /usr/bin/syscfg ${D}${bindir}/syscfg_destroy
    ln -sf /usr/bin/syscfg ${D}${bindir}/syscfg_format
    ln -sf /usr/bin/syscfg ${D}${bindir}/syscfg_check
}

USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM:${PN} = "--system firewall"
USERADD_PARAM:${PN} += "--system --home ${localstatedir}/run/firewall/ -M -g firewall --shell /bin/false firewall"

FILES:${PN} += "${sysconfdir}/utopia/"
FILES:${PN} += "${sysconfdir}/dhcp_static_hosts"
FILES:${PN} += "${sysconfdir}/IGD/"
FILES:${PN} += "${sysconfdir}/cron"
FILES:${PN} += "${sysconfdir}/utopia/service.d/"
FILES:${PN} += "${sysconfdir}/utopia/registrartion.d/"
FILES:${PN} += "${sysconfdir}/utopia/post.d/"
FILES:${PN} += "${sysconfdir}/utopia/service.d/service_bridge/"
FILES:${PN} += "${sysconfdir}/utopia/service.d/service_ddns/"
FILES:${PN} += "${sysconfdir}/utopia/service.d/service_dhcp_server/"
FILES:${PN} += "${sysconfdir}/utopia/service.d/service_lan/"
FILES:${PN} += "${sysconfdir}/utopia/service.d/service_multinet/"
FILES:${PN} += "${sysconfdir}/utopia/service.d/service_syslog/"
FILES:${PN} += "${sysconfdir}/utopia/service.d/service_wan/"
FILES:${PN} += "/fss/gw/etc/syslog.conf.${BPN}"

FILES:${PN}-dbg += " \
    ${prefix}/ccsp/.debug \
    ${prefix}/src/debug \
    ${bindir}/.debug \
    ${libdir}/.debug \
"

ALTERNATIVE_PRIORITY = "190"
ALTERNATIVE:${PN} = "syslog-conf"
ALTERNATIVE_LINK_NAME[syslog-conf] = "${sysconfdir}/syslog.conf"
ALTERNATIVE_TARGET[syslog-conf] = "/fss/gw/etc/syslog.conf.${BPN}"
CONFFILES:${PN} = "/fss/gw/${sysconfdir}/syslog.conf.${BPN}"

DEPENDS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'telemetry2_0', ' telemetry', '', d)}"

FILES:${PN}:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'partner_default_ext','${systemd_unitdir}/system/ApplySystemDefaults.service','',d)}"
SYSTEMD_SERVICE:${PN} += " ${@bb.utils.contains('DISTRO_FEATURES', 'partner_default_ext','ApplySystemDefaults.service','',d)}"

RDEPENDS:${PN}:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'core-net-lib', ' core-net-lib', " ", d)}"
RDEPENDS:${PN}:remove_morty = " ${@bb.utils.contains('DISTRO_FEATURES', 'core-net-lib', ' core-net-lib', " ", d)}"
DEPENDS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'core-net-lib', ' core-net-lib', " ", d)}"
CFLAGS:append  = " ${@bb.utils.contains('DISTRO_FEATURES', 'core-net-lib', ' -DCORE_NET_LIB', '', d)}"
EXTRA_OECONF:append = " --enable-core_net_lib_feature_support=${@bb.utils.contains('DISTRO_FEATURES', 'core-net-lib', 'yes', 'no', d)} "
EXTRA_OECONF:append  = " --with-ccsp-platform=bcm "
