DESCRIPTION = "Wifi Emulator Application"
LICENSE = "Apache-2.0 & MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=4c1d813ba70804c4d1704ab772a2d0b1"

DEPENDS = "rdk-wifi-emulator-hal rdk-wifi-libhostap ccsp-one-wifi rdk-wifi-halif linux-libc-headers libnl rbus libsyswrapper"

DEPENDS:remove_bananapi4-rdk-broadband = "rdk-wifi-emulator-hal"
DEPENDS:remove_raspberrypi4-64-rdk-broadband = "rdk-wifi-emulator-hal"

DEPENDS += "${@bb.utils.contains("MACHINE", "bananapi4-rdk-broadband", "rdk-wifi-hal", "", d)}"
DEPENDS += "${@bb.utils.contains("MACHINE", "raspberrypi4-64-rdk-broadband", "rdk-wifi-hal", "", d)}"

SRCREV_cpp-httplib =  "9bbb4741b4f7c8fc5083c8a56d8d301a8abc25a3"
SRCREV_FORMAT = "WifiEmulator_cpp-httplib"

SRC_URI = "git://github.com/rdkcentral/OneWifiTestSuite.git;protocol=https;branch=main;name=WifiEmulator"
SRCREV_WifiEmulator = "d2270812ba34979e15b79440dd340684c10f1f27"
SRCREV_FORMAT = "WifiEmulator"

SRC_URI += "git://github.com/yhirose/cpp-httplib;protocol=https;branch=master;destsuffix=${S}/src/external_agent_cci/temp_http_server;name=cpp-httplib;subdir=cpp-httplib"

S = "${UNPACKDIR}/${PN}-${PV}"
LDFLAGS += " -L ${STAGING_LIBDIR}"

LDFLAGS:append = " -lcjson -lcurl -lrbus -lsyscfg -lsecure_wrapper"

CXXFLAGS:append = " -I${STAGING_INCDIR}/libnl3 "
CXXFLAGS:append = " -I${STAGING_INCDIR}/ccsp "
CXXFLAGS:append = " -I${STAGING_INCDIR}/rdk-wifi-libhostap/src "
CXXFLAGS:append = " -I${STAGING_INCDIR}/rbus "
CXXFLAGS:append = " -DWIFI_HAL_VERSION_3 "
CXXFLAGS:append_tchxb7 += "  -DCONFIG_XB7_MTLS "
CXXFLAGS:append_xb10 += "  -DCONFIG_XB7_MTLS "
CXXFLAGS:append_vbvxb9 += "  -DCONFIG_XB9_MTLS "
CXXFLAGS:append_tchxb7 += " -D_XB7_PRODUCT_REQ_"
CXXFLAGS:append_tchxb8 += " -D_XB8_PRODUCT_REQ_"
CXXFLAGS:append_xb10 += " -D_XB10_PRODUCT_REQ_"
EXTRA_OECMAKE:append_bananapi4-rdk-broadband  = " -DCONFIG_EXT_AGENT_CCI=ON"
CXXFLAGS:append_bananapi4-rdk-broadband = "  -DCONFIG_EXT_AGENT_CCI "
EXTRA_OECMAKE:append_raspberrypi4-64-rdk-broadband  = " -DCONFIG_EXT_AGENT_CCI=ON"
CXXFLAGS:append_raspberrypi4-64-rdk-broadband = "  -DCONFIG_EXT_AGENT_CCI "

inherit cmake

inherit systemd pkgconfig

do_configure:prepend() {
    if [ ! -d "${S}/src/external_agent_cci/http_server/" ]; then
        mkdir -p ${S}/src/external_agent_cci/http_server/
        cp ${S}/src/external_agent_cci/temp_http_server/httplib.h ${S}/src/external_agent_cci/http_server/.
        rm -rf ${S}/src/external_agent_cci/temp_http_server/
    fi
}

do_install:append()  {
    install -d ${D}${systemd_unitdir}/system
    install -d ${D}${bindir}
    install -m 0644 ${S}/scripts/rdkfmac.service ${D}${systemd_unitdir}/system/rdkfmac.service
    install -m 0755 ${S}/scripts/rdkfmac_modprobe.sh ${D}${bindir}/rdkfmac_modprobe.sh
}

SYSTEMD_SERVICE:${PN} += "rdkfmac.service"
FILES:${PN} += "${systemd_unitdir}/system/rdkfmac.service"

SYSTEMD_SERVICE:${PN}:remove_raspberrypi4-64-rdk-broadband = "rdkfmac.service"
SYSTEMD_SERVICE:${PN}:remove_bananapi4-rdk-broadband = "rdkfmac.service"

FILES:${PN}:remove_raspberrypi4-64-rdk-broadband = "${systemd_unitdir}/system/rdkfmac.service"
FILES:${PN}:remove_bananapi4-rdk-broadband = "${systemd_unitdir}/system/rdkfmac.service"

FILES:${PN} += " \
        ${bindir}/* \
        ${base_bindir_native}/* \
        ${base_bindir}/* \
        ${systemd_unitdir}/system/* \
    "
