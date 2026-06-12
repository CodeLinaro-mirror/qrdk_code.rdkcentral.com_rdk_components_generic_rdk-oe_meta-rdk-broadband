SUMMARY = "CCSP Command Line Interface."
HOMEPAGE = "http://github.com/belvedere-yocto/CcspDmCli"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

DEPENDS = "ccsp-common-library dbus telemetry"
DEPENDS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'safec', ' safec', " ", d)}"

require ccsp_common.inc

SRC_URI = "${CMF_GITHUB_ROOT}/data-model-cli;protocol=https;${BRANCH_ccsp_dmcli}"

S = "${WORKDIR}/git"

inherit autotools ${@bb.utils.contains_any("DISTRO_FEATURES", "kirkstone wrynose", "python3native", "pythonnative", d)}

CFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'safec',  ' `pkg-config --cflags libsafec`', '-fPIC', d)}"

LDFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'safec', ' `pkg-config --libs libsafec`', '', d)}"
LDFLAGS:remove = "${@bb.utils.contains_any('DISTRO_FEATURES', 'safec', '-lsafec-3.5', '', d)}"
LDFLAGS:append_dunfell = "${@bb.utils.contains_any('DISTRO_FEATURES', 'safec', ' -lsafec-3.5.1 ', '', d)}"
LDFLAGS:append_kirkstone = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'safec', ' -lsafec ', '', d)}"
CFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'safec', '', ' -DSAFEC_DUMMY_API', d)}"

CFLAGS:append = " \
    -I${STAGING_INCDIR}/dbus-1.0 \
    -I${STAGING_LIBDIR}/dbus-1.0/include \
    -I${STAGING_INCDIR}/ccsp \
"

LDFLAGS += "-ldbus-1 -ltelemetry_msgsender"

CFLAGS += " -Wall -Werror -Wextra "
CPPLAGS += " -Wall -Werror -Wextra "

# generating minidumps symbols
inherit breakpad-wrapper
DEPENDS += "breakpad breakpad-wrapper"
BREAKPAD_BIN:append = " dmcli"

LDFLAGS += "-lbreakpadwrapper -lpthread -lstdc++"
CFLAGS += " -DINCLUDE_BREAKPAD"

do_compile:prepend () {
    (${PYTHON} ${STAGING_BINDIR_NATIVE}/dm_pack_code_gen.py ${S}/source/MsgBusTestServer/config/MsgBusTest.XML ${S}/source/MsgBusTestServer/dm_pack_datamodel.c)
}
do_install:append () {
    # Config files and scripts
    install -d ${D}/fss/gw/usr/ccsp
    install -d ${D}/usr/ccsp/MsgBusTestServer
    ln -sf ${bindir}/dmcli ${D}/fss/gw/usr/ccsp/ccsp_bus_client_tool
    
}

do_install:append:mips () {
    # Config files and scripts
    install -d ${D}/usr/ccsp
    install -d ${D}/usr/ccsp/MsgBusTestServer
    ln -sf ${bindir}/dmcli ${D}/usr/ccsp/ccsp_bus_client_tool
   
}

do_install:append_puma7 () {
    # Config files and scripts
    ln -sf ${bindir}/dmcli ${D}${bindir}/ccsp_bus_client_tool
    install -d ${D}/usr/ccsp
    ln -sf ${bindir}/dmcli ${D}/usr/ccsp/ccsp_bus_client_tool
}

do_install:append_arrisxb3atom () {
    install -d ${D}/usr/ccsp
    install -d ${D}/usr/ccsp/MsgBusTestServer
    ln -sf ${bindir}/dmcli ${D}/usr/ccsp/ccsp_bus_client_tool
   
}

do_install:append_bcm3390 () {
    # Config files and scripts
    ln -sf ${bindir}/dmcli ${D}${bindir}/ccsp_bus_client_tool
    install -d ${D}/usr/ccsp
    install -d ${D}/usr/ccsp/MsgBusTestServer
    ln -sf ${bindir}/dmcli ${D}/usr/ccsp/ccsp_bus_client_tool
  
}

do_install:append_xb10 () {
    # Config files and scripts
    ln -sf ${bindir}/dmcli ${D}${bindir}/ccsp_bus_client_tool
    install -d ${D}/usr/ccsp
    install -d ${D}/usr/ccsp/MsgBusTestServer
    ln -sf ${bindir}/dmcli ${D}/usr/ccsp/ccsp_bus_client_tool
}

do_install:append_vbvxb9 () {
    # Config files and scripts
    ln -sf ${bindir}/dmcli ${D}${bindir}/ccsp_bus_client_tool
    install -d ${D}/usr/ccsp
    install -d ${D}/usr/ccsp/MsgBusTestServer
    ln -sf ${bindir}/dmcli ${D}/usr/ccsp/ccsp_bus_client_tool
}

do_install:append_ciscoxb3atom () {
    # Config files and scripts
    install -d ${D}/usr/ccsp
    install -d ${D}/usr/ccsp/MsgBusTestServer
   
}

PACKAGES += "${PN}-ccsp"

FILES:${PN}-ccsp = " \
    /fss/gw/usr/ccsp/* \
    ${prefix}/ccsp/* \
"

FILES:${PN}:append_arrisxb3atom = " \
    /usr/ccsp/* \
    ${prefix}/ccsp/* \
"

FILES:${PN}:append_ciscoxb3atom = " \
    /usr/ccsp/* \
    ${prefix}/ccsp/* \
"

FILES:${PN}-dbg = " \
    ${prefix}/ccsp/.debug \
    ${prefix}/src/debug \
    ${bindir}/.debug \
    ${libdir}/.debug \
"

