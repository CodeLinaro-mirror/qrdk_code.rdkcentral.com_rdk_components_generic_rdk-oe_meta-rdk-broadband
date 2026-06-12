SUMMARY = "CCSP CcspCrSsp component"
HOMEPAGE = "http://github.com/belvedere-yocto/CcspCr"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

DEPENDS = "ccsp-common-library dbus telemetry utopia libunpriv rbus libxml2"
DEPENDS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'systemd', '', d)}"

require ccsp_common.inc

CFLAGS += " -Wall -Werror -Wextra -Wno-enum-conversion"

SRC_URI = "${CMF_GITHUB_ROOT}/component-registry;protocol=https;${BRANCH_ccsp_cr}"

S = "${WORKDIR}/git"

inherit autotools breakpad-logmapper

EXTRA_OECONF:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', '--enable-notify', '', d)}"

SRC_URI:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'rdkb_wan_manager', 'file://Add_WanManager_Ready_Event.patch', '', d)}"

CFLAGS:append = " \
    -I=${includedir}/dbus-1.0 \
    -I=${libdir}/dbus-1.0/include \
    -I=${includedir}/ccsp \
    -I${STAGING_INCDIR}/syscfg \
    -I${STAGING_INCDIR}/utapi \
    -I${STAGING_INCDIR}/utctx \
    -I${STAGING_INCDIR}/ulog \
    -I${STAGING_INCDIR}/rbus \
    -I${STAGING_INCDIR}/rtmessage \
    -I${STAGING_INCDIR}/libxml2 \
    "

LDFLAGS += "-ldbus-1 -ltelemetry_msgsender -lprivilege -lutapi -lutctx -lsyscfg -lcjson -lmsgpackc"

do_install:append () {
    # Config files and scripts
    install -d ${D}/usr/ccsp
    ln -sf /usr/bin/CcspCrSsp ${D}/usr/ccsp/CcspCrSsp
    install -m 644 ${S}/source/cr-ethwan-deviceprofile.xml ${D}/usr/ccsp/cr-ethwan-deviceprofile.xml
    install -m 644 ${S}/config/cr-deviceprofile_embedded.xml ${D}/usr/ccsp/cr-deviceprofile.xml
    #Remove mta component registration from config file.
    if ${@bb.utils.contains('DISTRO_FEATURES', 'no_mta_support', 'true', 'false', d)}; then
            sed -i -e "/mta/{n;d}" ${D}/usr/ccsp/cr-deviceprofile.xml
            sed -i -e 'N;/mta/!P;D' ${D}/usr/ccsp/cr-deviceprofile.xml
            sed -i -e "/mta/{n;d}" ${D}/usr/ccsp/cr-ethwan-deviceprofile.xml
            sed -i -e 'N;/mta/!P;D' ${D}/usr/ccsp/cr-ethwan-deviceprofile.xml
    fi
}


PACKAGES += "${PN}-ccsp"

FILES:${PN}-ccsp = " \
    ${prefix}/ccsp/CcspCrSsp \
    ${prefix}/ccsp/cr-deviceprofile.xml \
    ${prefix}/ccsp/cr-ethwan-deviceprofile.xml \
"

FILES:${PN}-dbg = " \
    ${prefix}/ccsp/.debug \
    ${prefix}/src/debug \
    ${bindir}/.debug \
    ${libdir}/.debug \
"

# Breakpad processname and logfile mapping
BREAKPAD_LOGMAPPER_PROCLIST = "CcspCrSsp"
BREAKPAD_LOGMAPPER_LOGLIST = "CRlog.txt.0"
