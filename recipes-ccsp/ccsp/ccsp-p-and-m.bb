SUMMARY = "CCSP PandMSsp component"
HOMEPAGE = "http://github.com/belvedere-yocto/CcspPandM"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

RPROVIDES:${PN} = "ccsp-p-and-m"

DEPENDS = "ccsp-common-library webconfig-framework ccsp-lm-lite telemetry ccsp-hotspot mountutils"
DEPENDS:append = " utopia hal-cm hal-dhcpv4c hal-ethsw hal-mso_mgmt hal-mta hal-platform hal-vlan hal-wifi curl ccsp-misc ccsp-hotspot cjson libsyswrapper cjson trower-base64 msgpack-c nanomsg wrp-c libparodus rbus"
DEPENDS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'no_moca_support', '', 'hal-moca', d)}"
DEPENDS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'systemd', 'systemd', '', d)}"
DEPENDS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'enable_rdkscheduler', 'rdk-scheduler', " ", d)}"

DEPENDS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'safec', ' safec', " ", d)}"
DEPENDS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'fwupgrade_manager', ' hal-fwupgrade', '',d)}"

# Add remotedebugger dependency
DEPENDS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'rrd', ' remotedebugger', " ", d)}"
RDEPENDS:${PN}:append  = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'rrd',' remotedebugger', '',d)}"
CFLAGS:append     = "${@bb.utils.contains_any('DISTRO_FEATURES', 'rrd', ' -I=${includedir}/rrd/', '', d)}"
CFLAGS:append     = "${@bb.utils.contains_any('DISTRO_FEATURES', 'rrd', ' -DUSE_REMOTE_DEBUGGER', '', d)}"
 
RDEPENDS:${PN}:append = " cjson trower-base64 msgpack-c nanomsg wrp-c libparodus "

RDEPENDS:${PN}-ccsp:append = " bash"
RDEPENDS:${PN}-ccsp:remove_morty = "bash"

require ccsp_common.inc

CFLAGS += " -Wall -Werror -Wextra -Wno-shift-negative-value"

CFLAGS:append = " -Wno-deprecated-declarations -Wno-stringop-overflow -Wno-format-truncation -Wno-enum-conversion -Wno-array-bounds -Wno-misleading-indentation"

SRC_URI = "${CMF_GITHUB_ROOT}/provisioning-and-management;protocol=https;${BRANCH_ccsp_p_and_m}"

S = "${UNPACKDIR}/${PN}-${PV}"

inherit autotools ${@bb.utils.contains_any("DISTRO_FEATURES", "kirkstone wrynose", "python3native", "pythonnative", d)} breakpad-logmapper

CFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'safec',  ' `pkg-config --cflags libsafec`', '-fPIC', d)}"

LDFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'safec', ' `pkg-config --libs libsafec`', '', d)}"
LDFLAGS:remove = "${@bb.utils.contains_any('DISTRO_FEATURES', 'safec', '-lsafec-3.5', '', d)}"
LDFLAGS:append_dunfell = "${@bb.utils.contains_any('DISTRO_FEATURES', 'safec', ' -lsafec-3.5.1 ', '', d)}"
LDFLAGS:append_kirkstone = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'safec', ' -lsafec ', '', d)}"
LDFLAGS:append_wrynose = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'safec', ' -lsafec ', '', d)}"
CFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'safec', '', ' -DSAFEC_DUMMY_API', d)}"
CFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'wbCfgTestApp', '-DWEBCFG_TEST_SIM', '', d)}"
CFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'ethstats', '-DETH_STATS_ENABLED', '', d)}"
CFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'wifimotion', '-DFEATURE_COGNITIVE_WIFIMOTION', '', d)}"
LDFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'rdkb_wan_manager', '-lnanomsg', '', d)}"
LDFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'fwupgrade_manager', '-lfw_upgrade', '', d)}"
LDFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'enable_rdkscheduler', '-lrdk_scheduler', " ", d)}"

CFLAGS:append = " \
    -I${STAGING_INCDIR} \
    -I${STAGING_INCDIR}/dbus-1.0 \
    -I${STAGING_LIBDIR}/dbus-1.0/include \
    -I${STAGING_INCDIR}/ccsp \
    -I${STAGING_INCDIR}/utapi \
    -I${STAGING_INCDIR}/utctx \
    -I${STAGING_INCDIR}/ulog \
    -I${STAGING_INCDIR}/syscfg \
    -I${STAGING_INCDIR}/wrp-c \
    -I${STAGING_INCDIR}/nanomsg \
    -I${STAGING_INCDIR}/trower-base64 \
    -I${STAGING_INCDIR}/msgpackc \
    -I${STAGING_INCDIR}/libparodus \
    -I${STAGING_INCDIR}/cjson \
    -I${STAGING_INCDIR}/rbus \
    "

EXTRA_OECONF:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'systemd', '--enable-notify', '', d)}"

ENABLE_MAPT = "--enable-maptsupport=${@bb.utils.contains_any('DISTRO_FEATURES', 'nat46', 'yes', 'no', d)}"
EXTRA_OECONF:append = " ${ENABLE_MAPT}"

ENABLE_MAPT_UNIFICATION = "--enable-maptunificationsupport=${@bb.utils.contains_any('DISTRO_FEATURES', 'unified_mapt', 'yes', 'no', d)}"
EXTRA_OECONF:append = " ${ENABLE_MAPT_UNIFICATION}"

ENABLE_WIFI_MANAGE = "--enable-wifimanagesupport=${@bb.utils.contains_any('DISTRO_FEATURES', 'ManagedWiFiSupportEnable', 'yes', 'no', d)}"
EXTRA_OECONF:append = " ${ENABLE_WIFI_MANAGE}"

EXTRA_OECONF:append += "${@bb.utils.contains_any("DISTRO_FEATURES", "SpeedBoostSupportEnable", "--enable-speedboost=yes", " ", d)}"

ENABLE_HOTSPOT ?= "yes"
EXTRA_OECONF:append += " --enable-hotspotsupport=${ENABLE_HOTSPOT}"

CFLAGS:append = " -DCONFIG_VENDOR_CUSTOMER_COMCAST -DCONFIG_INTERNET2P0"
CFLAGS:append = " ${@ '-DCONFIG_CISCO_HOTSPOT' if d.getVar('ENABLE_HOTSPOT', True) == 'yes' else '-DHOTSPOT_DISABLE'}"

CFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'bci', '-DCISCO_CONFIG_TRUE_STATIC_IP -DCISCO_CONFIG_DHCPV6_PREFIX_DELEGATION -DCONFIG_CISCO_TRUE_STATIC_IP -D_BCI_FEATURE_REQ', '', d)}"

CFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'meshwifi', '-DENABLE_FEATURE_MESHWIFI', '', d)}"
CFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'dslite', '-DDSLITE_FEATURE_SUPPORT', '', d)}" 
LDFLAGS:append = " \
    -ldbus-1 \
    -lutctx \
    -lutapi \
    -lm \
    -lcjson \
    -llibparodus \
    -lnanomsg \
    -lwrp-c \
    -lmsgpackc \
    -ltrower-base64 \
    -lm \
    -lpthread \
    -lrt \
    -lsysevent \
    -ltelemetry_msgsender \
    -lrbus \
"
CFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'custom_ula', '-DCUSTOM_ULA', '', d)}"

LDFLAGS:append = " -lsyscfg"
LDFLAGS:remove_morty = " -lsyscfg"

do_compile:prepend () {

    if ${@bb.utils.contains_any('DISTRO_FEATURES', 'no_moca_support', 'true', 'false', d)}; then
    sed -i '2i <?define NO_MOCA_FEATURE_SUPPORT=True?>' ${S}/config-arm/TR181-USGv2.XML
    fi
    if ${@bb.utils.contains_any('DISTRO_FEATURES', 'no_mta_support', 'true', 'false', d)}; then
    sed -i '2i <?define NO_MTA_FEATURE_SUPPORT=True?>' ${S}/config-arm/TR181-USGv2.XML
    fi
    if ${@bb.utils.contains_any('DISTRO_FEATURES', 'interworking', 'true', 'false', d)}; then
    sed -i '2i <?define FEATURE_SUPPORT_INTERWORKING=True?>' ${S}/config-arm/TR181-USGv2.XML
    fi
    if ${@bb.utils.contains_any('DISTRO_FEATURES', 'dslite', 'true', 'false', d)}; then
    sed -i '2i <?define DSLITE_FEATURE_SUPPORT=True?>' ${S}/config-arm/TR181-USGv2.XML
    fi
    if ${@bb.utils.contains_any('DISTRO_FEATURES', 'passpoint', 'true', 'false', d)}; then
    sed -i '2i <?define FEATURE_SUPPORT_PASSPOINT=True?>' ${S}/config-arm/TR181-USGv2.XML
    fi
    if ${@bb.utils.contains_any('DISTRO_FEATURES', 'offchannel_scan_5g', 'true', 'false', d)}; then
    sed -i '2i <?define FEATURE_OFF_CHANNEL_SCAN_5G=True?>' ${S}/config-arm/TR181-USGv2.XML
    fi
    if ${@bb.utils.contains_any('DISTRO_FEATURES', 'fwupgrade_manager', 'true', 'false', d)}; then
    sed -i '2i <?define FEATURE_FWUPGRADE_MANAGER=True?>' ${S}/config-arm/TR181-USGv2.XML
    fi
    if ${@bb.utils.contains_any('DISTRO_FEATURES', 'rdkb_xdsl_ppp_manager', 'true', 'false', d)}; then
    sed -i '2i <?define FEATURE_RDKB_XDSL_PPP_MANAGER=True?>' ${S}/config-arm/TR181-USGv2.XML
    fi
    if ${@bb.utils.contains_any('DISTRO_FEATURES', 'rdkb_wan_manager', 'true', 'false', d)}; then
    sed -i '2i <?define FEATURE_RDKB_WAN_MANAGER=True?>' ${S}/config-arm/TR181-USGv2.XML
    fi
    if ${@bb.utils.contains_any('DISTRO_FEATURES', 'RadiusGreyList', 'true', 'false', d)}; then
    sed -i '2i <?define FEATURE_SUPPORT_RADIUSGREYLIST=True?>' ${S}/config-arm/TR181-USGv2.XML
    fi
    if ${@bb.utils.contains_any('DISTRO_FEATURES', 'wifimotion', 'true', 'false', d)}; then
    sed -i '2i <?define FEATURE_COGNITIVE_WIFIMOTION=True?>' ${S}/config-arm/TR181-USGv2.XML
    fi
    sed -i '2i <?define CONFIG_INTERNET2P0=True?>' ${S}/config-arm/TR181-USGv2.XML
    sed -i '2i <?define CONFIG_VENDOR_CUSTOMER_COMCAST=True?>' ${S}/config-arm/TR181-USGv2.XML
    if ${@'true' if d.getVar('ENABLE_HOTSPOT', True) == 'yes' else 'false'}; then
    sed -i '2i <?define CONFIG_CISCO_HOTSPOT=True?>' ${S}/config-arm/TR181-USGv2.XML
    sed -i '2i <?define FEATURE_HOTSPOT_SUPPORT=True?>' ${S}/config-arm/TR181-USGv2.XML
    fi
    

	if ${@bb.utils.contains_any('DISTRO_FEATURES', 'bci', 'true', 'false', d)}; then
		sed -i '2i <?define BCI=True?>' ${S}/config-arm/TR181-USGv2.XML
        sed -i '2i <?define COSA_FOR_BCI=True?>' ${S}/config-arm/TR181-USGv2.XML
        sed -i '2i <?define CONFIG_CISCO_TRUE_STATIC_IP=True?>' ${S}/config-arm/TR181-USGv2.XML
        sed -i '2i <?define CONFIG_CISCO_FILE_TRANSFER=True?>' ${S}/config-arm/TR181-USGv2.XML
        
	else
        sed -i '2i <?define FEATURE_SUPPORT_ONBOARD_LOGGING=True?>' ${S}/config-arm/TR181-USGv2.XML
        sed -i '2i <?define MOCA_HOME_ISOLATION=True?>' ${S}/config-arm/TR181-USGv2.XML

        if ${@bb.utils.contains_any('DISTRO_FEATURES', 'ddns_broadband', 'true', 'false', d)}; then
        sed -i '2i <?define DDNS_BROADBANDFORUM=True?>' ${S}/config-arm/TR181-USGv2.XML
        fi
	fi
        if ${@bb.utils.contains_any('DISTRO_FEATURES', 'custom_ula', 'true', 'false', d)}; then
        sed -i '2i <?define CUSTOM_ULA=True?>' ${S}/config-arm/TR181-USGv2.XML
        fi
        if ${@bb.utils.contains_any('DISTRO_FEATURES', 'ManagedWiFiSupportEnable', 'true', 'false', d)}; then
            sed -i '2i <?define WIFI_MANAGE_SUPPORTED=True?>' ${S}/config-arm/TR181-USGv2.XML
        fi
    (${PYTHON} ${STAGING_BINDIR_NATIVE}/dm_pack_code_gen.py ${S}/config-arm/TR181-USGv2.XML ${S}/source/PandMSsp/dm_pack_datamodel.c)
}
do_install:append () {
    # Config files and scripts
    install -d ${D}/usr/ccsp/pam
    install -d ${D}${sysconfdir}
    install -m 755 ${S}/scripts/unique_telemetry_id.sh ${D}/usr/ccsp/pam/unique_telemetry_id.sh
    install -m 755 ${S}/scripts/launch_tr69.sh ${D}/usr/ccsp/pam/launch_tr69.sh
    install -d ${D}/usr/include/ccsp
    install -d ${D}/usr/include/middle_layer_src
    install -d ${D}/usr/include/middle_layer_src/pam
    install -m 644 ${S}/source/TR-181/include/*.h ${D}/usr/include/ccsp
    install -m 644 ${S}/source/TR-181/middle_layer_src/*.h ${D}/usr/include/middle_layer_src/pam
    install -m 755 ${S}/arch/intel_usg/boards/arm_shared/scripts/partners_defaults.json ${D}/etc/partners_defaults.json
    install -m 755 ${S}/arch/intel_usg/boards/arm_shared/scripts/ScheduleAutoReboot.sh ${D}/etc/ScheduleAutoReboot.sh
    install -m 755 ${S}/arch/intel_usg/boards/arm_shared/scripts/restart_services.sh ${D}/etc/restart_services.sh
    install -m 755 ${S}/arch/intel_usg/boards/arm_shared/scripts/AutoReboot.sh ${D}/etc/AutoReboot.sh
    install -m 755 ${S}/arch/intel_usg/boards/arm_shared/scripts/RebootCondition.sh ${D}/etc/RebootCondition.sh
    #Remove mta dm entries from partners_defaults.json
    if ${@bb.utils.contains_any('DISTRO_FEATURES', 'no_mta_support', 'true', 'false', d)}; then
        sed -i -e "/MTA/d" ${D}${sysconfdir}/partners_defaults.json
    fi
}

#do_install:append:qemux86 () {
#   #Config files and scripts
#    install -m 644 ${S}/config-pc/COSAXcalibur.XML -t ${D}/usr/ccsp/pam
#}

PACKAGES += "${PN}-ccsp"

FILES:${PN}-ccsp = " \
    ${prefix}/ccsp/pam/CcspDmLib.cfg  \
    ${prefix}/ccsp/pam/CcspPam.cfg  \
    ${prefix}/ccsp/pam/email_notification_monitor.sh  \
    ${prefix}/ccsp/pam/unique_telemetry_id.sh  \
    ${prefix}/ccsp/pam/calc_random_time_to_reboot_dev.sh  \
    ${prefix}/ccsp/pam/network_response.sh  \
    ${prefix}/ccsp/pam/redirect_url.sh \
    ${prefix}/ccsp/pam/revert_redirect.sh \
    ${prefix}/ccsp/pam/whitelist.sh \
    ${prefix}/ccsp/pam/restart_services.sh \
    ${prefix}/ccsp/pam/moca_status.sh \
    ${prefix}/ccsp/pam/erouter0_ip_sync.sh \
    ${prefix}/ccsp/pam/launch_tr69.sh \
    ${prefix}/ccsp/pam/ScheduleAutoReboot.sh \
    ${prefix}/ccsp/pam/AutoReboot.sh \
    ${prefix}/ccsp/pam/RebootCondition.sh \
    /fss/gw/usr/sbin/ip \
    /fss/gw/usr/ccsp/pam/mapping.txt \
"

FILES:${PN}-ccsp:remove_no_moca_support = " \
    ${prefix}/ccsp/pam/moca_status.sh \
"

FILES:${PN}-dbg = " \
    ${prefix}/ccsp/pam/.debug \
    ${prefix}/src/debug \
    ${bindir}/.debug \
    ${libdir}/.debug \
"
#FILES:${PN}-ccsp:append:qemux86 = " \
#    ${prefix}/ccsp/pam/COSAXcalibur.XML \
#"

# Breakpad processname and logfile mapping
BREAKPAD_LOGMAPPER_PROCLIST = "CcspPandMSsp"
BREAKPAD_LOGMAPPER_LOGLIST = "PAMlog.txt.0"

EXTRA_OECONF:append += "${@bb.utils.contains_any("DISTRO_FEATURES", "MountUtils", "--enable-mountutils=yes", " ", d)}"
