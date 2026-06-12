SUMMARY = "CCSP PsmSsp component"
HOMEPAGE = "http://github.com/belvedere-yocto/CcspPsm"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"


DEPENDS = "ccsp-common-library dbus rbus utopia libunpriv mountutils"
DEPENDS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'systemd', '', d)}"
DEPENDS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' safec', " ", d)}"

require ccsp_common.inc

SRC_URI = "${CMF_GITHUB_ROOT}/persistent-storage-manager;protocol=https;${BRANCH_ccsp_psm};name=CcspPsm"

S = "${UNPACKDIR}/${PN}-${PV}"

inherit autotools breakpad-logmapper

CFLAGS += " -Wall -Werror -Wextra -Wno-misleading-indentation"

CFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec',  ' `pkg-config --cflags libsafec`', '-fPIC', d)}"
CFLAGS:remove:wrynose = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec',  ' `pkg-config --cflags libsafec`', '-fPIC', d)}"

LDFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' `pkg-config --libs libsafec`', '', d)}"
LDFLAGS:remove:wrynose = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' `pkg-config --libs libsafec`', '', d)}"
LDFLAGS:remove = "${@bb.utils.contains('DISTRO_FEATURES', 'safec', '-lsafec-3.5', '', d)}"
LDFLAGS:append_dunfell = "${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' -lsafec-3.5.1 ', '', d)}"
LDFLAGS:append_kirkstone = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' -lsafec ', '', d)}"
LDFLAGS:append_wrynose = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' -lsafec ', '', d)}"
CFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', '', ' -DSAFEC_DUMMY_API', d)}"

LDFLAGS +=" -lprivilege -lsyscfg -lsecure_wrapper -lsafec"
EXTRA_OECONF:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', '--enable-notify', '', d)}"

CFLAGS:append = " \
    -I=${includedir}/dbus-1.0 \
    -I=${libdir}/dbus-1.0/include \
    -I=${includedir}/ccsp \
    -I=${includedir}/rbus \
    -I${STAGING_INCDIR}/syscfg \
    -I${STAGING_INCDIR}/safeclib \
    "
CFLAGS:append:wrynose = " -Wno-error=address"
LDFLAGS:append = " \
    -ldbus-1 \
    "

do_install:append () {
    # Config files and scripts
    install -d ${D}/usr/ccsp
    install -d ${D}/usr/ccsp/psm
    ln -sf /usr/bin/PsmSsp ${D}/usr/ccsp/PsmSsp
}

do_install:append:qemuarm () {
    # Config files and scripts
    install -d ${D}/usr/ccsp/config
    install -m 644 ${S}/config/bbhm_def_cfg_qemu.xml ${D}/usr/ccsp/config/bbhm_def_cfg.xml
}

PACKAGES += "${PN}-ccsp"

FILES:${PN}-ccsp = " \
    ${prefix}/ccsp/psm \
    ${prefix}/ccsp/PsmSsp \
    ${prefix}/ccsp/config/bbhm_def_cfg.xml \
"

FILES:${PN}-dbg = " \
    ${prefix}/ccsp/.debug \
    ${prefix}/src/debug \
    ${bindir}/.debug \
    ${libdir}/.debug \
"

# Breakpad processname and logfile mapping
BREAKPAD_LOGMAPPER_PROCLIST = "PsmSsp"
BREAKPAD_LOGMAPPER_LOGLIST = "PSMlog.txt.0"

EXTRA_OECONF:append += "${@bb.utils.contains("DISTRO_FEATURES", "MountUtils", "--enable-mountutils=yes", " ", d)}"
