FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:${THISDIR}/ccsp-webui:${THISDIR}/ccsp-webui-bci:"

do_install_append() {
    install -d ${D}/usr/bgw/actionHandler
    install -m 0755 ${WORKDIR}/ajax_maintenance_window_conf.jst ${D}/usr/bgw/actionHandler/
}

FILES_${PN} += "/usr/bgw/actionHandler"
