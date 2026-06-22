SUMMARY = "RDK Firmware Upgrade Manager component"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

DEPENDS = "ccsp-common-library ccsp-misc hal-cm dbus rdk-logger utopia halinterface hal-fwupgrade libunpriv"
require ccsp_common.inc

SRC_URI = "${CMF_GITHUB_ROOT}/platform-manager;protocol=https;${BRANCH_rdk_fwupgrade_manager}"

S = "${WORKDIR}/git"

EXTRA_OECONF:append  = " ${@bb.utils.contains_any('DISTRO_FEATURES','kirkstone wrynose','','--with-ccsp-platform=bcm --with-ccsp-arch=arm',d)} "

inherit autotools pkgconfig

do_compile:prepend () {
    (${PYTHON} ${STAGING_BINDIR_NATIVE}/dm_pack_code_gen.py ${S}/config/RdkFwUpgradeManager.xml ${S}/source/FwUpgradeManager/dm_pack_datamodel.c)
}

CFLAGS:append = " \
    -I${STAGING_INCDIR} \
    -I${STAGING_INCDIR}/dbus-1.0 \
    -I${STAGING_LIBDIR}/dbus-1.0/include \
    -I${STAGING_INCDIR}/ccsp \
    -I ${STAGING_INCDIR}/syscfg \
    -I ${STAGING_INCDIR}/sysevent \
    "

LDFLAGS += " -lprivilege"

CFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' -fPIC -I${STAGING_INCDIR}/libsafec', '-fPIC', d)}"
CFLAGS:append  = " ${@bb.utils.contains('DISTRO_FEATURES', 'rdkb_wan_manager', '-DFEATURE_RDKB_WAN_MANAGER', '', d)}"

LDFLAGS:append = " -ldbus-1"
LDFLAGS:remove_morty = " -ldbus-1"

do_configure[depends] += "ccsp-common-library:do_install"

do_install:append () {
    # Config files and scripts
    install -d ${D}${exec_prefix}/rdk/fwupgrademanager
    ln -sf ${bindir}/fwupgrademanager ${D}${exec_prefix}/rdk/fwupgrademanager/fwupgrademanager
    install -m 644 ${S}/config/RdkFwUpgradeManager.xml ${D}/usr/rdk/fwupgrademanager/
}


FILES:${PN} = " \
   ${exec_prefix}/rdk/fwupgrademanager/fwupgrademanager \
   ${exec_prefix}/rdk/fwupgrademanager/RdkFwUpgradeManager.xml \
   ${bindir}/* \
"

FILES:${PN}-dbg = " \
    ${exec_prefix}/ccsp/fwupgrademanager/.debug \
    /usr/src/debug \
    ${bindir}/.debug \
    ${libdir}/.debug \
"
