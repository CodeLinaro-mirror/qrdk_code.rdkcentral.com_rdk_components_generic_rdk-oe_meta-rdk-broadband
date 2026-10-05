FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

RDEPENDS:${PN} += "bash"

DEPENDS += "telemetry"

SRC_URI += "file://chrony.conf \
            file://chronyd.service \
            file://rdk_chrony.conf \
            file://build_chrony_config.sh \
           "
PACKAGECONFIG:remove = "editline"


do_install:append() {
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


FILES:${PN} += "${sbindir}/chronyc"
CONFFILES:${PN} += "${sysconfdir}/chrony.conf"
CONFFILES:${PN} += "${sysconfdir}/rdk_chrony.conf"
FILES:${PN} += "${systemd_unitdir}/system/chronyd.service"
FILES:${PN} += "${base_libdir}/rdk/build_chrony_config.sh"



RCONFLICTS:${PN} = "ntimed"
