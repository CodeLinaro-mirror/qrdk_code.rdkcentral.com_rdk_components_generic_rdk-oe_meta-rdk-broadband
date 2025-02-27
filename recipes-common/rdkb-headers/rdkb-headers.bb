SUMMARY = "RDK-B Global Headers"

LICENSE = "CLOSED"

SRC_URI += "file://rdkb_global.h"

SRCREV_rdkb-headers = "${AUTOREV}"
PV = "${RDK_RELEASE}"

S = "${WORKDIR}/git"

# this is a header package only, nothing to build
do_compile[noexec] = "1"
do_configure[noexec] = "1"

# also get rid of the default dependency added in bitbake.conf
# since there is no 'main' package generated (empty)
RDEPENDS_${PN}-dev = ""

do_install() {
    install -D -m 0644 ${S}/../rdkb_global.h ${D}${includedir}/rdkb_global.h
}


