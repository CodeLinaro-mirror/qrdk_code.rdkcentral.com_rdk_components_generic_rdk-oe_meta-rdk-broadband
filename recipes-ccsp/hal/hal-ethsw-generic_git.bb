SUMMARY = "HAL for RDK CCSP components"
HOMEPAGE = "http://github.com/belvedere-yocto/hal"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://../../LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

PROVIDES = "hal-ethsw"
RPROVIDES:${PN} = "hal-ethsw"

DEPENDS += "rdkb-halif-ethsw"

SRC_URI = "${CMF_GITHUB_ROOT}/hardware-abstraction-layer;protocol=https;${BRANCH_hardware_abstraction_layer};name=ethswhal"
SRCREV_FORMAT = "ethswhal"

S = "${WORKDIR}/git/source/ethsw"

CFLAGS:append = " -I=${includedir}/ccsp "

inherit autotools coverity
