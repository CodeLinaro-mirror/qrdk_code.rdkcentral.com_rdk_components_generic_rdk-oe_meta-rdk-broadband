SUMMARY = "JSON HAL Client and Server library"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=279c0d21cb7bc051383cef7cd415c938"

DEPENDS += " json-c json-schema-validator"

SRC_URI = "${CMF_GITHUB_ROOT}/json-hal-library;protocol=https;${BRANCH_json_hal_lib}"

S = "${UNPACKDIR}/${PN}-${PV}"

inherit cmake pkgconfig

FILES:${PN} += "${libdir}/* \
                ${bindir}/* "

FILES_SOLIBSDEV = ""
INSANE_SKIP:${PN} += "dev-so"
