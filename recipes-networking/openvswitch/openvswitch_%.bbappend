FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'kirkstone', '', 'file://CVE-2021-3905_fix.patch \
                                                                             file://CVE-2021-36980_fix.patch', d)} \
                 "