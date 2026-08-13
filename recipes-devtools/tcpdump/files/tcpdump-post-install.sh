#!/bin/sh
# Post-install hook for RDM tcpdump package.
# Stages tcpdump binary into /tmp/tools for runtime use.

set -e

if [ -f /etc/device.properties ]; then
    . /etc/device.properties
fi

if [ -z "$DEVICE_TYPE" ]; then
    DEVICE_TYPE="unknown"
fi

case "$DEVICE_TYPE" in
    mediaclient)
        RDM_LOG_FILE="/opt/logs/rdm_status.log"
        ;;
    broadband)
        RDM_LOG_FILE="/rdklogs/logs/rdm_status.log.0"
        ;;
    *)
        RDM_LOG_FILE="/var/log/rdm_status.log"
        ;;
esac

log_info() {
    echo "[tcpdump-post-install] [INFO] $*" >> "$RDM_LOG_FILE"
}

log_error() {
    echo "[tcpdump-post-install] [ERROR] $*" >> "$RDM_LOG_FILE"
}

SCRIPT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
APP_HOME_DIR="${SCRIPT_DIR%/etc/rdm/post-services}"
SRC_BIN="${APP_HOME_DIR}/usr/bin/tcpdump"
DST_BIN="/run/tcpdump/usr/bin/tcpdump"

log_info "Starting tcpdump post-service script"
log_info "Source binary: $SRC_BIN"
log_info "Destination binary: $DST_BIN"

if [ ! -f "$SRC_BIN" ]; then
    log_error "Source binary not found: $SRC_BIN"
    exit 1
fi

if ! mkdir -p "$(dirname "$DST_BIN")"; then
    log_error "Failed to create destination directory"
    exit 1
fi

if ! cp -f "$SRC_BIN" "$DST_BIN"; then
    log_error "Failed to copy binary to runtime path"
    exit 1
fi

if ! chmod 0755 "$DST_BIN"; then
    log_error "Failed to set executable permission on $DST_BIN"
    exit 1
fi

log_info "tcpdump staged successfully at $DST_BIN"

# Create /tmp/tools symlink pointing to the execution path
if mkdir -p /tmp/tools && ln -sf "$DST_BIN" /tmp/tools/tcpdump; then
    log_info "Symlink created: /tmp/tools/tcpdump -> $DST_BIN"
else
    log_error "Failed to create symlink at /tmp/tools/tcpdump"
fi

# Cleanup: remove downloaded archives and extracted package directories
# Determine package base name from APP_HOME_DIR (e.g., /tmp/CGM4331COM-tcpdump -> CGM4331COM-tcpdump)
PKG_BASE=$(basename "$APP_HOME_DIR")

log_info "Cleaning up package artifacts for $PKG_BASE"

# Remove the rdm downloads folder for this package (tar, sig, signed tar, etc.)
if [ -d "/tmp/rdm/downloads/$PKG_BASE" ]; then
    if rm -rf "/tmp/rdm/downloads/$PKG_BASE"; then
        log_info "Removed /tmp/rdm/downloads/$PKG_BASE"
    else
        log_error "Failed to remove /tmp/rdm/downloads/$PKG_BASE"
    fi
fi

# Remove any lingering package temp dirs under /tmp (extracted package)
if [ -d "/tmp/$PKG_BASE" ]; then
    if rm -rf "/tmp/$PKG_BASE"; then
        log_info "Removed /tmp/$PKG_BASE"
    else
        log_error "Failed to remove /tmp/$PKG_BASE"
    fi
fi

# Remove archive files matching package basename under /tmp/rdm/downloads (best-effort)
rm -f /tmp/rdm/downloads/${PKG_BASE}* 2>/dev/null || true
rm -f /tmp/${PKG_BASE}* 2>/dev/null || true

log_info "Post-install cleanup complete for $PKG_BASE"

exit 0
