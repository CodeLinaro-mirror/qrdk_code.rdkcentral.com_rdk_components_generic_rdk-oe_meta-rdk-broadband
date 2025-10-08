SUMMARY = "CCSP Hotspot"
HOMEPAGE = "https://github.com/belvedere-yocto/hotspot"

LICENSE = "Apache-2.0 & ISC"
LIC_FILES_CHKSUM = "file://LICENSE;md5=7fd38647ff87fdac48b3fb87e20c1b07"

DEPENDS = "dbus libnetfilter-queue utopia ccsp-lm-lite telemetry libunpriv"
DEPENDS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' safec', " ", d)}"
DEPENDS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'core-net-lib', ' core-net-lib', " ", d)}"

require ccsp_common.inc
CFLAGS += " -Wall -Werror -Wextra -Wno-pointer-sign -Wno-sign-compare "
CFLAGS:append_kirkstone = " -Wno-array-bounds -Wno-stringop-overflow "
CFLAGS:append:scarthgap = " -Wno-array-bounds -Wno-stringop-overflow "
SRC_URI = "${CMF_GIT_ROOT}/rdkb/components/opensource/ccsp/hotspot;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH};name=hotspot"

SRCREV_hotspot = "${AUTOREV}"
SRCREV_FORMAT = "hotspot"
PV = "${RDK_RELEASE}+git${SRCPV}"

S = "${WORKDIR}/git"

inherit autotools pkgconfig ${@bb.utils.contains_any('DISTRO_FEATURES', 'kirkstone scarthgap', 'python3native', 'pythonnative', d)}

CFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec',  ' `pkg-config --cflags libsafec`', '-fPIC', d)}"

LDFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' `pkg-config --libs libsafec`', '', d)}"
LDFLAGS:remove = "${@bb.utils.contains('DISTRO_FEATURES', 'safec', '-lsafec-3.5', '', d)}"
LDFLAGS:append_dunfell = "${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' -lsafec-3.5.1 ', '', d)}"
LDFLAGS:append_kirkstone = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' -lsafec ', '', d)}"
LDFLAGS:append:scarthgap = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' -lsafec ', '', d)}"
CFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', '', ' -DSAFEC_DUMMY_API', d)}"
CFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'OneWifi', '-DRDK_ONEWIFI', '', d)}"
CFLAGS:append  = " ${@bb.utils.contains('DISTRO_FEATURES', 'core-net-lib', ' -DCORE_NET_LIB', '', d)}"
EXTRA_OECONF:append = " --enable-core_net_lib_feature_support=${@bb.utils.contains('DISTRO_FEATURES', 'core-net-lib', 'yes', 'no', d)} "

EXTRA_OECONF:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'WanFailOverSupportEnable', ' --enable-wanfailover ', '', d)}"

CFLAGS:append = " \
   -I${STAGING_INCDIR}/dbus-1.0 \
   -I${STAGING_LIBDIR}/dbus-1.0/include \
   -I${STAGING_INCDIR}/ccsp \
   "

LDFLAGS += "-ldbus-1 -lbreakpadwrapper -lprivilege"

do_compile:prepend(){
    (${PYTHON} ${STAGING_BINDIR_NATIVE}/dm_pack_code_gen.py ${S}/source/hotspotfd/config/hotspot.XML ${S}/source/hotspotfd/dm_pack_datamodel.c)
}

do_install:append () {
    # Config files and scripts
	install -d ${D}/usr/ccsp
	install -d ${D}/usr/ccsp/hotspot
	install -d ${D}${includedir}/ccsp

	install -m 777 ${D}${bindir}/hotspot_arpd -t ${D}/usr/ccsp
	install -m 644 ${S}/source/hotspotfd/include/dhcpsnooper.h ${D}${includedir}/ccsp
	install -m 644 ${S}/source/hotspotfd/include/hotspotfd.h ${D}${includedir}/ccsp
    	install -m 777 ${S}/source/HotspotApi/libHotspotApi.h ${D}${includedir}/ccsp
    	ln -sf ${bindir}/CcspHotspot ${D}${prefix}/ccsp/hotspot/CcspHotspot
}

PACKAGES += "${PN}-ccsp"

FILES:${PN}-ccsp = " \
    /usr/ccsp/hotspot_arpd \
    /usr/ccsp/* \
    "
FILES:${PN} += " \
    ${prefix}/ccsp/hotspot/CcspHotspot \
    ${prefix}/ccsp/hotspot/hotspot.XML  \
    ${libdir}/libHotspotApi.so.* \
	"
FILES:${PN}-dev += "${libdir}/libHotspotApi.so"

FILES:${PN}-dbg = " \
   ${prefix}/ccsp/.debug \
   ${prefix}/ccsp/hotspot/.debug \
   ${prefix}/src/debug \
   ${bindir}/.debug \
   ${libdir}/.debug \
   "
