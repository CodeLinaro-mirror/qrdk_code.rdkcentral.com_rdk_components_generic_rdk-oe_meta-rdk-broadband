FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

DEPENDS += " ${@bb.utils.contains('DISTRO_FEATURES', 'onewifi_json_dml_support', '', 'ccsp-common-library', d)}"

CFLAGS += " ${@bb.utils.contains('DISTRO_FEATURES', 'onewifi_json_dml_support', '', '-DONEWIFI_RDKB_CCSP_SUPPORT', d)}"
CFLAGS += " -DWBCFG_MULTI_COMP_SUPPORT"
CFLAGS:remove = " ${@bb.utils.contains('DISTRO_FEATURES', 'onewifi_json_dml_support', '-DCCSP_SUPPORT_ENABLED', '', d)}"

require recipes-ccsp/ccsp/ccsp_common.inc
SRC_URI:append:wrynose = " file://fix_rollbackFunc_wrynose.patch;apply=no"
do_webconfig_patch () {
    cd ${S}
    if [ ! -e patch_applied ]; then
        if ${@bb.utils.contains('DISTRO_FEATURES', 'wrynose', 'true', 'false', d)}; then
             patch -p1 < ${UNPACKDIR}/fix_rollbackFunc_wrynose.patch
             touch patch_applied
        fi
    fi
}
addtask webconfig_patch after do_unpack before do_configure
do_install:append () {
    install -d ${D}/usr/include/ccsp
    install -m 644 ${S}/include/*.h ${D}/usr/include/ccsp/
}

EXTRA_OECONF += " ${@bb.utils.contains('DISTRO_FEATURES', 'onewifi_json_dml_support', '', '--enable-ccspsupport', d)}"

CFLAGS:append = " \
    -Wno-error=incompatible-pointer-types \
"
