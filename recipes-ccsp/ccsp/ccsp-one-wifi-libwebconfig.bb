
SUMMARY = "CCSP OneWifi - webconfig encode/decode library"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=042d68aa6c083a648f58bb8d224a4d31"

# To trigger builds, change the SRC_URI to point to forked version in github with correct BRANCH where
# the changes are merged before creating a pull request to github.com/rdkcentral/OneWifi
SRC_URI = "git://github.com/rdkcentral/OneWifi.git;protocol=https;branch=main;name=libwebconfig"

SRC_URI:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'cac', '${RDKB_CCSP_ROOT_GIT}/WiFiCnxCtrl/generic;protocol=${RDK_GIT_PROTOCOL};branch=${CCSP_GIT_BRANCH};destsuffix=WiFiCnxCtrl;name=WiFiCnxCtrl', " ", d)}"

SRCREV_libwebconfig = "ef055d88d8d0dba9d9dc0f386c79dd9d0689a034"
SRCREV_WiFiCnxCtrl = "${AUTOREV}"
SRCREV_FORMAT = "libwebconfig"
PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'sta_manager', '${RDKB_CCSP_ROOT_GIT}/WiFiStaManager/generic;protocol=${RDK_GIT_PROTOCOL};branch=${CCSP_GIT_BRANCH};destsuffix=WiFiStaManager;name=WiFiStaManager', " ", d)}"
SRCREV_WiFiStaManager = "${AUTOREV}"

S = "${WORKDIR}/git"

inherit autotools pkgconfig systemd ${@bb.utils.contains_any("DISTRO_FEATURES", "kirkstone wrynose", "python3native", "pythonnative", d)} breakpad-logmapper

DEPENDS = "rdk-wifi-halif opensync-headers rbus libsyswrapper jansson webconfig-framework libev openssl"
DEPENDS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'systemd', 'systemd', '', d)}"
DEPENDS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'safec', ' safec', " ", d)}"

CFLAGS += " -Wall -Werror -Wextra -Wno-implicit-function-declaration -Wno-type-limits -Wno-unused-parameter -Wno-enum-conversion\
            -I${STAGING_INCDIR}/ccsp -I=${includedir}/rbus -DWIFI_HAL_VERSION_3"
CFLAGS:append = " -Wno-format-overflow -Wno-format-truncation -Wno-tautological-compare -Wno-stringop-truncation -fcommon"
CFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'safec',  ' `pkg-config --cflags libsafec`', '-fPIC', d)}"
CFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'safec', '', ' -DSAFEC_DUMMY_API', d)}"
CFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'meshwifi', '-DENABLE_FEATURE_MESHWIFI', '', d)}"
CFLAGS:append = " ${@bb.utils.contains_any("DISTRO_FEATURES", 'CONFIG_IEEE80211BE', ' -DCONFIG_IEEE80211BE', '', d)}"
CFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'Memwrap_Tool', '-DONEWIFI_MEMWRAPTOOL_APP_SUPPORT', '', d)}"
CFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'sta_manager', '-DONEWIFI_STA_MGR_APP_SUPPORT', '', d)}"


EXTRA_OECONF:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'cac', 'ONEWIFI_CAC_APP_SUPPORT=true', 'ONEWIFI_CAC_APP_SUPPORT=false', d)}"
EXTRA_OECONF:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'dbus_support', 'ONEWIFI_DBUS_SUPPORT=true', 'ONEWIFI_DBUS_SUPPORT=false', d)}"
EXTRA_OECONF:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'Memwrap_Tool', 'ONEWIFI_MEMWRAPTOOL_APP_SUPPORT=true', 'ONEWIFI_MEMWRAPTOOL_APP_SUPPORT=false', d)}"
CFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'cac', '-DONEWIFI_CAC_APP_SUPPORT', '', d)}"
CFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'wps_support', '-DFEATURE_SUPPORT_WPS', '', d)}"
EXTRA_OECONF:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'sta_manager', 'ONEWIFI_STA_MGR_APP_SUPPORT=true', 'ONEWIFI_STA_MGR_APP_SUPPORT=false', d)}"
CFLAGS:append_kirkstone = " -Wno-deprecated-declarations"
CFLAGS:append_wrynose = " -Wno-deprecated-declarations"

LDFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'dbus_support', '-ldbus-1', '-lrbus', d)} "
LDFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'safec', ' `pkg-config --libs libsafec`', '', d)}"
LDFLAGS:remove = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'safec', ' -lsafec-3.5 ', '', d)}"
LDFLAGS:append_dunfell = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'safec', ' -lsafec-3.5.1 ', '', d)}"
LDFLAGS:append_kirkstone = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'safec', ' -lsafec ', '', d)}"
LDFLAGS:append_wrynose = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'safec', ' -lsafec ', '', d)}"

EXTRA_OECONF:append = " --enable-libwebconfig"
EXTRA_OECONF:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'systemd', '--enable-notify', '', d)}"
ISSYSTEMD = "${@bb.utils.contains_any('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}"

do_compile:prepend () {
    # Copy files specific to the cac cac distribution
    if ${@bb.utils.contains_any('DISTRO_FEATURES', 'cac', 'true', 'false', d)}; then
        mkdir -p ${S}/source/apps/cac/
        cp -rf ${S}/../WiFiCnxCtrl/source/apps/cac/* ${S}/source/apps/cac/
    fi
    if ${@bb.utils.contains_any('DISTRO_FEATURES', 'sta_manager', 'true', 'false', d)}; then
        cp -rf ${S}/../WiFiStaManager/* ${S}/source/apps/sta_mgr/
    fi
}

do_compile() {
    oe_runmake -C source/webconfig
}

do_install() {
    oe_runmake -C source/webconfig DESTDIR=${D} install

    install -d ${D}/usr/include/ccsp
    install -m 644 ${S}/include/webconfig_external_proto_ovsdb.h  ${D}/usr/include/ccsp
    install -m 644 ${S}/include/webconfig_external_proto.h  ${D}/usr/include/ccsp
    install -m 644 ${S}/include/webconfig_external_proto_tr181.h  ${D}/usr/include/ccsp
    install -m 644 ${S}/include/wifi_webconfig.h       ${D}/usr/include/ccsp
    install -m 644 ${S}/include/wifi_base.h       ${D}/usr/include/ccsp
    install -m 644 ${S}/source/utils/collection.h ${D}/usr/include/ccsp
}

FILES:${PN} += "${libdir}/*.so*"

FILES_SOLIBSDEV = ""
INSANE_SKIP:${PN} += "dev-so"

ERROR_QA:remove_morty = "la"

# Breakpad processname and logfile mapping
