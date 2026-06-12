
inherit comcast-package-deploy

DOWNLOAD_APPS= "${@bb.utils.contains('DISTRO_FEATURES', 'rdm', bb.utils.contains('DISTRO_FEATURES', 'enable_xmeminsight', '', '${BPN}', d), '', d)}"
CUSTOM_PKG_EXTNS = "dl"
SKIP_MAIN_PKG = "yes"
DOWNLOAD_ON_DEMAND = "yes"
DOWNLOAD_METHOD_CONTROLLER = "RFC"

PACKAGE_BEFORE_PN += "${PN}-dl "
RDEPENDS:${PN} += " ${PN}-dl"

FILES:${PN}-dl += "${bindir}/meminsight \
                  /etc/rdm/post-services/start_meminsight.sh \
                  "

pkg_postinst:${PN}-dl () {
    if ${@bb.utils.contains('DOWNLOAD_APPS', 'meminsight', 'true', 'false', d)}; then
        if [ -n "$D" -a -d "$D" ]; then
            echo "Removing meminsight binary & meminsight start script from rootfs"
            rm -f $D/usr/bin/meminsight
            rm -f $D/etc/rdm/post-services/start_meminsight.sh
        fi
    fi
}
