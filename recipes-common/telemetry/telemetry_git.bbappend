DEPENDS += " webconfig-framework libunpriv  mountutils libsyswrapper rdkcertconfig"
DEPENDS += " ${@bb.utils.contains('DISTRO_FEATURES', 'onewifi_json_dml_support', '', 'ccsp-common-library dbus', d)}"

LDFLAGS:append = " -lprivilege"
LDFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'onewifi_json_dml_support', '', '-ldbus-1', d)}"

CFLAGS += " -DENABLE_RDKB_SUPPORT \
            -DFEATURE_SUPPORT_WEBCONFIG \
            -DDROP_ROOT_PRIV \
          "

require recipes-ccsp/ccsp/ccsp_common.inc

CFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'onewifi_json_dml_support', '', '-DCCSP_SUPPORT_ENABLED', d)}"
CFLAGS:remove = " ${@bb.utils.contains('DISTRO_FEATURES', 'onewifi_json_dml_support', '-DCCSP_SUPPORT_ENABLED', '', d)}"

DEPENDS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' safec', " ", d)}"

CFLAGS:append = " -I${STAGING_INCDIR}/ccsp "
CFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec',  ' `pkg-config --cflags libsafec`', '-fPIC', d)}"
CFLAGS:remove:wrynose = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec',  ' `pkg-config --cflags libsafec`', '-fPIC', d)}"
CFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', '', ' -DSAFEC_DUMMY_API', d)}"
LDFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' `pkg-config --libs libsafec`', '', d)}"
LDFLAGS:remove:wrynose = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' `pkg-config --libs libsafec`', '', d)}"

CFLAGS:append:wrynose = " \
    -Wno-discarded-qualifiers \
"
do_compile:prepend () {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'onewifi_json_dml_support', 'false', 'true', d)}; then
        (${PYTHON} ${STAGING_BINDIR_NATIVE}/dm_pack_code_gen.py ${S}/config/TR181-T2-USGv2.XML ${S}/source/t2ssp/dm_pack_datamodel.c)
    fi
}

do_install:append () {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'onewifi_json_dml_support', 'false', 'true', d)}; then
        install -d ${D}/usr/ccsp/telemetry
        install -m 644 ${S}/config/T2Agent.cfg ${D}/usr/ccsp/telemetry
        install -m 644 ${S}/config/CcspDmLib.cfg ${D}/usr/ccsp/telemetry
    fi
}

FILES:${PN}:append = " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'onewifi_json_dml_support', '', '${prefix}/ccsp/telemetry/T2Agent.cfg', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'onewifi_json_dml_support', '', '${prefix}/ccsp/telemetry/CcspDmLib.cfg', d)} \
"

EXTRA_OECONF += " ${@bb.utils.contains('DISTRO_FEATURES', 'onewifi_json_dml_support', '', '--enable-ccspsupport', d)}"
EXTRA_OECONF += " --enable-libsyswrapper"
EXTRA_OECONF:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'gtestapp', '--enable-gtestapp', '', d)}"
EXTRA_OECONF += " --enable-mountutils --enable-rdkcertselector"

EXTRA_OECMAKE += "\
    -Dmsgpack_DIR=${RECIPE_SYSROOT}${libdir}/cmake/msgpack-c \
"
EXTRA_OECMAKE += " \
    -DMSGPACK_LIBRARIES=${RECIPE_SYSROOT}${libdir}/libmsgpack-c.so \
    -DMSGPACK_INCLUDE_DIRS=${RECIPE_SYSROOT}${includedir} \
"
do_configure:prepend() {
    find ${S} -type f \( -name Makefile.am \) -exec sed -i 's/-lmsgpackc/-lmsgpack-c/g' {} \;
}
CPPFLAGS:append = " -I${RECIPE_SYSROOT}/usr/include/safeclib"
LDFLAGS:append:wrynose = " -lsafec"
