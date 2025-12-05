FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI += "file://jsonquery.c"

EXTRA_OECONF_append = " --enable-openssl=yes --enable-rdkb=yes"


CFLAGS += "-I${STAGING_INCDIR}/cjson"
CFLAGS_morty = " -I${PKG_CONFIG_SYSROOT_DIR}${includedir}/cjson"

LDFLAGS += "-lcjson"

DEPENDS += "cjson"

do_compile_append () {
    ${CC} -Wall -Wextra ${CFLAGS} ${WORKDIR}/jsonquery.c -o ${WORKDIR}/jsonquery ${LDFLAGS}
}

do_install_append () {
        install -d ${D}${bindir}
        install -m 0755 ${WORKDIR}/jsonquery ${D}${bindir}/
}
