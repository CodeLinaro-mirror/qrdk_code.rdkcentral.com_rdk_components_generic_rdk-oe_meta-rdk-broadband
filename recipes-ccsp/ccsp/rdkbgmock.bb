SUMMARY = "RdkbGMock libs"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

DEPENDS = "gtest"

SRC_URI = "${RDKB_CCSP_ROOT_GIT}/RdkbGMock/generic;protocol=${RDK_GIT_PROTOCOL};branch=${CCSP_GIT_BRANCH};name=RdkbGMock"
SRCREV_RdkbGMock = "${AUTOREV}"
SRCREV_FORMAT = "RdkbGMock"
PV = "${RDK_RELEASE}+git${SRCPV}"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

CFLAGS += " -Wall -Werror -Wextra "

CFLAGS_append_dunfell = " -Wno-restrict -Wno-format-truncation -Wno-format-overflow -Wno-cast-function-type -Wno-unused-function -Wno-implicit-fallthrough "

CFLAGS_append = " -I=${includedir}"

do_install_append () {
	install -d ${D}/usr/include/mocks
	install -m 644 ${S}/source/mocks_fd/*.h ${D}/usr/include/mocks/
	install -m 644 ${S}/source/mocks_file_io/*.h ${D}/usr/include/mocks/
	install -m 644 ${S}/source/mocks_messagebus/*.h ${D}/usr/include/mocks/
	install -m 644 ${S}/source/mocks_psm/*.h ${D}/usr/include/mocks/
	install -m 644 ${S}/source/mocks_socket/*.h ${D}/usr/include/mocks/
	install -m 644 ${S}/source/mocks_syscfg/*.h ${D}/usr/include/mocks/
	install -m 644 ${S}/source/mocks_sysevent/*.h ${D}/usr/include/mocks/
	install -m 644 ${S}/source/mocks_util/*.h ${D}/usr/include/mocks/
}

FILES_${PN} += "${libdir}/*.so"

