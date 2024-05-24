SUMMARY = "Bridge Util HAL"
HOMEPAGE = "https://github.com/rdkcentral/rdkb-halif-bridge-util"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

SRC_URI = "git://github.com/rdkcentral/rdkb-halif-bridge-util.git;protocol=https;branch=main"

SRCREV = "26ca33f50fecfb77d881579bdf251d57e3c957f2"

S = "${WORKDIR}/git"

CFLAGS_append = " -I=${includedir}/ccsp "

do_install () {
   install -d ${D}/usr/include/ccsp
   install -m 0644 ${S}/include/bridge_util_hal.h ${D}/usr/include/ccsp
}

FILES_${PN} = " \
/usr/include/ccsp \
"