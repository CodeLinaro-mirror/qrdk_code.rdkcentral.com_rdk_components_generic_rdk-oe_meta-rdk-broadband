FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

RDEPENDS_${PN} += "bash"

DEPENDS += "telemetry"

SRC_URI += "file://chrony.conf \
            file://chronyd.service \
            file://rdk_chrony.conf \
            file://build_chrony_config.sh \
           "
PACKAGECONFIG_remove = "editline"


do_install_append() {
    # Binaries
    install -m 0755 ${S}/chronyc ${D}${sbindir}
    install -d ${D}${base_libdir}/rdk

    #config File
    rm -rf ${D}${sysconfdir}/chrony.conf 
    install -m 0644 ${WORKDIR}/chrony.conf ${D}${sysconfdir}/
    install -m 0644 ${WORKDIR}/rdk_chrony.conf ${D}${sysconfdir}/
    install -m 0755 ${WORKDIR}/build_chrony_config.sh ${D}${base_libdir}/rdk  

    # service to start chrony
    rm -rf ${D}${systemd_unitdir}/system/chronyd.service
    install -m 0644 ${WORKDIR}/chronyd.service ${D}${systemd_unitdir}/system/


}


FILES_${PN} += "${sbindir}/chronyc"
CONFFILES_${PN} += "${sysconfdir}/chrony.conf"
CONFFILES_${PN} += "${sysconfdir}/rdk_chrony.conf"
FILES_${PN} += "${systemd_unitdir}/system/chronyd.service"
FILES_${PN} += "${base_libdir}/rdk/build_chrony_config.sh"



RCONFLICTS_${PN} = "ntimed"
