SUMMARY = "CCSP test and diagnostice utilities."
HOMEPAGE = "http://github.com/belvedere-yocto/TestAndDiagnostic"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=06093b681f6d882a55e3bc222a02a988"

DEPENDS = "ccsp-common-library utopia hal-cm hal-dhcpv4c hal-ethsw hal-moca hal-mso_mgmt hal-mta hal-platform hal-vlan hal-wifi rbus libev libpcap telemetry"
DEPENDS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'safec', ' safec', " ", d)}"
DEPENDS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'core-net-lib', ' core-net-lib', " ", d)}"
DEPENDS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'enable_rdkscheduler', ' trower-base64 msgpack-c rdk-scheduler cimplog', " ", d)}"

require recipes-ccsp/ccsp/ccsp_common.inc

SRC_URI = "${CMF_GITHUB_ROOT}/test-and-diagnostic;protocol=https;${BRANCH_test_and_diagnostic}"

S = "${WORKDIR}/git"

CFLAGS += " -Wall -Werror -Wextra -Wno-pointer-sign -Wno-sign-compare -Wno-type-limits -Wno-unused-parameter -Wno-format -Wno-misleading-indentation"

CFLAGS:append_kirkstone = " -fcommon"
CFLAGS:append_wrynose = " -fcommon"

RDEPENDS:${PN} += "libpcap"
RDEPENDS:${PN}:append = " bash"
RDEPENDS:${PN}-ccsp:append = " bash"
RDEPENDS:${PN}:remove_morty = "bash"
RDEPENDS:${PN}-ccsp:remove_morty = "bash"

inherit autotools ${@bb.utils.contains_any("DISTRO_FEATURES", "kirkstone wrynose", "python3native", "pythonnative", d)} breakpad-logmapper

CFLAGS:append = " \
    -I${STAGING_INCDIR} \
    -I${STAGING_INCDIR}/dbus-1.0 \
    -I${STAGING_LIBDIR}/dbus-1.0/include \
    -I${STAGING_INCDIR}/ccsp \
    -I${STAGING_INCDIR}/utapi \
    -I${STAGING_INCDIR}/utctx \
    -I${STAGING_INCDIR}/ulog \
    -I${STAGING_INCDIR}/syscfg \
    "

CFLAGS:append += "${@bb.utils.contains_any('DISTRO_FEATURES', 'enable_rdkscheduler',' -I${STAGING_INCDIR}/trower-base64 -I${STAGING_INCDIR}/msgpackc -I${STAGING_INCDIR}/cimplog','',d)}"

EXTRA_OECONF:append = "--enable-mta"

LDFLAGS:append = " \
    -ldbus-1 \
    "

# Fan & Thermal Control Feature
HASTHERMAL = "${@bb.utils.contains_any('DISTRO_FEATURES', 'thermalctrl', 'true', 'false', d)}"
CFLAGS:append += "${@bb.utils.contains_any('DISTRO_FEATURES', 'thermalctrl','-DFAN_THERMAL_CTR','',d)}"
LDFLAGS:append += "${@bb.utils.contains_any('DISTRO_FEATURES', 'thermalctrl',' -lhal_platform','',d)}"

CFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'safec',  ' `pkg-config --cflags libsafec`', '-fPIC', d)}"

LDFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'safec', ' `pkg-config --libs libsafec`', '', d)}"
CFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'safec', '', ' -DSAFEC_DUMMY_API', d)}"

# Limited Warehouse Parameters if XB doesn't support Full Fan Control
CFLAGS:append += "${@bb.utils.contains_any('DISTRO_FEATURES', 'warehouseFan','-DLIMITED_FAN_WAREHOUSE','',d)}"
LDFLAGS:append += "${@bb.utils.contains_any('DISTRO_FEATURES', 'warehouseFan',' -lhal_platform','',d)}"

CFLAGS:append  = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'core-net-lib', ' -DCORE_NET_LIB', '', d)}"
EXTRA_OECONF:append = " --enable-core_net_lib_feature_support=${@bb.utils.contains_any('DISTRO_FEATURES', 'core-net-lib', 'yes', 'no', d)} "

EXTRA_OECONF:append = "${@bb.utils.contains_any('DISTRO_FEATURES', 'enable_device_prioritization',' --enable-device_prioritization','',d)}"
EXTRA_OECONF:append = "${@bb.utils.contains_any('DISTRO_FEATURES', 'enable_rdkscheduler',' --enable-rdk_scheduler','',d)}"
LDFLAGS:append = "${@bb.utils.contains_any('DISTRO_FEATURES', 'enable_rdkscheduler',' -lcimplog','',d)}"
EXTRA_OECONF:remove = "${@bb.utils.contains_any('DISTRO_FEATURES', 'no_mta_support', '--enable-mta', '', d)}"

do_compile:prepend () {
    if ${@bb.utils.contains_any('DISTRO_FEATURES', 'no_fan_support', 'true', 'false', d)}; then
    sed -i '2i <?define NO_FAN_FEATURE_SUPPORT=True?>' ${S}/config/TestAndDiagnostic_arm.XML
    fi
    
    (${PYTHON} ${STAGING_BINDIR_NATIVE}/dm_pack_code_gen.py ${S}/config/TestAndDiagnostic_arm.XML ${S}/source/TandDSsp/dm_pack_datamodel.c)
}
do_install:append () {
    # Config files and scripts
    install -d ${D}/usr/ccsp/tad
    install -d ${D}/usr/include/ccsp
    ln -sf /usr/bin/CcspTandDSsp ${D}/usr/ccsp/tad/CcspTandDSsp
    install -m 644 ${S}/source/dmltad/diag*.h ${D}/usr/include/ccsp/
   
    install -m 755 ${S}/source/CpuMemFrag/cpumemfrag_cron.sh ${D}/usr/ccsp/tad/cpumemfrag_cron.sh
    install -m 755 ${S}/source/CpuMemFrag/log_buddyinfo.sh ${D}/usr/ccsp/tad/log_buddyinfo.sh

    if [ ${HASTHERMAL} = "true" ]; then
    	install -m 755 ${S}/source/ThermalCtrl/check_fan.sh ${D}/usr/ccsp/tad/check_fan.sh
    fi
    if ${@bb.utils.contains_any('DISTRO_FEATURES', 'bci', 'true', 'false', d)}; then
        if [ "${MACHINE_IMAGE_NAME}" = "CGA4332COM" ] || [ "${MACHINE_IMAGE_NAME}" = "CGA4131COM" ]; then
            install -m 0755 ${S}/source/StaticInfo/log_staticIP_client_info.sh ${D}/usr/ccsp/tad/log_staticIP_client_info.sh
        fi
    fi
}

PACKAGES += "${PN}-ccsp"

FILES:${PN}-ccsp = " \
    ${libdir}/libdiagnostic.so.* \
    ${libdir}/libdmltad.so.* \
    ${prefix}/ccsp/Sub64 \
    ${bindir}/Sub64 \
    ${bindir}/CcspTandDSsp \
    /fss/gw/usr/ccsp/* \
    ${prefix}/ccsp/tad/*.sh \
    ${prefix}/ccsp/tad/CcspTandDSsp \
    ${sbindir}/* \
"

FILES:${PN}-dbg = " \
    ${prefix}/ccsp/tad/.debug \
    ${prefix}/src/debug \
    ${bindir}/.debug \
    ${libdir}/.debug \
"

# Breakpad processname and logfile mapping
BREAKPAD_LOGMAPPER_PROCLIST = "CcspTandDSsp"
BREAKPAD_LOGMAPPER_LOGLIST = "TDMlog.txt.0"
