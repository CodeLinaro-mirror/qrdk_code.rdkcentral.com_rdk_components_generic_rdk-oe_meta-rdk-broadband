DEPENDS = "ccsp-common-library rdk-logger avro-c trower-base64 hal-cm hal-dhcpv4c hal-ethsw hal-moca hal-mso_mgmt hal-mta hal-platform hal-vlan hal-wifi msgpackc dbus util-linux utopia wrp-c nanomsg libparodus "
DEPENDS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'safec', ' safec', " ", d)}"
DEPENDS:append = "${@bb.utils.contains_any("DISTRO_FEATURES", "OneWifi", " rbus cjson ", " ", d)}"
require recipes-ccsp/ccsp/ccsp_common.inc

RDEPENDS:${PN} += "avro-c trower-base64 rdk-logger msgpackc util-linux utopia "
RDEPENDS:${PN} += "${@bb.utils.contains_any("DISTRO_FEATURES", "OneWifi", " cjson ", " ", d)}"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRC_URI = "${CMF_GITHUB_ROOT}/harvester;protocol=https;${BRANCH_harvester}"

CFLAGS += " -Wall -Werror -Wextra -Wno-unused-parameter -Wno-pointer-sign -Wno-sign-compare "


CFLAGS:append = " \
    -I${STAGING_INCDIR}/dbus-1.0 \
    -I${STAGING_LIBDIR}/dbus-1.0/include \
    -I${STAGING_INCDIR}/ccsp \
    -I${STAGING_INCDIR}/trower-base64 \
    -I${STAGING_INCDIR}/cimplog \
    -I${STAGING_INCDIR}/libparodus \
    -DFEATURE_SUPPORT_RDKLOG \
    "

S = "${UNPACKDIR}/${PN}-${PV}"

inherit autotools breakpad-logmapper

# generating minidumps symbols
inherit breakpad-wrapper
DEPENDS += "breakpad breakpad-wrapper"
BREAKPAD_BIN:append = " harvester"

LDFLAGS:append = " \
    -ldbus-1 \
    -lrdkloggers \
    -llog4c \
    -lsysevent \
    -lsyscfg \
    -lutapi \
    -lutctx \
    -lnanomsg \
    -lcimplog \
    -lwrp-c \
    -llibparodus \
    "
DEPENDS:append = "${@bb.utils.contains_any("DISTRO_FEATURES", "seshat", " libseshat ", " ", d)}"
CFLAGS:append = "${@bb.utils.contains_any("DISTRO_FEATURES", "seshat", " -DENABLE_SESHAT ", " ", d)}"
LDFLAGS:append = "${@bb.utils.contains_any("DISTRO_FEATURES", "seshat", " -llibseshat ", " ", d)}"

LDFLAGS += "-lbreakpadwrapper -lpthread -lstdc++"

CFLAGS:append = "\
    ${@bb.utils.contains_any("DISTRO_FEATURES", "seshat", "-I${STAGING_INCDIR}/libseshat ", " ", d)} \
"

inherit ${@bb.utils.contains_any("DISTRO_FEATURES", "kirkstone wrynose", "python3native", "pythonnative", d)}
CFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'safec',  ' `pkg-config --cflags libsafec`', '-fPIC', d)}"

LDFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'safec', ' `pkg-config --libs libsafec`', '', d)}"
CFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'safec', '', ' -DSAFEC_DUMMY_API', d)}"
LDFLAGS:append = "${@bb.utils.contains_any("DISTRO_FEATURES", "OneWifi", " -lrbus -lrbuscore -lrtMessage -lcjson", " ", d)}"
CFLAGS:append = "${@bb.utils.contains_any("DISTRO_FEATURES", "OneWifi", " -I${STAGING_INCDIR}/rbus -I${STAGING_INCDIR}/rtmessage -I${STAGING_INCDIR}/cjson ", " ", d)}"
CFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'OneWifi', '-DRDK_ONEWIFI', '', d)}"

EXTRA_OECONF += "${@bb.utils.contains_any("DISTRO_FEATURES", "OneWifi", " --enable-rdkOneWifi=yes ", " ", d)}"
EXTRA_OECONF:append  = " --with-ccsp-platform=bcm --with-ccsp-arch=arm "

DEPENDS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'core-net-lib', ' core-net-lib', " ", d)}"
CFLAGS:append  = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'core-net-lib', ' -DCORE_NET_LIB', '', d)}"
EXTRA_OECONF:append = " --enable-core_net_lib_feature_support=${@bb.utils.contains_any('DISTRO_FEATURES', 'core-net-lib', 'yes', 'no', d)} "

do_compile:prepend(){
	(${PYTHON} ${STAGING_BINDIR_NATIVE}/dm_pack_code_gen.py ${S}/config-atom/Harvester.XML ${S}/source/HarvesterSsp/dm_pack_datamodel.c)
}
do_install:append () {
    # Config files and scripts
    install -d ${D}/usr/ccsp/harvester
    install -m 664 ${S}/config-atom/InterfaceDevicesWifi.avsc -t ${D}/usr/ccsp/harvester
    install -m 664 ${S}/config-atom/InterfaceDevicesWifiMLO.avsc -t ${D}/usr/ccsp/harvester
    install -m 664 ${S}/config-atom/RadioInterfacesStatistics.avsc -t ${D}/usr/ccsp/harvester
    install -m 664 ${S}/config-atom/GatewayAccessPointNeighborScanReport.avsc -t ${D}/usr/ccsp/harvester
}

FILES:${PN} += " \
    ${exec_prefix}/ccsp/harvester/InterfaceDevicesWifi.avsc \
    ${exec_prefix}/ccsp/harvester/InterfaceDevicesWifiMLO.avsc \
    ${exec_prefix}/ccsp/harvester/RadioInterfacesStatistics.avsc \
    ${exec_prefix}/ccsp/harvester/GatewayAccessPointNeighborScanReport.avsc \
    ${libdir}/libwifi.so* \
"

ERROR_QA:remove_morty = "la"


# Breakpad processname and logfile mapping
BREAKPAD_LOGMAPPER_PROCLIST = "harvester"
BREAKPAD_LOGMAPPER_LOGLIST = "Harvesterlog.txt.0"
