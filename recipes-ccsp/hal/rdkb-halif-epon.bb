SUMMARY = "EPON HAL"
HOMEPAGE = "https://github.com/rdkcentral/rdkb-halif-epon"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

GIT_TAG = "v1.0.0"
SRC_URI := "git://github.com/rdkcentral/rdkb-halif-epon.git;protocol=https;branch=main;tag=${GIT_TAG}"
PV = "${GIT_TAG}+git${SRCPV}"

S = "${WORKDIR}/git"

CFLAGS_append = " -I=${includedir}/epon "

do_install () {
   install -d ${D}/usr/include/epon
   install -m 0644 ${S}/epon_hal.h ${D}/usr/include/epon
}

FILES_${PN} = " \
/usr/include/epon \
"
