SUMMARY = "DHCP HAL"
HOMEPAGE = "https://github.com/rdkcentral/rdkb-halif-dhcp"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

SRC_URI = "git://github.com/rdkcentral/rdkb-halif-dhcp.git;protocol=https;branch=main"
SRCREV = "1b2260d58e140b7e4a84164b20fcfb445d463c24"

S = "${UNPACKDIR}/${PN}-${PV}"

CFLAGS:append = " -I=${includedir}/ccsp "
CFLAGS += "${@bb.utils.contains('DISTRO_FEATURES','no_mta_support','-DNO_MTA_FEATURE_SUPPORT ','',d)}"

do_install () {
   install -d ${D}/usr/include/ccsp
   install -m 0644 ${S}/include/dhcp4cApi.h ${D}/usr/include/ccsp
   install -m 0644 ${S}/include/dhcpv4c_api.h ${D}/usr/include/ccsp
}

FILES:${PN} = " \
/usr/include/ccsp \
"
