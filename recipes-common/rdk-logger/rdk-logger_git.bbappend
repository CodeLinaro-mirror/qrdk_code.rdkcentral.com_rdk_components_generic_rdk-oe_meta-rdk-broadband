FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += " file://rdkb_log4crc "

do_configure:append () {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'no_mta_support', 'true', 'false', d)}; then
        sed -i '/mta/Id' ${WORKDIR}/rdkb_log4crc
    fi   
    if ${@bb.utils.contains('DISTRO_FEATURES', 'no_moca_support', 'true', 'false', d)}; then
        sed -i '/moca/Id' ${WORKDIR}/rdkb_log4crc
    fi   
    install -m 644 ${WORKDIR}/rdkb_log4crc ${S}/log4crc
}

do_install:append () {
        install -d ${D}/rdklogger
        install -d ${D}/fss/gw/rdklogger
        ln -sf /etc/log4crc ${D}/rdklogger/log4crc
        ln -sf /etc/log4crc ${D}/fss/gw/rdklogger/log4crc
}

FILES:${PN} += " /rdklogger/ \
                 /fss/gw/rdklogger/ \
               "
