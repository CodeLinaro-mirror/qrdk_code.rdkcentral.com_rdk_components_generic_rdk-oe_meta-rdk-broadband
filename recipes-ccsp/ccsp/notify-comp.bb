SUMMARY = "This receipe provides notify-comp support."
SECTION = "console/utils"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=8da35c40378155af4c5404b8f72d1237"

DEPENDS = "ccsp-common-library dbus rdk-logger utopia breakpad breakpad-wrapper"
DEPENDS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'safec', ' safec', " ", d)}"

require recipes-ccsp/ccsp/ccsp_common.inc

SRC_URI = "${CMF_GITHUB_ROOT}/notify-component;protocol=https;${BRANCH_notify_comp}"

S = "${UNPACKDIR}/${PN}-${PV}"
inherit autotools pkgconfig breakpad-wrapper coverity ${@bb.utils.contains_any("DISTRO_FEATURES", "kirkstone wrynose", "python3native", "pythonnative", d)} breakpad-logmapper

CFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'safec',  ' `pkg-config --cflags libsafec`', '-fPIC', d)}"

LDFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'safec', ' `pkg-config --libs libsafec`', '', d)}"
CFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'safec', '', ' -DSAFEC_DUMMY_API', d)}"

CFLAGS += " -Wall -Werror -Wextra -Wno-pointer-sign -Wno-sign-compare -Wno-unused-parameter"

BREAKPAD_BIN:append = " notify_comp"

LDFLAGS += "-lbreakpadwrapper -lpthread -lstdc++"

LDFLAGS:append = " -lrt"
LDFLAGS:remove_morty = " -lrt"


CFLAGS:append = " \
    -I=${includedir}/ccsp \
    "
do_compile:prepend () {
    (${PYTHON} ${STAGING_BINDIR_NATIVE}/dm_pack_code_gen.py ${S}/scripts/NotifyComponent.xml ${S}/source/NotifyComponent/dm_pack_datamodel.c)
}

do_install:append:armeb () {
    # Config files and scripts
    install -d ${D}${exec_prefix}/ccsp/notify-comp
    install -m 644 ${S}/scripts/msg_daemon.cfg ${D}${exec_prefix}/ccsp/notify-comp/msg_daemon.cfg
}

do_install:append_puma7 () {
    # Config files and scripts
    install -d ${D}${exec_prefix}/ccsp/notify-comp
    install -m 644 ${S}/scripts/msg_daemon.cfg ${D}${exec_prefix}/ccsp/notify-comp/msg_daemon.cfg
}

do_install:append:mips () {
    # Config files and scripts
    install -d ${D}${exec_prefix}/ccsp/notify-comp
    install -m 644 ${S}/scripts/msg_daemon.cfg ${D}${exec_prefix}/ccsp/notify-comp/msg_daemon.cfg
}
do_install:append_bcm3390(){
    # Config files and scripts
    install -d ${D}${exec_prefix}/ccsp/notify-comp
    install -m 644 ${S}/scripts/msg_daemon.cfg ${D}${exec_prefix}/ccsp/notify-comp/msg_daemon.cfg
}

FILES:${PN} += "${exec_prefix}/ccsp/notify-comp"

# Breakpad processname and logfile mapping
BREAKPAD_LOGMAPPER_PROCLIST = "notify_comp"
BREAKPAD_LOGMAPPER_LOGLIST = "NOTIFYLog.txt.0"
