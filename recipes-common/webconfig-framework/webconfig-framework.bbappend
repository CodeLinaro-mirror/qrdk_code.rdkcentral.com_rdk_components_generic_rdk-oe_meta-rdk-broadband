DEPENDS += " ${@bb.utils.contains('DISTRO_FEATURES', 'onewifi_json_dml_support', '', 'ccsp-common-library', d)}"

CFLAGS += " ${@bb.utils.contains('DISTRO_FEATURES', 'onewifi_json_dml_support', '', '-DONEWIFI_RDKB_CCSP_SUPPORT', d)}"
CFLAGS += " -DWBCFG_MULTI_COMP_SUPPORT"
CFLAGS:remove = " ${@bb.utils.contains('DISTRO_FEATURES', 'onewifi_json_dml_support', '-DCCSP_SUPPORT_ENABLED', '', d)}"

require recipes-ccsp/ccsp/ccsp_common.inc

do_install:append () {
    install -d ${D}/usr/include/ccsp
    install -m 644 ${S}/include/*.h ${D}/usr/include/ccsp/
}

EXTRA_OECONF += " ${@bb.utils.contains('DISTRO_FEATURES', 'onewifi_json_dml_support', '', '--enable-ccspsupport', d)}"
