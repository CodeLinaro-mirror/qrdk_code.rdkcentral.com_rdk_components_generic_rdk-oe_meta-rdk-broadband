SUMMARY = "Test Development Kit for RDKB stack"
SECTION = "console/utils"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${CMF_GITHUB_ROOT}/tdk-broadband;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GITHUB_MAIN_BRANCH};name=tdkb"
SRCREV_tdkb = "${AUTOREV}"
SRCREV_FORMAT = "tdkb"

S = "${UNPACKDIR}/${PN}-${PV}"

DEPENDS += "jsoncpp jsonrpc ccsp-common-library ccsp-lm-lite hal-cm hal-dhcpv4c hal-ethsw hal-moca hal-mso_mgmt hal-mta hal-platform hal-vlan hal-wifi ccsp-cm-agent ccsp-mta-agent ccsp-p-and-m test-and-diagnostic trower-base64 rbus rdkb-halif-fwupgrade"

DEPENDS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' safec', " ", d)}"

DEPENDS:append = " ${@bb.utils.contains("DISTRO_FEATURES", 'OneWifi', ' ccsp-one-wifi', ' ccsp-wifi-agent', d)}"

RDEPENDS:${PN} = "jsoncpp jsonrpc ccsp-cm-agent bash trower-base64"

require recipes-ccsp/ccsp/ccsp_common.inc

tdkdir = "/usr/ccsp/tdk/"

inherit autotools systemd coverity

CFLAGS += " -Wall -Werror -Wextra -Wno-unused-parameter -Wno-unused-but-set-parameter -Wno-pointer-sign -Wno-sign-compare -Wno-implicit-function-declaration "

CFLAGS:append = " \
    -I=${includedir}/dbus-1.0 \
    -I=${libdir}/dbus-1.0/include \
    -I=${includedir}/ccsp \
    -I=${includedir}/middle_layer_src/pam \
    -I=${includedir}/middle_layer_src/mta \
    -I=${includedir}/middle_layer_src/cm \
    -I=${includedir}/middle_layer_src/wifi \
    -I=${includedir}/cimplog \
    -I${STAGING_DIR_TARGET}${includedir}/trower-base64 \
    "

CFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec',  ' `pkg-config --cflags libsafec`', '-fPIC', d)}"

CFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', '', ' -DSAFEC_DUMMY_API', d)}"

CFLAGS:append  = " ${@bb.utils.contains('DISTRO_FEATURES', 'rdkb_wan_manager', '-DFEATURE_RDKB_WAN_MANAGER', '', d)}"

CPPFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'OneWifi', '-DRDK_ONEWIFI', '', d)}"

LDFLAGS:append = " \
    -ldbus-1 \
    -ltrower-base64 \
    "
LDFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' `pkg-config --libs libsafec`', '', d)}"
LDFLAGS:remove = "${@bb.utils.contains('DISTRO_FEATURES', 'safec', '-lsafec-3.5', '', d)}"
LDFLAGS:append_dunfell = "${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' -lsafec-3.5.1 ', '', d)}"
LDFLAGS:append_kirkstone = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' -lsafec ', '', d)}"
LDFLAGS:append_wrynose = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' -lsafec ', '', d)}"

#Adding new package "tdk-b-dl" which will be downloaded package only of tdk_rdm distro feature is enabled
TDKB_DL_PACK:= "${@bb.utils.contains('DISTRO_FEATURES', 'tdk_rdm', '${PN}-dl', '', d)}"
PACKAGE_BEFORE_PN += "${TDKB_DL_PACK}"

#ENABLE_WAN_MANAGER = "${@bb.utils.contains('DISTRO_FEATURES', 'rdkb_wan_manager', '--enable-rdkb_wan_manager', '--disable-rdkb_wan_manager', d)}"
#EXTRA_OECONF:append = "${ENABLE_WAN_MANAGER}"
#EXTRA_OECONF:append  = " --with-ccsp-platform=bcm --with-ccsp-arch=arm "


# Install all TDK scripts
do_install:append () {
    install -d ${D}/${tdkdir}
    install -d ${D}/${sbindir}
    install -d ${D}/${systemd_unitdir}/system
    install -D -p -m 755 ${S}/agent/scripts/TDK_version.txt ${D}/
    install -D -p -m 755 ${S}/agent/scripts/*.sh ${D}/${tdkdir}
    install -D -p -m 755 ${S}/agent/scripts/tdk_cmd_utility.config ${D}/${tdkdir}
    install -D -p -m 755 ${S}/agent/scripts/tdkb_launcher.sh ${D}/${sbindir}

    install -m 0644 ${S}/tdk.service ${D}/${systemd_unitdir}/system/tdk.service

    install -D -p -m 755 ${S}/tdkb_lib/cfg/TDKB.cfg ${D}/${tdkdir}
    install -D -p -m 755 ${S}/tdkb_lib/cfg/TDKBDM.cfg ${D}/${tdkdir}
    install -D -p -m 755 ${S}/tdkb_lib/cfg/TR181-TDKB.XML ${D}/${tdkdir}

    rm ${D}${bindir}/runSysStat.sh
    rm ${D}${bindir}/RemoveLogs.sh
    rm ${D}${bindir}/file_copy.sh
    rm ${D}${bindir}/TDK_version.txt
    rm ${D}/${tdkdir}/tdkb_launcher.sh
}

#In both RDM and non RDM scenarios, below startup script and service files will be part of tdk package only
SYSTEMD_SERVICE:${PN} = "tdk.service"

#In both RDM and non RDM scenarios, below files will be part of tdk package only
FILES:${PN} = " \
    /TDK_version.txt \
    /etc/tdk_platform.properties \
    ${sbindir}/tdkb_launcher.sh \
"

#All artifacts will be part of tdk-b package when tdk_rdm distro is not present (in non rdm tdk-b builds)
FILES:${PN} += "${@bb.utils.contains('DISTRO_FEATURES', 'tdk_rdm', ' ', ' ${bindir}/rdk_tdk_agent_process ${bindir}/tdk_cmd_utility ${libdir}/*.so* ${tdkdir}/* /etc/*', d)"

#All artifacts will be packed in tdk-b-dl package when tdk_rdm distro is enabled
FILES:${PN}-dl = "${@bb.utils.contains('DISTRO_FEATURES', 'tdk_rdm', ' ${bindir}/rdk_tdk_agent_process ${bindir}/tdk_cmd_utility ${libdir}/*.so* ${tdkdir}/* /etc/* ', '', d)"

FILES:${PN}-dbg = " \
    ${prefix}/src/debug \
    ${bindir}/.debug \
    ${libdir}/.debug \
    ${tdkdir}/.debug \
"
do_compile:prepend() {
     (${PYTHON} ${STAGING_BINDIR_NATIVE}/dm_pack_code_gen.py ${S}/tdkb_lib/cfg/TR181-TDKB.XML ${S}/tdkb_lib/src/dm_pack_datamodel.c)
}

