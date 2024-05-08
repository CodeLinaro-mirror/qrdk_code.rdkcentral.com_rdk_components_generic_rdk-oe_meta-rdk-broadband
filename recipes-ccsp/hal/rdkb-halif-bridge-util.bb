SUMMARY = "Bridge Util HAL"
HOMEPAGE = "https://github.com/rdkcentral/rdkb-halif-bridge-util"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

SRC_URI = "git://github.com/rdkcentral/rdkb-halif-bridge-util.git;protocol=https;branch=main"

SRCREV = "abcb9799f1e9152b6ee049e5c077a474189d6955"

S = "${WORKDIR}/git"

CFLAGS_append = " -I=${includedir}/ccsp "

do_install () {
   install -d ${D}/usr/include/ccsp
   install -m 0644 ${S}/include/bridge_util_hal.h ${D}/usr/include/ccsp
}

FILES_${PN} = " \
/usr/include/ccsp \
"