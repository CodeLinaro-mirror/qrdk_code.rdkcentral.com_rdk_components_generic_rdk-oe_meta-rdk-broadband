SUMMARY = "CCSP XDNS component"
HOMEPAGE = "http://github.com/belvedere-yocto/CcspXDNS"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

DEPENDS = "ccsp-common-library webconfig-framework dbus rdk-logger utopia trower-base64 glog libunpriv"
DEPENDS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' safec', " ", d)}"

require recipes-ccsp/ccsp/ccsp_common.inc

RDEPENDS:${PN} = " trower-base64 "
DEPENDS += " trower-base64"

SRC_URI = "${CMF_GIT_ROOT}/rdkb/components/opensource/ccsp/CcspXDNS;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH};name=CcspXDNS"

SRCREV_CcspXDNS = "${AUTOREV}"
SRCREV_FORMAT = "CcspXDNS"
PV = "${RDK_RELEASE}+git${SRCPV}"

S = "${WORKDIR}/git"

inherit autotools pkgconfig ${@bb.utils.contains_any('DISTRO_FEATURES', 'kirkstone scarthgap', 'python3native', 'pythonnative', d)} breakpad-logmapper

CFLAGS += " -Wall -Werror -Wextra "

CFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec',  ' `pkg-config --cflags libsafec`', '-fPIC', d)}"

LDFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' `pkg-config --libs libsafec`', '', d)}"
LDFLAGS:remove = "${@bb.utils.contains('DISTRO_FEATURES', 'safec', '-lsafec-3.5', '', d)}"
LDFLAGS:append_dunfell = "${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' -lsafec-3.5.1 ', '', d)}"
LDFLAGS:append_kirkstone = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' -lsafec ', '', d)}"
LDFLAGS:append:scarthgap = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' -lsafec ', '', d)}"
CFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', '', ' -DSAFEC_DUMMY_API', d)}"

DEPENDS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'core-net-lib', ' core-net-lib', " ", d)}"
CFLAGS:append  = " ${@bb.utils.contains('DISTRO_FEATURES', 'core-net-lib', ' -DCORE_NET_LIB', '', d)}"
EXTRA_OECONF:append = " --enable-core_net_lib_feature_support=${@bb.utils.contains('DISTRO_FEATURES', 'core-net-lib', 'yes', 'no', d)} "

CFLAGS:append = " \
    -I${STAGING_INCDIR} \
    -I${STAGING_INCDIR}/dbus-1.0 \
    -I${STAGING_LIBDIR}/dbus-1.0/include \
    -I${STAGING_INCDIR}/ccsp \
    -I${STAGING_INCDIR}/utapi \
    -I${STAGING_INCDIR}/utctx \
    -I${STAGING_INCDIR}/ulog \
    -I${STAGING_INCDIR}/trower-base64 \
    -I${STAGING_INCDIR}/glog \
    "

LDFLAGS:append = " \
    -ldbus-1 \
    -lutctx \
    -lutapi \
    -lglog \
    -lprivilege \
    "

do_compile:prepend () {
    (${PYTHON} ${STAGING_BINDIR_NATIVE}/dm_pack_code_gen.py ${S}/config/CcspXdns_dm.xml ${S}/source/XdnsSsp/dm_pack_datamodel.c)
}

do_install:append () {
    # Config files and scripts
    install -d ${D}/usr/ccsp/xdns
}

PACKAGES += "${PN}-ccsp"

FILES:${PN} += " \
    ${prefix}/ccsp/xdns \
    ${libdir}/libdmlxdns.so.* \
    ${bindir}/* \
"

FILES:${PN}-dbg += " \
    ${prefix}/ccsp/xdns/.debug \
    ${prefix}/src/debug \
    ${bindir}/.debug \
    ${libdir}/.debug \
"

# Breakpad processname and logfile mapping
BREAKPAD_LOGMAPPER_PROCLIST = "CcspXdnsSsp"
BREAKPAD_LOGMAPPER_LOGLIST = "XDNSlog.txt.0"
