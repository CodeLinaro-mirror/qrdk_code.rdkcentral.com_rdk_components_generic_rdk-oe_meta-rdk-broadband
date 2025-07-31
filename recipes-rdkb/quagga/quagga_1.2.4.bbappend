FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://rtadv.patch"
SRC_URI += "file://0001-RDKB-20441-zebra-service-fails-to-start.patch"
SRC_URI += "file://quagga-Avoid-duplicate-connected-address.patch"

SYSTEMD_PACKAGES:remove = "${PN} ${PN}-bgpd ${PN}-isisd ${PN}-ospf6d ${PN}-ospfd ${PN}-ripd ${PN}-ripngd"
SYSTEMD_SERVICE_${PN}-bgpd:remove = "bgpd.service"
SYSTEMD_SERVICE_${PN}-isisd:remove = "isisd.service"
SYSTEMD_SERVICE_${PN}-ospf6d:remove = "ospf6d.service"
SYSTEMD_SERVICE_${PN}-ospfd:remove = "ospfd.service"
SYSTEMD_SERVICE_${PN}-ripd:remove = "ripd.service"
SYSTEMD_SERVICE_${PN}-ripngd:remove = "ripngd.service"
SYSTEMD_SERVICE_${PN}:remove = "zebra.service"

RDEPENDS_${PN}:remove = "${PN}-bgpd ${PN}-isisd ${PN}-ospf6d ${PN}-ospfd ${PN}-ripd ${PN}-ripngd"

do_install:append() {
    rm -rf ${D}${base_libdir}
    rm -r ${D}${sysconfdir}/quagga/*.conf.sample
}

