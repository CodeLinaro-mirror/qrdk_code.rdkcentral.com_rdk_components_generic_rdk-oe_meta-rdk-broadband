SUMMARY = "HAL for RDK CCSP components"
HOMEPAGE = "http://github.com/belvedere-yocto/hal"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

SRC_URI = "git://github.com/rdkcentral/rdkb-halif-wifi.git;protocol=https;branch=main"

SRCREV = "f7df4587ae95cf97cfe243e8b984005e34cb5f2b"

S = "${WORKDIR}/git"

CFLAGS_append = " -I=${includedir}/ccsp "

do_install() {
    install -d ${D}${includedir}/ccsp
    install -m 0644 ${S}/include/*.h ${D}${includedir}/ccsp/
}

FILES_${PN} = "${includedir}/ccsp"
