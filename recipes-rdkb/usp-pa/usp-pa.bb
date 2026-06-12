#
# Yocto recipe to install obuspa open source project
#

SUMMARY = "USP Pa component"
DESCRIPTION = "Agent for USP protocol"
DEPENDS = "obuspa ccsp-common-library rbus"
RDEPENDS:${PN} += "obuspa"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${WORKDIR}/usp-pa-vendor-rdk/LICENSE;md5=778849279f710b843cfcef75fe59376b"

require recipes-ccsp/ccsp/ccsp_common.inc

# USPPA is the RDK specializations
SRC_URI += "git://github.com/rdkcentral/usp-pa-vendor-rdk;protocol=http;branch=main;name=usppa;destsuffix=usp-pa-vendor-rdk"

SRCREV = "6a7dec999577695543c8a81c762d44db56dbdce3"
PV = "1.0+git${SRCPV}"

S = "${WORKDIR}/usp-pa-vendor-rdk/src/vendor"

# Configuration files for target
SRC_URI += "file://conf/usp_factory_reset.conf"
SRC_URI += "file://conf/usp_dm_objs.conf"
SRC_URI += "file://conf/usp_dm_params.conf"
SRC_URI += "file://conf/usp_truststore.pem"
SRC_URI += "file://usp-pa.service"
SRC_URI += "file://UspPa.sh"

# Specify the rules to use to build and install this package
inherit autotools pkgconfig systemd

EXTRA_OECONF = " \
    --with-sysroot=${STAGING_DIR_TARGET} \
    "

# Copy files to staging area
do_install:append() {
    install -d ${D}${bindir}
    install -d ${D}${sysconfdir}/usp-pa
    install -d ${D}${systemd_system_unitdir}

    install -m 0755 ${WORKDIR}/UspPa.sh ${D}${bindir}/UspPa
    install -m 0644 ${WORKDIR}/conf/usp_factory_reset.conf ${D}${sysconfdir}/usp-pa
    install -m 0644 ${WORKDIR}/conf/usp_dm_objs.conf ${D}${sysconfdir}/usp-pa
    install -m 0644 ${WORKDIR}/conf/usp_dm_params.conf ${D}${sysconfdir}/usp-pa
    install -m 0644 ${WORKDIR}/conf/usp_truststore.pem ${D}${sysconfdir}/usp-pa
    install -m 0644 ${WORKDIR}/usp-pa.service ${D}${systemd_system_unitdir}
}

# Files in staging area to copy to system image
FILES:${PN} += "${bindir}/UspPa"
FILES:${PN} += "${sysconfdir}/usp-pa/usp_factory_reset.conf"
FILES:${PN} += "${sysconfdir}/usp-pa/usp_dm_objs.conf"
FILES:${PN} += "${sysconfdir}/usp-pa/usp_dm_params.conf"
FILES:${PN} += "${sysconfdir}/usp-pa/usp_truststore.pem"

# Signal that a system-d service must be provisioned
SYSTEMD_SERVICE:${PN} = "usp-pa.service"

## Additional steps for DAC Distro Feature
TARGET_CFLAGS  += "${@bb.utils.contains('DISTRO_FEATURES', 'dac', ' -DINCLUDE_LCM_DATAMODEL ', '', d)}"
