SUMMARY = "HAL for RDK CCSP components"
HOMEPAGE = "http://github.com/belvedere-yocto/hal"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://../../LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

PROVIDES = "hal-bridgeutil"
RPROVIDES:${PN} = "hal-bridgeutil"

DEPENDS += "rdkb-halif-bridge-util"

require recipes-ccsp/ccsp/ccsp_common.inc

SRC_URI = "${CMF_GITHUB_ROOT}/hardware-abstraction-layer;protocol=https;${BRANCH_hardware_abstraction_layer};name=bridgeutilhal"
SRCREV_FORMAT = "bridgeutilhal"

S = "${UNPACKDIR}/git/source/bridgeutil"

CFLAGS:append = " -I=${includedir}/ccsp "

CFLAGS:append += " ${@bb.utils.contains('DISTRO_FEATURES', 'rdkb_extender', '-DRDKB_EXTENDER_ENABLED', '', d)}"
DEPENDS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'core-net-lib', ' core-net-lib', " ", d)}"
CFLAGS:append  = " ${@bb.utils.contains('DISTRO_FEATURES', 'core-net-lib', ' -DCORE_NET_LIB', '', d)}"

inherit autotools coverity
