FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://nftables.conf"
SRC_URI += "file://nftables-boot.service"
do_install_append() {
    install -d ${D}/etc/nftables
    install -m 0644 ${WORKDIR}/nftables.conf ${D}/etc/nftables/nftables.conf
    install -d ${D}${systemd_system_unitdir}  # Create the missing directory
    install -m 0644 ${WORKDIR}/nftables-boot.service ${D}${systemd_system_unitdir}
}

FILES_${PN} += "/etc/nftables/nftables.conf"
FILES_${PN} += "${systemd_unitdir}/system/nftables-boot.service"

SYSTEMD_SERVICE_${PN} = "nftables-boot.service"
