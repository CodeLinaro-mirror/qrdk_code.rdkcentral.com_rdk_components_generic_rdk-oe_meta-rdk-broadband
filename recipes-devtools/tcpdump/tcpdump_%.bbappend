inherit ${@bb.utils.contains('GENERATE_RDM_CERTS', '${BPN}', 'comcast-package-deploy', '', d)}

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
SRC_URI += "file://package.json \
            file://tcpdump-post-install.sh"

DOWNLOAD_APPS = "${@bb.utils.contains('DISTRO_FEATURES', 'rdm', '${BPN}', '', d)}"
CUSTOM_PKG_EXTNS = "dl"
SKIP_MAIN_PKG = "yes"
ALLOW_EMPTY:${PN} = "1"
ENABLE_RDM_VERSIONING="${@bb.utils.contains('DISTRO_FEATURES', 'rdm rdm-versioning', 'true', 'false', d)}"
PKG_FIRMWARE_DECOUPLED="true"

PKG_BUNDLE_NAME="${MACHINE_IMAGE_NAME}-tcpdump"
PKG_BUNDLE_MAJOR_VERSION="1"
PKG_BUNDLE_MINOR_VERSION="0"

DOWNLOADABLE_FILES = "${@bb.utils.contains('DOWNLOAD_APPS', '${PKG_BUNDLE_NAME}', '\
                             ${bindir}/tcpdump \
                             ${sysconfdir}/rdm/post-services/tcpdump-post-install.sh \
                             ', '', d)}"
DOWNLOADABLE_FILES += "${@bb.utils.contains('ENABLE_RDM_VERSIONING', 'true', '\
                              ${sysconfdir}/apps/${PKG_BUNDLE_NAME}_package.json \
                              ', '', d)}"

PACKAGE_BEFORE_PN += "${PN}-dl "
ALLOW_EMPTY:${PN}-dl = "1"
RDEPENDS:${PN} += " ${PN}-dl"

do_install:append() {
    if [ "${ENABLE_RDM_VERSIONING}" = "true" ]; then
        install -d ${D}${sysconfdir}/apps
        install -m 644 ${WORKDIR}/package.json ${D}${sysconfdir}/apps/${PKG_BUNDLE_NAME}_package.json
        install -d ${D}${sysconfdir}/rdm/post-services
        install -m 0755 ${WORKDIR}/tcpdump-post-install.sh ${D}${sysconfdir}/rdm/post-services/tcpdump-post-install.sh
    fi
}
FILES:${PN}-dl += "${sysconfdir}/apps/${PKG_BUNDLE_NAME}_package.json \
                   ${sysconfdir}/rdm/post-services/tcpdump-post-install.sh \
                   ${bindir}/tcpdump \
                   "

pkg_postinst:${PN}-dl () {
    if [ -n "$D" -a -d "$D" ]; then
        echo "Removing tcpdump binary from rootfs"
        rm -f $D${bindir}/tcpdump
    fi
}
