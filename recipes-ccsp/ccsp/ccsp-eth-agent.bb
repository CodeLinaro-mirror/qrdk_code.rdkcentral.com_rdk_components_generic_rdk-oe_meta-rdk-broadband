SUMMARY = "This receipe provides test component support."
SECTION = "console/utils"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

DEPENDS = "ccsp-common-library dbus utopia hal-ethsw hal-platform curl ccsp-lm-lite libunpriv"
DEPENDS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'safec', ' safec', " ", d)}"
DEPENDS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'WanFailOverSupportEnable', ' rbus ', " ", d)}"
DEPENDS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'core-net-lib', ' core-net-lib', " ", d)}"
require ccsp_common.inc

SRC_URI = "${CMF_GITHUB_ROOT}/ethernet-agent;protocol=https;${BRANCH_ccsp_eth_agent}"

CFLAGS += " -Wall -Werror -Wextra -Wno-format-overflow -Wno-format-truncation -Wno-array-bounds"

S = "${WORKDIR}/git"

inherit autotools ${@bb.utils.contains_any("DISTRO_FEATURES", "kirkstone wrynose", "python3native", "pythonnative", d)} breakpad-logmapper

PACKAGECONFIG ?= "dropearly"
PACKAGECONFIG[dropearly] = "--enable-dropearly,--disable-dropearly"
CFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'safec',  ' `pkg-config --cflags libsafec`', '-fPIC', d)}"

LDFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'safec', ' `pkg-config --libs libsafec`', '', d)}"
LDFLAGS:remove = "${@bb.utils.contains_any('DISTRO_FEATURES', 'safec', '-lsafec-3.5', '', d)}"
LDFLAGS:append_dunfell = "${@bb.utils.contains_any('DISTRO_FEATURES', 'safec', ' -lsafec-3.5.1 ', '', d)}"
LDFLAGS:append_kirkstone = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'safec', ' -lsafec ', '', d)}"
CFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'safec', '', ' -DSAFEC_DUMMY_API', d)}"
CFLAGS:append  = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'rdkb_wan_manager', ' -DFEATURE_RDKB_WAN_MANAGER', '', d)}"
CFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'ethstats', '-DETH_STATS_ENABLED', '', d)}"
LDFLAGS:append = " -lrt"
LDFLAGS:remove_morty = " -lrt"


CFLAGS:append  = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'core-net-lib', ' -DCORE_NET_LIB', '', d)}"
EXTRA_OECONF:append = " --enable-core_net_lib_feature_support=${@bb.utils.contains_any('DISTRO_FEATURES', 'core-net-lib', 'yes', 'no', d)} "

EXTRA_OECONF:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'WanFailOverSupportEnable', ' --enable-wanfailover ', '', d)}"

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
CFLAGS:append  = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'WanFailOverSupportEnable', ' -I=${includedir}/rbus ', '', d)}"
CFLAGS:append  = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'RbusBuildFlagEnable', ' -I=${includedir}/rbus ', '', d)}"

LDFLAGS:append = " \
    -lccsp_common \
    -ldbus-1 \
    -lutctx \
    -lutapi \
    -lrt \
    -lprivilege \
    "

LDFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'RbusBuildFlagEnable', ' -lrbus ', '', d)}"
LDFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'WanFailOverSupportEnable', ' -lrbus ', '', d)}"
LDFLAGS:append = " ${@bb.utils.contains_any('DISTRO_FEATURES', 'WanFailOverSupportEnable', ' -lsysevent ', '', d)}"

do_compile:prepend () {
	if ${@bb.utils.contains_any('DISTRO_FEATURES', 'rdkb_wan_manager', 'true', 'false', d)}; then
    		sed -i '2i <?define FEATURE_RDKB_WAN_MANAGER=True?>' ${S}/config/TR181-EthAgent.xml
   	fi
        if ${@bb.utils.contains_any('DISTRO_FEATURES', 'rdkb_wan_upstream', 'true', 'false', d)}; then
                sed -i '2i <?define FEATURE_RDKB_WAN_UPSTREAM=True?>' ${S}/config/TR181-EthAgent.xml
        fi
        if ${@bb.utils.contains_any('DISTRO_FEATURES', 'rdkb_auto_port_switch', 'true', 'false', d)}; then
                sed -i '2i <?define FEATURE_RDKB_AUTO_PORT_SWITCH=True?>' ${S}/config/TR181-EthAgent.xml
        fi
    (${PYTHON} ${STAGING_BINDIR_NATIVE}/dm_pack_code_gen.py ${S}/config/TR181-EthAgent.xml ${S}/source/EthSsp/dm_pack_datamodel.c)
}

do_install:append () {
    # Config files and scripts
    install -d ${D}${exec_prefix}/ccsp/ethagent
    install -m 644 ${S}/config/TR181-EthAgent.xml ${D}${exec_prefix}/ccsp/ethagent/TR181-EthAgent.xml
}

FILES:${PN} += " ${exec_prefix}/ccsp/ethagent"

# Breakpad processname and logfile mapping
BREAKPAD_LOGMAPPER_PROCLIST = "CcspEthAgent"
BREAKPAD_LOGMAPPER_LOGLIST = "ETHAGENTLog.txt.0"
