SUMMARY = "This receipe provides utility to start parodus."
SECTION = "console/utils"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"
DEPENDS = "cjson utopia breakpad breakpad-wrapper"
DEPENDS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' safec', " ", d)}"
require recipes-ccsp/ccsp/ccsp_common.inc

# generating minidumps symbols
inherit breakpad-wrapper
BREAKPAD_BIN:append = " parodusStart"

CFLAGS += " -Wall -Werror -Wextra "

SRC_URI = "${CMF_GITHUB_ROOT}/start-parodus;protocol=https;${BRANCH_start_parodus}"

S = "${UNPACKDIR}/${PN}-${PV}"

EXTRA_OECONF:append  = " --with-ccsp-arch=arm "

inherit autotools pkgconfig
DEPENDS:append = " hal-platform hal-cm openssl cpgc lxy "
RDEPENDS:${PN} += " cjson hal-platform hal-cm utopia "

LDFLAGS:append = " -lbreakpadwrapper -lhal_platform -lcm_mgnt -lsyscfg -lcjson -lsysevent -lutapi -lutctx -lm "

CFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec',  ' `pkg-config --cflags libsafec`', '-fPIC', d)}"

LDFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' `pkg-config --libs libsafec`', '', d)}"
CFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', '', ' -DSAFEC_DUMMY_API', d)}"

CFLAGS:append = " -I${STAGING_INCDIR} -I${STAGING_INCDIR}/ccsp -I${STAGING_INCDIR}/syscfg -I${STAGING_INCDIR}/cjson -DFEATURE_DNS_QUERY"
CFLAGS:append = " ${@bb.utils.contains("DISTRO_FEATURES", "seshat", " -DENABLE_SESHAT ", " ", d)} "
CFLAGS:append = " ${@bb.utils.contains("DISTRO_FEATURES", "WanFailOverSupportEnable", " -DWAN_FAILOVER_SUPPORTED ", " ", d)} "
CFLAGS:append = "${@bb.utils.contains("DISTRO_FEATURES", "webconfig_bin", "-DENABLE_WEBCFGBIN ", " ", d)}"


FILES:${PN} += "/usr/bin/* "
