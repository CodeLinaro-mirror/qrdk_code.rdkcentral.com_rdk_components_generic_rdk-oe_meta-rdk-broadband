SUMMARY = "HAL for RDK CCSP components"
HOMEPAGE = "http://github.com/belvedere-yocto/hal"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://../../LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

PROVIDES = "hal-mso_mgmt"
RPROVIDES:${PN} = "hal-mso_mgmt"

DEPENDS += "rdkb-halif-mso"

SRC_URI = "${CMF_GITHUB_ROOT}/hardware-abstraction-layer;protocol=https;${BRANCH_hardware_abstraction_layer};name=msomgmthal"
SRCREV_FORMAT = "msomgmthal"

S = "${WORKDIR}/git/source/mso_mgmt"

CFLAGS:append = " -I=${includedir}/ccsp "

inherit autotools coverity
