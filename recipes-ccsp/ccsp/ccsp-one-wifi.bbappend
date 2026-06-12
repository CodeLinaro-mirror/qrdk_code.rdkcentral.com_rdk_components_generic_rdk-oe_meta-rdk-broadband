require ccsp_common.inc

DEPENDS:append = " utopia "
DEPENDS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'onewifi_json_dml_support', '', 'ccsp-common-library libparodus', d)}"

DEPENDS:append = " hal-wifi hal-cm  hal-dhcpv4c hal-ethsw hal-moca hal-mso_mgmt hal-mta hal-platform hal-vlan hal-wifi avro-c "
RDEPENDS:${PN}:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'onewifi_json_dml_support', '', 'libparodus', d)}"

EXTRA_OECONF:append = " --enable-journalctl"
EXTRA_OECONF:append = " ONEWIFI_CAC_APP_SUPPORT=true"
EXTRA_OECONF:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'onewifi_json_dml_support', 'ONEWIFI_JSON_DML_SUPPORT=true', 'ONEWIFI_DML_SUPPORT_MAKEFILE=true', d)}"
EXTRA_OECONF:append = " ONEWIFI_CSI_APP_SUPPORT=true"
EXTRA_OECONF:append = " ONEWIFI_MOTION_APP_SUPPORT=true"
EXTRA_OECONF:append = " ONEWIFI_HARVESTER_APP_SUPPORT=true"
EXTRA_OECONF:append = " ONEWIFI_ANALYTICS_APP_SUPPORT=true"
EXTRA_OECONF:append = " ONEWIFI_LEVL_APP_SUPPORT=true"
EXTRA_OECONF:append = " ONEWIFI_WHIX_APP_SUPPORT=true"
EXTRA_OECONF:append = " ONEWIFI_BLASTER_APP_SUPPORT=true"
EXTRA_OECONF:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'offchannel_scan_5g', ' FEATURE_OFF_CHANNEL_SCAN_5G=true ', '', d)}"
EXTRA_OECONF:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'Memwrap_Tool', ' ONEWIFI_MEMWRAPTOOL_APP_SUPPORT=true ', '', d)}"

CFLAGS:append = " -I${STAGING_INCDIR}/dbus-1.0"
CFLAGS:append = " -I${STAGING_LIBDIR}/dbus-1.0/include"
CFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'onewifi_json_dml_support', '', '-I${STAGING_INCDIR}/libparodus', d)}"

CFLAGS:append = " -DONEWIFI_CSI_APP_SUPPORT  \
           -DONEWIFI_CAC_APP_SUPPORT \
           -DONEWIFI_MOTION_APP_SUPPORT \
           -DONEWIFI_HARVESTER_APP_SUPPORT \
           -DONEWIFI_ANALYTICS_APP_SUPPORT \
           -DONEWIFI_LEVL_APP_SUPPORT \
           -DONEWIFI_WHIX_APP_SUPPORT \
           -DONEWIFI_BLASTER_APP_SUPPORT \
           -DONEWIFI_RDKB_APP_SUPPORT \
           -DONEWIFI_DB_SUPPORT \
           "
CFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'Memwrap_Tool', '-DONEWIFI_MEMWRAPTOOL_APP_SUPPORT', '', d)}"

CFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'onewifi_json_dml_support', '-DONEWIFI_JSON_DML_SUPPORT', '-DONEWIFI_DML_SUPPORT  -DONEWIFI_RDKB_CCSP_SUPPORT', d)}"
CFLAGS:remove = " ${@bb.utils.contains('DISTRO_FEATURES', 'onewifi_json_dml_support', '-DCCSP_SUPPORT_ENABLED', '', d)}"

LDFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'dbus_support', '-ldbus-1', '', d)}"
LDFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'onewifi_json_dml_support', '', '-llibparodus', d)}"
LDFLAGS:append = " -ltrower-base64"
LDFLAGS:append = " -lutctx"


do_compile:prepend () {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'onewifi_json_dml_support', 'false', 'true', d)}; then
        ${PYTHON} ${STAGING_BINDIR_NATIVE}/dm_pack_code_gen.py ${S}/config/TR181-WiFi-USGv2.XML ${S}/source/dml/wifi_ssp/dm_pack_datamodel.c
    fi
}

do_install:append () {
    install -d ${D}/usr/ccsp/wifi

    if ${@bb.utils.contains('DISTRO_FEATURES', 'onewifi_json_dml_support', 'true', 'false', d)} ; then
        install -m 775 ${S}/config/bus_dml_config.json -t ${D}/usr/ccsp/wifi
    fi
    install -m 775 ${S}/config/Data_Elements_JSON_Schema_v3.0.json -t ${D}/usr/ccsp/wifi
}

FILES:${PN}:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'onewifi_json_dml_support', '${prefix}/ccsp/wifi/bus_dml_config.json', '', d)}"
FILES:${PN}:append = " ${prefix}/ccsp/wifi/Data_Elements_JSON_Schema_v3.0.json"
