SUMMARY = "OB-USP-AGENT"
DESCRIPTION = "Open Broadband-User Services Platform-Agent (OB-USP-Agent) is an open source project that is focused on creating a reference implementation of the User Services Platform (USP) specification from an "Agent" perspective"
DEPENDS = "curl openssl sqlite3 zlib"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1fcc8a31dcd8bb90cb70510dd3fd17d2"

SRC_URI += "git://github.com/BroadbandForum/obuspa;protocol=http;branch=master;name=obuspa"

SRCREV = "cd71ce1fe34e782b6b417e9eee46f861060301bf"
PV = "10.0.11+git${SRCPV}"

# Customized vendor definitions specific to RDK
# This may need to be updated with new versions of obuspa
SRC_URI += "file://0001-Update-vendor_defs.h-from-usp-pa-vendor-rdk.patch"

S = "${UNPACKDIR}/${PN}-${PV}"

# Configure options for OBUSPA
PACKAGECONFIG ??= "bulkdata coap mqtt stomp uds"
PACKAGECONFIG[bulkdata] = "--enable-bulkdata,--disable-bulkdata"
PACKAGECONFIG[coap] = "--enable-coap,--disable-coap"
PACKAGECONFIG[hardening] = "--enable-hardening,--disable-hardening"
PACKAGECONFIG[mqtt] = "--enable-mqtt,--disable-mqtt"
PACKAGECONFIG[stomp] = "--enable-stomp,--disable-stomp"
PACKAGECONFIG[uds] = "--enable-uds,--disable-uds"
PACKAGECONFIG[websockets] = "--enable-websockets,--disable-websockets"

# Build dependencies based on configure options
DEPENDS += "${@bb.utils.contains('PACKAGECONFIG', 'mqtt', 'mosquitto', '', d)}"
DEPENDS += "${@bb.utils.contains('PACKAGECONFIG', 'websockets', 'libwebsockets', '', d)}"

inherit autotools pkgconfig
