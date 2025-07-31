inherit breakpad-logmapper
DEPENDS += " utopia libunpriv "
CFLAGS:append = " \
    -I${STAGING_INCDIR}/syscfg \
    "
LDFLAGS +=" -lprivilege -lsyscfg"
FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
SRC_URI:append += "file://Webcfg_drop_root.patch"
# Breakpad processname and logfile mapping
BREAKPAD_LOGMAPPER_PROCLIST = "webconfig"
BREAKPAD_LOGMAPPER_LOGLIST = "WEBCONFIGlog.txt.0"
