DEPENDS:append = " libunpriv "
LDFLAGS:append = " \
                 -lprivilege \
                 "

SUMMARY = "CCSP OneWifi component"
HOMEPAGE = "http://github.com/belvedere-yocto/OneWifi"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=042d68aa6c083a648f58bb8d224a4d31"
DEPENDS = "webconfig-framework telemetry libsyswrapper libev rbus libnl ccsp-one-wifi-libwebconfig trower-base64"
DEPENDS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'systemd', 'systemd', '', d)}"
DEPENDS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'safec', ' safec', " ", d)}"
#DEPENDS:append = " hal-cm  hal-dhcpv4c hal-ethsw hal-moca hal-mso_mgmt hal-mta hal-platform hal-vlan hal-wifi avro-c "

CFLAGS:prepend += "-I${PKG_CONFIG_SYSROOT_DIR}/usr/include/libnl3 "
CFLAGS:prepend += "-I${PKG_CONFIG_SYSROOT_DIR}/usr/include/ "
CFLAGS:append = " \
    -I${STAGING_INCDIR}/trower-base64 \
    "

DEPENDS:append += " libunpriv"

CFLAGS += " -Wall -Werror -Wextra -Wno-implicit-function-declaration -Wno-type-limits -Wno-unused-parameter "

CFLAGS:append = " -Wno-format-overflow -Wno-format-truncation -Wno-tautological-compare -Wno-stringop-truncation -Wno-enum-conversion -fcommon -Wno-mismatched-dealloc"

# To trigger builds, change the SRC_URI to point to forked version in github with correct BRANCH where
# the changes are merged before creating a pull request to github.com/rdkcentral/OneWifi
SRC_URI = "git://github.com/rdkcentral/OneWifi.git;protocol=https;branch=main;name=OneWifi"

SRC_URI:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'cac', '${RDKB_CCSP_ROOT_GIT}/WiFiCnxCtrl/generic;protocol=${RDK_GIT_PROTOCOL};branch=${CCSP_GIT_BRANCH};destsuffix=WiFiCnxCtrl;name=WiFiCnxCtrl', " ", d)}"

SRCREV_OneWifi = "ef055d88d8d0dba9d9dc0f386c79dd9d0689a034"
SRCREV_WiFiCnxCtrl = "${AUTOREV}"
SRCREV_FORMAT = "OneWifi"

SRC_URI:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'sta_manager', '${RDKB_CCSP_ROOT_GIT}/WiFiStaManager/generic;protocol=${RDK_GIT_PROTOCOL};branch=${CCSP_GIT_BRANCH};destsuffix=WiFiStaManager;name=WiFiStaManager', " ", d)}"
SRCREV_WiFiStaManager = "${AUTOREV}"

S = "${WORKDIR}/git"

PV = "${RDK_RELEASE}+git${SRCPV}"

inherit autotools pkgconfig systemd ${@bb.utils.contains_any("DISTRO_FEATURES", "kirkstone wrynose", "python3native", "pythonnative", d)} breakpad-logmapper

CFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'safec',  ' `pkg-config --cflags libsafec`', '-fPIC', d)}"

LDFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'safec', ' `pkg-config --libs libsafec`', '', d)}"
LDFLAGS:remove = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'safec', ' -lsafec-3.5 ', '', d)}"
LDFLAGS:append_dunfell = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'safec', ' -lsafec-3.5.1 ', '', d)}"
LDFLAGS:append_kirkstone = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'safec', ' -lsafec ', '', d)}"
LDFLAGS:append_wrynose = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'safec', ' -lsafec ', '', d)}"
CFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'safec', '', ' -DSAFEC_DUMMY_API', d)}"
EXTRA_OECONF:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'cac', 'ONEWIFI_CAC_APP_SUPPORT=true', 'ONEWIFI_CAC_APP_SUPPORT=false', d)}"
EXTRA_OECONF:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'dbus_support', 'ONEWIFI_DBUS_SUPPORT=true', 'ONEWIFI_DBUS_SUPPORT=false', d)}"
EXTRA_OECONF:append = " --disable-libwebconfig"
EXTRA_OECONF:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'systemd', '--enable-notify', '', d)}"
EXTRA_OECONF:append  = " --with-ccsp-platform=bcm --with-ccsp-arch=arm "
EXTRA_OECONF:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'sta_manager', 'ONEWIFI_STA_MGR_APP_SUPPORT=true', 'ONEWIFI_STA_MGR_APP_SUPPORT=false', d)}"
CFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'disable_nl80211_acl', '', ' -DNL80211_ACL', d)}"
EXTRA_OECONF:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'EasyConnect', '--enable-easyconnect', '', d)}"
ISSYSTEMD = "${@bb.utils.contains_any('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}"
CFLAGS:append = " \
    -I${STAGING_INCDIR}/ccsp \
    -I=${includedir}/rbus \
"

CFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'meshwifi', '-DENABLE_FEATURE_MESHWIFI', '', d)}"
CFLAGS:append = " -DWIFI_CAPTIVE_PORTAL"
CFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'halVersion3', ' -DWIFI_HAL_VERSION_3', '', d)}"
CFLAGS:append = " -DWIFI_CAPTIVE_PORTAL"
CFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'onewifi_integration', '-DNEWPLATFORM_PORT', '', d)}"
CFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'cac', '-DONEWIFI_CAC_APP_SUPPORT', '', d)}"
CFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'wps_support', '-DFEATURE_SUPPORT_WPS', '', d)}"
CFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'dbus_support', '-DONEWIFI_DBUS_SUPPORT', '', d)}"
CFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'always_enable_ax_2g', '-DALWAYS_ENABLE_AX_2G', '', d)}" 

EXTRA_OECONF:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'hal-ipc', 'HAL_IPC=true', '', d)}"
CFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'hal-ipc', ' -DHAL_IPC', '', d)}"

#!FIXME!
#Ensure proper propagation of CFLAGS and LIBS through the build system.
#configure.ac:
#PKG_CHECK_MODULES([LIBHOSTAP], [libhostap >= 2.9])
#
#Makefile.am:
#target_name_CFLAGS += ${LIBHOSTAP_CFLAGS}
#target_name_LDFLAGS += ${LIBHOSTAP_LIBS}
CFLAGS:append = " `pkg-config --exists libhostap && pkg-config --cflags libhostap`"

LDFLAGS:append = " \
    -ltelemetry_msgsender \
    -lrbus \
    -lwifi_webconfig \
    -lm \
    -lcjson \
"
LDFLAGS:append += " -lprivilege"
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

DEPENDS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'Memwrap_Tool', 'libmemfnswrap', '', d)}"
RDEPENDS:${PN}:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'Memwrap_Tool', 'libmemfnswrap', '', d)}"
LDFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'Memwrap_Tool', ' -lmemfnswrap', '', d)}"

do_install:append () {
    # Config files and scripts
    install -d ${D}/usr/ccsp/wifi
    install -m 664 ${S}/scripts/process_monitor_atom.sh -t ${D}/usr/ccsp/wifi
    install -m 755 ${S}/scripts/br0_ip.sh -t ${D}/usr/ccsp/wifi
    install -m 755 ${S}/scripts/br106_addvlan.sh -t ${D}/usr/ccsp/wifi
    install -m 755 ${S}/scripts/copy_wifi_logs.sh -t ${D}/usr/ccsp/wifi
    install -m 755 ${S}/scripts/wifi_logupload.sh -t ${D}/usr/ccsp/wifi
    install -m 755 ${S}/scripts/OneWiFi_Selfheal.sh -t ${D}/usr/ccsp/wifi
    install -m 755 ${S}/scripts/OneWiFi_vap_down.sh -t ${D}/usr/ccsp/wifi
    install -m 755 ${S}/scripts/lfp.sh -t ${D}/usr/ccsp/wifi
    install -m 755 ${S}/scripts/aphealth.sh -t ${D}/usr/ccsp/wifi
    install -m 755 ${S}/scripts/aphealth_log.sh -t ${D}/usr/ccsp/wifi
    install -m 755 ${S}/scripts/wifivAPPercentage.sh -t ${D}/usr/ccsp/wifi
    # Only install services if meshwifi has been defined.
    if ${@bb.utils.contains_any('DISTRO_FEATURES', 'meshwifi', 'true', 'false', d)}; then
        install -m 755 ${S}/scripts/mesh_aclmac.sh -t ${D}/usr/ccsp/wifi
        install -m 755 ${S}/scripts/mesh_setip.sh -t ${D}/usr/ccsp/wifi
        install -m 755 ${S}/scripts/meshapcfg.sh -t ${D}/usr/ccsp/wifi
        install -m 755 ${S}/scripts/handle_mesh -t ${D}/usr/ccsp/wifi
        install -m 755 ${S}/scripts/mesh_status.sh -t ${D}/usr/ccsp/wifi
    fi

    if ${@bb.utils.contains_any('DISTRO_FEATURES', 'Memwrap_Tool', 'true', 'false', d)}; then
	install -m 755 ${S}/scripts/Heapwalkcheckrss.sh -t ${D}/usr/ccsp/wifi
	install -m 755 ${S}/scripts/HeapwalkField.sh -t ${D}/usr/ccsp/wifi
    fi

    install -m 775 ${S}/config/CcspWifi.cfg -t ${D}/usr/ccsp/wifi
    install -m 775 ${S}/config/CcspDmLib.cfg -t ${D}/usr/ccsp/wifi
    install -m 664 ${S}/config/WifiSingleClient.avsc -t ${D}/usr/ccsp/wifi
    install -m 664 ${S}/config/WifiSingleClientActiveMeasurement.avsc -t ${D}/usr/ccsp/wifi
    install -m 664 ${S}/config/rdkb-wifi.ovsschema -t ${D}/usr/ccsp/wifi
    install -m 755 ${D}/usr/bin/wifi_db_ovsh -t ${D}/usr/ccsp/wifi
    rm ${D}/usr/bin/wifi_db_ovsh
    install -d ${D}/usr/include/ccsp
    install -d ${D}/usr/include/middle_layer_src
    install -d ${D}/usr/include/middle_layer_src/wifi
    install -m 644 ${S}/source/dml/tr_181/sbapi/*.h ${D}/usr/include/ccsp
    install -m 644 ${S}/include/tr_181/ml/*.h ${D}/usr/include/middle_layer_src/wifi

	if [ ${ISSYSTEMD} = "true" ]; then
    	install -d ${D}${systemd_unitdir}/system
    	install -D -m 755 ${S}/scripts/wifiTelemetrySetup.sh ${D}${exec_prefix}/ccsp/wifi/wifiTelemetrySetup.sh
    	install -D -m 0644 ${S}/scripts/systemd/wifi-telemetry.target ${D}${systemd_unitdir}/system/wifi-telemetry.target
    	install -D -m 0644 ${S}/scripts/systemd/wifi-telemetry-cron.service ${D}${systemd_unitdir}/system/wifi-telemetry-cron.service
	fi
    install -d ${D}/usr/sbin
    install -m 755 ${S}/scripts/get_vlan.sh -t ${D}/usr/sbin
}

do_install:append:mips (){
    install -d ${D}/usr/ccsp/wifi
    install -m 775 ${S}/config/CcspWifi.cfg -t ${D}/usr/ccsp/wifi
    install -m 775 ${S}/config/CcspDmLib.cfg -t ${D}/usr/ccsp/wifi
}

do_install:append_puma7 () {
    rm ${D}/usr/ccsp/wifi/br0_ip.sh
}

do_install:append_bcm3390() {
    rm ${D}/usr/ccsp/wifi/br0_ip.sh
}

FILES:${PN} = "\
    ${bindir}/OneWifi \
    ${bindir}/wifi_ctrl \
    ${bindir}/onewifi_component_test_app \
    ${bindir}/wifi_api2 \
    ${libdir}/libwifi.so* \
    ${prefix}/ccsp/wifi/process_monitor_atom.sh \
    ${prefix}/ccsp/wifi/br0_ip.sh \
    ${prefix}/ccsp/wifi/br106_addvlan.sh \
    ${prefix}/ccsp/wifi/copy_wifi_logs.sh \
    ${prefix}/ccsp/wifi/wifi_logupload.sh \
    ${prefix}/ccsp/wifi/OneWiFi_Selfheal.sh \
    ${prefix}/ccsp/wifi/OneWiFi_vap_down.sh \
    ${prefix}/ccsp/wifi/lfp.sh \
    ${prefix}/ccsp/wifi/aphealth.sh \
    ${prefix}/ccsp/wifi/aphealth_log.sh \
    ${prefix}/ccsp/wifi/apshealth.sh \
    ${prefix}/ccsp/wifi/wifivAPPercentage.sh \
    ${prefix}/ccsp/wifi/mesh_aclmac.sh \
    ${prefix}/ccsp/wifi/mesh_setip.sh \
    ${prefix}/ccsp/wifi/meshapcfg.sh \
    ${prefix}/ccsp/wifi/handle_mesh \
    ${prefix}/ccsp/wifi/mesh_status.sh \
    ${prefix}/ccsp/wifi/CcspWifi.cfg \
    ${prefix}/ccsp/wifi/CcspDmLib.cfg \
    ${prefix}/ccsp/wifi/WifiSingleClient.avsc \
    ${prefix}/ccsp/wifi/WifiSingleClientActiveMeasurement.avsc \
    ${prefix}/ccsp/wifi/rdkb-wifi.ovsschema \
    ${prefix}/ccsp/wifi/wifi_db_ovsh \
    ${sbindir}/get_vlan.sh \
"
FILES:${PN}:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'Memwrap_Tool', '${prefix}/ccsp/wifi/Heapwalkcheckrss.sh', '', d)}"
FILES:${PN}:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'Memwrap_Tool', '${prefix}/ccsp/wifi/HeapwalkField.sh', '', d)}"

FILES:${PN}-dbg = " \
    ${prefix}/ccsp/wifi/.debug \
    ${prefix}/src/debug \
    ${bindir}/.debug \
    ${libdir}/.debug \
"

SYSTEMD_SERVICE:${PN} += " ${@bb.utils.contains_any('DISTRO_FEATURES', 'systemd', 'wifi-telemetry.target', '', d)}"
SYSTEMD_SERVICE:${PN} += " ${@bb.utils.contains_any('DISTRO_FEATURES', 'systemd', 'wifi-telemetry-cron.service', '', d)}"

FILES:${PN}:append += " ${@bb.utils.contains_any('DISTRO_FEATURES', 'systemd', '${exec_prefix}/ccsp/wifi/wifiTelemetrySetup.sh', '', d)}"
FILES:${PN}:append += " ${@bb.utils.contains_any('DISTRO_FEATURES', 'systemd', '${systemd_unitdir}/system/wifi-telemetry.target', '', d)}"
FILES:${PN}:append += " ${@bb.utils.contains_any('DISTRO_FEATURES', 'systemd', '${systemd_unitdir}/system/wifi-telemetry-cron.service', '', d)}"

ERROR_QA:remove_morty = "la"

# Breakpad processname and logfile mapping
BREAKPAD_LOGMAPPER_PROCLIST = "OneWifi,log_agent"
BREAKPAD_LOGMAPPER_LOGLIST = "WiFilog.txt.0,WifiConsole.txt,wifihealth.txt"
