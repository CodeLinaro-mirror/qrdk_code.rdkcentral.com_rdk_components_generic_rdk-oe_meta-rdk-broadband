SUMMARY = "Platform HAL"
HOMEPAGE = "https://github.com/rdkcentral/rdkb-halif-platform"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

SRC_URI = "git://github.com/rdkcentral/rdkb-halif-platform.git;protocol=https;branch=main"
SRCREV = "c99e7071a83a695cbdb9ed7b70c0e340a4eab15f"

S = "${UNPACKDIR}/${PN}-${PV}"

CFLAGS:append = " -I=${includedir}/ccsp "

do_install () {
   install -d ${D}/usr/include/ccsp
   install -m 0644 ${S}/include/platform_hal.h ${D}/usr/include/ccsp
}

FILES:${PN} = " \
/usr/include/ccsp \
"
