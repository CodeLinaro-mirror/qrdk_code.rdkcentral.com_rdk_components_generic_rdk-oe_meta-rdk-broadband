SUMMARY = "This receipe provides lan manager support."
SECTION = "console/utils"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1ccc0aeb842758030ee9446b11ed931b"

DEPENDS = "ccsp-common-library rdk-logger utopia "
DEPENDS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' safec', " ", d)}"

require recipes-ccsp/ccsp/ccsp_common.inc

SRC_URI = "${CMF_GITHUB_ROOT}/lan-manager;protocol=https;${BRANCH_lanmanager}"

S = "${WORKDIR}/git"

inherit autotools systemd

CFLAGS:append += "-DCONFIG_CISCO_HOME_SECURITY"

CFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec',  ' `pkg-config --cflags libsafec`', '-fPIC', d)}"

CFLAGS:append_dunfell = " -Wno-restrict -Wno-format-overflow -Wno-deprecated-declarations -Wno-cast-function-type "
CFLAGS += " -Wall -Werror -Wextra -Wno-pointer-sign -Wno-sign-compare -Wno-deprecated-declarations -Wno-address -Wno-type-limits -Wno-unused-parameter "

LDFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' `pkg-config --libs libsafec`', '', d)}"
LDFLAGS:remove = "${@bb.utils.contains('DISTRO_FEATURES', 'safec', '-lsafec-3.5', '', d)}"
LDFLAGS:append_dunfell = "${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' -lsafec-3.5.1 ', '', d)}"
LDFLAGS:append_kirkstone = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' -lsafec ', '', d)}"
CFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', '', ' -DSAFEC_DUMMY_API', d)}"

CFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'bci', '-DCISCO_CONFIG_TRUE_STATIC_IP -DCISCO_CONFIG_DHCPV6_PREFIX_DELEGATION', '', d)}"
CFLAGS:append += " ${@bb.utils.contains('DISTRO_FEATURES', 'bci', '', '-DFEATURE_SUPPORT_ONBOARD_LOGGING',d)}"
CFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'dslite', '-DDSLITE_FEATURE_SUPPORT', '', d)}"

LDFLAGS += "-pthread -lsecure_wrapper -ltelemetry_msgsender "

CFLAGS:append = " \
    -I=${includedir}/dbus-1.0 \
    -I=${libdir}/dbus-1.0/include \
    -I=${includedir}/ccsp \
    -I=${includedir}/syscfg \
    -I${STAGING_INCDIR}/syscfg \
    -DFEATURE_SUPPORT_RDKLOG \
    "
LDFLAGS:append = " \
    -ldbus-1 \
    -lccsp_common \
    -lsyscfg \
    "
do_install:append () {
    # Config files and scripts
    install -d ${D}${exec_prefix}/ccsp/lanmanager
    install -d ${D}${systemd_unitdir}/system
    install -D -m 0644 ${S}/config/LanManager.service ${D}${systemd_unitdir}/system/LanManager.service
}
SYSTEMD_SERVICE:${PN} = "LanManager.service"
FILES:${PN} += " \
    ${exec_prefix}/ccsp/lanmanager \
    /usr/bin/* \
        ${systemd_unitdir}/system/LanManager.service \
"

