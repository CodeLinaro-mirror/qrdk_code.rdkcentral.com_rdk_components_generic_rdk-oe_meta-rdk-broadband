require ccsp_common.inc

DEPENDS:append = " ccsp-common-library utopia libparodus"

DEPENDS:append = " hal-wifi hal-cm  hal-dhcpv4c hal-ethsw hal-moca hal-mso_mgmt hal-mta hal-platform hal-vlan hal-wifi avro-c "
RDEPENDS:${PN}:append = " libparodus"

EXTRA_OECONF:append = " --enable-journalctl"
EXTRA_OECONF:append = " ONEWIFI_CAC_APP_SUPPORT=true"
EXTRA_OECONF:append = " ONEWIFI_DML_SUPPORT_MAKEFILE=true"

CFLAGS:append = " -I${STAGING_INCDIR}/dbus-1.0"
CFLAGS:append = " -I${STAGING_LIBDIR}/dbus-1.0/include"
CFLAGS:append = " -I${STAGING_INCDIR}/libparodus"

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
           -DONEWIFI_DML_SUPPORT \
           -DONEWIFI_RDKB_CCSP_SUPPORT \
           "

LDFLAGS:append = " -ldbus-1"
LDFLAGS:append = " -llibparodus"
LDFLAGS:append = " -ltrower-base64"
LDFLAGS:append = " -lutctx"

do_compile:prepend () {
    (${PYTHON} ${STAGING_BINDIR_NATIVE}/dm_pack_code_gen.py ${S}/config/TR181-WiFi-USGv2.XML ${S}/source/dml/wifi_ssp/dm_pack_datamodel.c)
}
FILES:${PN}:remove = "${libdir}/libwifi.so"
FILES:${PN}-dev += "${libdir}/*.so"
