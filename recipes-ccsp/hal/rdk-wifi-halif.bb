SUMMARY = "HAL for RDK CCSP components"
HOMEPAGE = "http://github.com/belvedere-yocto/hal"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

SRC_URI = "git://github.com/rdkcentral/rdkb-halif-wifi.git;protocol=https;branch=main"

SRCREV = "bff7e20b56c435f266a6a1ffd2eddf4de8528e68"

S = "${WORKDIR}/git"

CFLAGS_append = " -I=${includedir}/ccsp "

do_install() {
    install -d ${D}${includedir}/ccsp
    install -m 0644 ${S}/include/*.h ${D}${includedir}/ccsp/
}

FILES_${PN} = "${includedir}/ccsp"
