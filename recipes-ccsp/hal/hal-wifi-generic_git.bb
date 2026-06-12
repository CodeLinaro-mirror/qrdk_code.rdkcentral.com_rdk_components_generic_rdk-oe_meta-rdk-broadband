SUMMARY = "HAL for RDK CCSP components"
HOMEPAGE = "http://github.com/belvedere-yocto/hal"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://../../LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

PROVIDES = "hal-wifi"
RPROVIDES:${PN} = "hal-wifi"

DEPENDS += "rdk-wifi-halif safec-common-wrapper"
DEPENDS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' safec', " ", d)}"

SRC_URI = "${CMF_GITHUB_ROOT}/hardware-abstraction-layer;protocol=https;${BRANCH_hardware_abstraction_layer};name=wifihal"
SRCREV_FORMAT = "wifihal"

S = "${UNPACKDIR}/${PN}-${PV}/source/wifi"

# Add flags to support mesh wifi if the feature is available.
CFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'meshwifi', '-DENABLE_FEATURE_MESHWIFI', '', d)}"
CFLAGS:append = " -I=${includedir}/ccsp "
CFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec',  ' `pkg-config --cflags libsafec`', '-fPIC', d)}"
CFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', '', ' -DSAFEC_DUMMY_API', d)}"
LDFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' `pkg-config --libs libsafec`', '', d)}"

CFLAGS:remove:wrynose = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec',  ' `pkg-config --cflags libsafec`', '-fPIC', d)}"
LDFLAGS:remove:wrynose = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' `pkg-config --libs libsafec`', '', d)}"
inherit autotools coverity pkgconfig
