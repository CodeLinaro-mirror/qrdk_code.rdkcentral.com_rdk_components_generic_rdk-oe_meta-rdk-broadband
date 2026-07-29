SUMMARY = "CCSP CcspSnmpPa component"
HOMEPAGE = "http://github.com/belvedere-yocto/CcspSnmpPa"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=2291535ca559c92189f5f6053018b3e2"

DEPENDS = "ccsp-common-library net-snmp openssl utopia"
DEPENDS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' safec', " ", d)}"

require ccsp_common.inc

SRC_URI = "${CMF_GITHUB_ROOT}/snmp-protocol-agent;protocol=https;${BRANCH_ccsp_snmp_pa}"

CFLAGS += " -Wall -Werror -Wextra "

S = "${UNPACKDIR}/${PN}-${PV}"

inherit autotools breakpad-logmapper
CFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec',  ' `pkg-config --cflags libsafec`', '-fPIC', d)}"
CFLAGS:remove:wrynose = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec',  ' `pkg-config --cflags libsafec`', '-fPIC', d)}"

LDFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' `pkg-config --libs libsafec`', '', d)}"
LDFLAGS:remove:wrynose = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' `pkg-config --libs libsafec`', '', d)}"
CFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', '', ' -DSAFEC_DUMMY_API', d)}"
CFLAGS:append += " ${@bb.utils.contains('DISTRO_FEATURES', 'OneWifi', '-DRDK_ONEWIFI', '', d)}"

CFLAGS:append = " \
    -I=${includedir}/dbus-1.0 \
    -I=${libdir}/dbus-1.0/include \
    -I=${includedir}/ccsp \
    -I${STAGING_INCDIR}/syscfg \
    "
CFLAGS:append = "${@bb.utils.contains("DISTRO_FEATURES", "snmppa", " -DSNMP_PA_ENABLE ", " ", d)}"

LDFLAGS:append = " \
    -ldbus-1 \
    -lsyscfg \
    "

do_install:append () {
	if ${@bb.utils.contains('DISTRO_FEATURES', 'snmppa', 'true', 'false', d)}; then
		install -d ${D}/etc
		touch ${D}/etc/SNMP_PA_ENABLE
		# Config files and scripts
		install -d ${D}/usr/ccsp/snmp
		install -m 644 ${S}/config/snmpd.conf -t ${D}/usr/ccsp/snmp
		install -m 777 ${S}/scripts/run_snmpd.sh -t ${D}/usr/ccsp/snmp
		install -m 777 ${S}/scripts/run_subagent.sh -t ${D}/usr/ccsp/snmp
		install -m 644 ${S}/Mib2DmMapping/Ccsp*.xml -t ${D}/usr/ccsp/snmp
		install -m 644 ${S}/Mib2DmMapping/XOPS-DEVICE-MGMT-MIB.xml -t ${D}/usr/ccsp/snmp
		install -m 644 ${S}/Mib2DmMapping/SELFHEAL-DEVICE-MIB.xml -t ${D}/usr/ccsp/snmp
		install -m 644 ${S}/Mib2DmMapping/DEVICE-WEBPA-MIB.xml -t ${D}/usr/ccsp/snmp
	fi
        #Remove mta releated mib support.
        if ${@bb.utils.contains('DISTRO_FEATURES', 'no_mta_support', 'true', 'false', d)}; then
           sed -i -e 'N;/rdkbRgDeviceMTA/,/scalarGroup/d;P;D' ${D}${exec_prefix}/ccsp/snmp/Ccsp_RDKB-RG-MIB-DeviceMgmt.xml
        fi
}

PACKAGES += "${PN}-ccsp"

FILES:${PN}-ccsp = " \
	${@bb.utils.contains("DISTRO_FEATURES", "snmppa", "/etc/* ", " ", d)} \
	${@bb.utils.contains("DISTRO_FEATURES", "snmppa", "${prefix}/ccsp/snmp/snmpd.conf ", " ", d)} \
	${@bb.utils.contains("DISTRO_FEATURES", "snmppa", "${prefix}/ccsp/snmp/run_snmpd.sh ", " ", d)} \
	${@bb.utils.contains("DISTRO_FEATURES", "snmppa", "${prefix}/ccsp/snmp/run_subagent.sh ", " ", d)} \
	${@bb.utils.contains("DISTRO_FEATURES", "snmppa", "${prefix}/ccsp/snmp/Ccsp*.xml ", " ", d)} \
	${@bb.utils.contains("DISTRO_FEATURES", "snmppa", "${prefix}/ccsp/snmp/SELFHEAL-DEVICE-MIB.xml ", " ", d)} \
	${@bb.utils.contains("DISTRO_FEATURES", "snmppa", "${prefix}/ccsp/snmp/XOPS-DEVICE-MGMT-MIB.xml ", " ", d)} \
	${@bb.utils.contains("DISTRO_FEATURES", "snmppa", "${prefix}/ccsp/snmp/DEVICE-WEBPA-MIB.xml ", " ", d)} \
"

FILES:${PN}-dbg = " \
	${@bb.utils.contains("DISTRO_FEATURES", "snmppa", "${prefix}/ccsp/snmp/.debug ", " ", d)} \
	${@bb.utils.contains("DISTRO_FEATURES", "snmppa", "${prefix}/src/debug ", " ", d)} \
	${@bb.utils.contains("DISTRO_FEATURES", "snmppa", "${bindir}/.debug ", " ", d)} \
	${@bb.utils.contains("DISTRO_FEATURES", "snmppa", "${libdir}/.debug ", " ", d)} \
"
