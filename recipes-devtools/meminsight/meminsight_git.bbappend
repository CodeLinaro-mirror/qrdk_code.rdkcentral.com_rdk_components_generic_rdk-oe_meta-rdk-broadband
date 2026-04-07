# Generate RDM package only when 'meminsight' is passed to GENERATE_RDM_CERTS
# GENERATE_RDM_CERTS can be passed through jenkins or can be set locally in auto.conf
inherit ${@bb.utils.contains('GENERATE_RDM_CERTS', '${BPN}', 'comcast-package-deploy', '', d)}

DOWNLOAD_APPS= "${@bb.utils.contains('DISTRO_FEATURES', 'rdm', bb.utils.contains('DISTRO_FEATURES', 'enable_xmeminsight', '', '${BPN}', d), '', d)}"
CUSTOM_PKG_EXTNS = "dl"
SKIP_MAIN_PKG = "yes"
DOWNLOAD_ON_DEMAND = "yes"
DOWNLOAD_METHOD_CONTROLLER = "RFC"
ENABLE_RDM_VERSIONING="${@bb.utils.contains('DISTRO_FEATURES', 'rdm rdm-versioning', 'true', 'false', d)}"
PKG_FIRMWARE_DECOUPLED="true"

PKG_BUNDLE_NAME="${MACHINE_IMAGE_NAME}-meminsight"
PKG_BUNDLE_MAJOR_VERSION="1"
PKG_BUNDLE_MINOR_VERSION="0"

DOWNLOADABLE_FILES = "${@bb.utils.contains('DOWNLOAD_APPS', '${PKG_BUNDLE_NAME}', '\
                             ${bindir}/meminsight \
                             /etc/rdm/post-services/start_meminsight.sh \
                             ', '', d)}"
DOWNLOADABLE_FILES += "${@bb.utils.contains('ENABLE_RDM_VERSIONING', 'true', '\
                             ${sysconfdir}/apps/${PKG_BUNDLE_NAME}_package.json \
                             ', '', d)}"

PACKAGE_BEFORE_PN += "${PN}-dl "
RDEPENDS:${PN} += " ${PN}-dl"

do_install:append() {
    if [ "${ENABLE_RDM_VERSIONING}" = "true" ]; then
        install -d ${D}${sysconfdir}/apps
        install -m 644 ${WORKDIR}/package.json ${D}${sysconfdir}/apps/${PKG_BUNDLE_NAME}_package.json
    fi
}

FILES:${PN}-dl += "${sysconfdir}/apps/${PKG_BUNDLE_NAME}_package.json \
                   ${bindir}/meminsight \
                  /etc/rdm/post-services/start_meminsight.sh \
                  "

pkg_postinst:${PN}-dl () {
        if [ -n "$D" -a -d "$D" ]; then
            echo "Removing meminsight binary & meminsight start script from rootfs"
            rm -f $D/usr/bin/meminsight
            rm -f $D/etc/rdm/post-services/start_meminsight.sh
        fi
}
