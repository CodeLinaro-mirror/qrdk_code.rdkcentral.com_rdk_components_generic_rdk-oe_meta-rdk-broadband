EXTRA_OECONF_append = " --enable-ccsp-common"
EXTRA_OECONF_append = " --enable-dml"
EXTRA_OECONF_append = " --enable-journalctl"

CFLAGS_append = " -DONEWIFI_OVSDB_TABLE_SUPPORT "

EXTRA_OECONF_append = "${@bb.utils.contains('DISTRO_FEATURES', 'sm_app', ' --enable-sm-app', '', d)}"

do_install_append() {
    install -m 644 ${S}/source/platform/rdkb/bus.h ${D}/usr/include/ccsp
    install -m 644 ${S}/source/platform/common/bus_common.h ${D}/usr/include/ccsp
}
