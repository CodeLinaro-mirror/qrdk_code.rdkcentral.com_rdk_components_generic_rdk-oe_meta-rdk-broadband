SUMMARY = "CCSP GWProvAPP ETHWAN"
HOMEPAGE = "https://github.com/belvedere-yocto/GwProvApp"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

DEPENDS = "ccsp-common-library hal-cm hal-dhcpv4c hal-ethsw hal-moca hal-mso_mgmt hal-mta hal-platform hal-vlan hal-wifi ruli utopia"
DEPENDS:append = " hal-gwprovappabs"
DEPENDS:remove_morty = " hal-gwprovappabs"

require ccsp_common.inc

SRC_URI = "${CMF_GIT_ROOT}/rdkb/components/opensource/ccsp/GwProvApp-EthWan;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH};name=GwProvApp-EthWan"

SRCREV_GwProvApp-EthWan = "${AUTOREV}"
SRCREV_FORMAT = "GwProvApp-EthWan"
PV = "${RDK_RELEASE}+git${SRCPV}"

S = "${WORKDIR}/git"

CFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'bci', '-DCISCO_CONFIG_TRUE_STATIC_IP -DCISCO_CONFIG_DHCPV6_PREFIX_DELEGATION', '', d)}"

CFLAGS += " -Wall -Werror -Wextra -Wno-pointer-sign -Wno-pointer-to-int-cast "

LDFLAGS:append = " -lgwprovappabs"
LDFLAGS:remove_morty = " -lgwprovappabs"

inherit autotools

do_install:append () {
    # Config files and scripts
    install -d ${D}/${STAGING_INCDIR}
    install -d ${D}/usr/ccsp/
    install -m 0755 ${B}/source/gw_prov_ethwan ${D}/usr/ccsp/gw_prov_ethwan
#    install -d ${D}${systemd_unitdir}/system
#    install -m 0644 ${S}/service/gwprovethwan.service ${D}${systemd_unitdir}/system
}

#SYSTEMD_SERVICE:${PN} = "gwprovethwan.service"

FILES:${PN} += " \
    ${STAGING_INCDIR} \
    /usr/ccsp \
    /usr/ccsp/gw_prov_ethwan \
"

#FILES_${PN} += " \
#     ${systemd_unitdir}/system/gwprovethwan.service \
#"

FILES:${PN}-dbg = " \
    ${prefix}/ccsp/.debug \
    ${prefix}/src/debug \
    ${bindir}/.debug \
    ${libdir}/.debug \
"
