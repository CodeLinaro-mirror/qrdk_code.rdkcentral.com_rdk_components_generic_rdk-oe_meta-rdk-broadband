SUMMARY = "CCSP CcspCMAgent component"
HOMEPAGE = "http://github.com/belvedere-yocto/CcspCMAgent"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

DEPENDS = "utopia ccsp-common-library ccsp-misc hal-cm hal-dhcpv4c hal-ethsw hal-moca hal-mso_mgmt hal-mta hal-platform hal-vlan hal-wifi libunpriv rbus webconfig-framework curl trower-base64 msgpack-c"
DEPENDS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'safec', ' safec', " ", d)}"
DEPENDS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'systemd', 'systemd', '', d)}"
DEPENDS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'rdkb_wan_manager', ' telemetry ', ' ', d)}"
DEPENDS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'rdkb_wan_manager', ' ruli ', ' ', d)}"
DEPENDS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'rdkb_wan_manager', ' cimplog ', ' ', d)}"
DEPENDS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'core-net-lib', ' core-net-lib', " ", d)}"

require ccsp_common.inc

SRC_URI = "${CMF_GITHUB_ROOT}/cable-modem-agent;protocol=https;${BRANCH_ccsp_cm_agent}"

S = "${UNPACKDIR}/${PN}-${PV}"

inherit autotools ${@bb.utils.contains_any("DISTRO_FEATURES", "kirkstone wrynose", "python3native", "pythonnative", d)} breakpad-logmapper

CFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'safec',  ' `pkg-config --cflags libsafec`', '-fPIC', d)}"

LDFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'safec', ' `pkg-config --libs libsafec`', '', d)}"
LDFLAGS:remove = "${@bb.utils.contains_any('DISTRO_FEATURES', 'safec', '-lsafec-3.5', '', d)}"
LDFLAGS:append_dunfell = "${@bb.utils.contains_any('DISTRO_FEATURES', 'safec', ' -lsafec-3.5.1 ', '', d)}"
LDFLAGS:append_kirkstone = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'safec', ' -lsafec ', '', d)}"
LDFLAGS:append_wrynose = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'safec', ' -lsafec ', '', d)}"
CFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'safec', '', ' -DSAFEC_DUMMY_API', d)}"
CFLAGS:append =  "${@bb.utils.contains_any('DISTRO_FEATURES', 'rdkb_wan_manager', ' -DCONFIG_CISCO_HOME_SECURITY ', '', d)}"

LDFLAGS:append = "${@bb.utils.contains_any('DISTRO_FEATURES', 'rdkb_wan_manager', ' -lpthread ', '', d)}"
LDFLAGS:append = "${@bb.utils.contains_any('DISTRO_FEATURES', 'rdkb_wan_manager', ' -lhal_platform ', '', d)}"
LDFLAGS:append = "${@bb.utils.contains_any('DISTRO_FEATURES', 'rdkb_wan_manager', ' -lrt ', '', d)}"
LDFLAGS:append = "${@bb.utils.contains_any('DISTRO_FEATURES', 'rdkb_wan_manager', ' -lcimplog ', '', d)}"
LDFLAGS:append = "${@bb.utils.contains_any('DISTRO_FEATURES', 'rdkb_wan_manager', ' -ltelemetry_msgsender ', '', d)}"

CFLAGS:prepend += " ${@bb.utils.contains_any('DISTRO_FEATURES', 'highsplit', '-D_CM_HIGHSPLIT_SUPPORTED_', '', d)}"
CFLAGS:prepend += " ${@bb.utils.contains_any('DISTRO_FEATURES', 'highsplit', ' -I=${includedir}/sysevent ', '',d)} "
LDFLAGS:append += " ${@bb.utils.contains_any('DISTRO_FEATURES', 'highsplit',' -lsysevent', '',d)}"

CFLAGS:append =  "${@bb.utils.contains_any('DISTRO_FEATURES', 'lld_support', ' -DENABLE_LLD_SUPPORT ', '', d)}"

CFLAGS:append  = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'core-net-lib', ' -DCORE_NET_LIB', '', d)}"
EXTRA_OECONF:append = " --enable-core_net_lib_feature_support=${@bb.utils.contains_any('DISTRO_FEATURES', 'core-net-lib', 'yes', 'no', d)}"

EXTRA_OECONF:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'WanFailOverSupportEnable', ' --enable-wanfailover ', '', d)}"
EXTRA_OECONF:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'systemd', '--enable-notify', '', d)}"
EXTRA_OECONF:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'rdkb_wan_manager', '--enable-wanmgr', '', d)}"


CFLAGS:append = " \
    -I=${includedir}/dbus-1.0 \
    -I=${libdir}/dbus-1.0/include \
    -I=${includedir}/ccsp \
    -I${STAGING_INCDIR}/cimplog \
    -I=${includedir}/rbus \
    -I${STAGING_INCDIR}/trower-base64 \
    -I${STAGING_INCDIR}/msgpackc \
    "

LDFLAGS:append = " \
    -ldbus-1 \
    -lprivilege \
    -lsyscfg \
    -lrbus \
    "

CFLAGS += " -Wall -Werror -Wextra -Wno-enum-conversion"

do_compile:prepend () {
    if ${@bb.utils.contains_any('DISTRO_FEATURES', 'highsplit', 'true', 'false', d)}; then
        sed -i '2i <?define _CM_HIGHSPLIT_SUPPORTED_=True?>' ${S}/config-arm/TR181-CM.XML
    fi
    if ${@bb.utils.contains_any('DISTRO_FEATURES', 'rdkb_wan_manager', 'true', 'false', d)}; then
        sed -i '2i <?define FEATURE_RDKB_WAN_MANAGER=True?>' ${S}/config-arm/TR181-CM.XML
    fi
    (${PYTHON} ${STAGING_BINDIR_NATIVE}/dm_pack_code_gen.py ${S}/config-arm/TR181-CM.XML ${S}/source/CMAgentSsp/dm_pack_datamodel.c)
}

do_install:append () {
    # Config files and scripts
    install -d ${D}/usr/ccsp/cm
    ln -sf /usr/bin/CcspCMAgentSsp ${D}${prefix}/ccsp/cm/CcspCMAgentSsp
    install -d ${D}/usr/include/ccsp
    install -d ${D}/usr/include/middle_layer_src
    install -d ${D}/usr/include/middle_layer_src/cm
    install -m 644 ${S}/source/TR-181/middle_layer_src/*.h ${D}/usr/include/middle_layer_src/cm
    install -m 644 ${S}/source/TR-181/include/*.h ${D}/usr/include/ccsp
}

PACKAGES += "${PN}-ccsp"

FILES:${PN}-ccsp = " \
    ${prefix}/ccsp/cm/CcspCMAgentSsp \
    ${prefix}/ccsp/cm/CcspCMDM.cfg \
    ${prefix}/ccsp/cm/CcspCM.cfg \
"

FILES:${PN}-dbg = " \
    ${prefix}/ccsp/cm/.debug \
    ${prefix}/src/debug \
    ${bindir}/.debug \
    ${libdir}/.debug \
"
# Breakpad processname and logfile mapping
BREAKPAD_LOGMAPPER_PROCLIST = "CcspCMAgentSsp"
BREAKPAD_LOGMAPPER_LOGLIST = "CMlog.txt.0,cmconsole.log,cmevent.log"
