
SUMMARY = "This recipe compiles rfc-agent"
SECTION = "console/utils"

LICENSE = "CLOSED"
DEPENDS = "utopia rbus trower-base64 msgpack-c webconfig-framework cjson libparodus libsyswrapper hal-platform ccsp-misc hal-cm"
DEPENDS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' safec', " ", d)}"
DEPENDS:class-native = ""

SRC_URI = "${RDK_GENERIC_ROOT_GIT}/rfc-agent/generic;protocol=${RDK_GIT_PROTOCOL};branch=${RDK_GIT_BRANCH};name=rfc-agent"

SRCREV_rfc-agent = "${AUTOREV}"
SRCREV_FORMAT = "rfc-agent"
PV = "${RDK_RELEASE}+git${SRCPV}"
S = "${UNPACKDIR}/${PN}-${PV}"


inherit autotools systemd pkgconfig

CFLAGS += " \
     -I${STAGING_INCDIR} \
     -I${STAGING_INCDIR}/ccsp \
    -I${STAGING_INCDIR}/rbus \
    -I${STAGING_INCDIR}/trower-base64 \
    -I${STAGING_INCDIR}/msgpackc \
    -I${STAGING_INCDIR}/syscfg \
    -I${STAGING_INCDIR}/cjson \
    -I${STAGING_INCDIR}/libparodus \
    "


# generating minidumps symbols
inherit breakpad-wrapper
DEPENDS += "breakpad breakpad-wrapper"
BREAKPAD_BIN:append = " rfc_agent"
CFLAGS += " -Wall -Werror -Wextra -DINCLUDE_BREAKPAD"

#CFLAGS:append_dunfell = " -Wno-restrict -Wno-format-truncation -Wno-format-overflow -Wno-cast-function-type -Wno-unused-function -Wno-implicit-fallthrough "

LDFLAGS += " \
    -lrtMessage \
    -lrbus \
    -lmsgpackc \
    -lsyscfg \
    -ltrower-base64 \
    -lsecure_wrapper \
    -lcjson \
    -lhal_platform \
    -lm \
    -lccsp_common \
"
CFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec',  ' `pkg-config --cflags libsafec`', '-fPIC', d)}"
LDFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' `pkg-config --libs libsafec`', '', d)}"
CFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', '', ' -DSAFEC_DUMMY_API', d)}"
LDFLAGS:remove = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' -lsafec-3.5 ', '', d)}"
LDFLAGS:append_dunfell = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' -lsafec-3.5.1 ', '', d)}"
LDFLAGS:append_kirkstone = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' -lsafec ', '', d)}"
LDFLAGS:append_wrynose = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' -lsafec ', '', d)}"

LDFLAGS += "-lbreakpadwrapper -lpthread -lstdc++"
do_configure:class-native () {
    echo "Configure is skipped"
}

do_install:append () {
     install -d ${D}${systemd_unitdir}/system
    install -D -m 0644 ${S}/config/rfc_agent.service ${D}${systemd_unitdir}/system/rfc_agent.service
}
SYSTEMD_SERVICE:${PN} = "rfc_agent.service"
FILES:${PN} = " \
    ${bindir}/rfc_agent \
    ${prefix}/ccsp/* \
     /usr/bin/* \
    ${systemd_unitdir}/system/rfc_agent.service \
"


