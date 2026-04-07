SUMMARY = "MTA HAL"
HOMEPAGE = "https://github.com/rdkcentral/rdkb-halif-mta"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

SRC_URI = "git://github.com/rdkcentral/rdkb-halif-mta.git;protocol=https;branch=main"
SRCREV = "14cc1fe6491824a6c0ec06f587465a249395f162"

S = "${UNPACKDIR}/${PN}-${PV}"

CFLAGS:append = " -I=${includedir}/ccsp "

do_install () {
   install -d ${D}/usr/include/ccsp
   install -m 0644 ${S}/include/mta_hal.h ${D}/usr/include/ccsp
}

FILES:${PN} = " \
/usr/include/ccsp \
"