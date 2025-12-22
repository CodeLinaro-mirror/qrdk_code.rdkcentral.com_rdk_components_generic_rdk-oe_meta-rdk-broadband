CXXFLAGS_append += " -D_RDK_BROADBAND_PRIV_CAPS_ "
CFLAGS_append = " -D_RDK_BROADBAND_PRIV_CAPS_"
require recipes-ccsp/ccsp/ccsp_common.inc

do_install_append(){
        install -d ${D}${sysconfdir}
        install -d ${D}${sysconfdir}/security/caps/
        install -m 755 ${S}/libunpriv/source/process-capabilities_rdkb.json ${D}${sysconfdir}/security/caps/process-capabilities.json
        if [ "${MACHINE_IMAGE_NAME}" = "CGA4332COM" ] || [ "${MACHINE_IMAGE_NAME}" = "CGM4981COM" ] || [ "${MACHINE_IMAGE_NAME}" = "CGM601TCOM" ] || [ "${MACHINE_IMAGE_NAME}" = "CWA438TCOM" ] || [ "${MACHINE_IMAGE_NAME}" = "SG417DBCT" ]; then
            sed -i '/webpa/!b;n;n;c \      "drop":"UGID_GROUP"' ${D}${sysconfdir}/security/caps/process-capabilities.json
            sed -i '/parodus/!b;n;n;c \      "drop":"UGID_GROUP"' ${D}${sysconfdir}/security/caps/process-capabilities.json
            sed -i '/PsmSsp/!b;n;n;c \      "drop":"UGID_GROUP"' ${D}${sysconfdir}/security/caps/process-capabilities.json
            sed -i '/CcspLMLite/!b;n;n;c \      "drop":""' ${D}${sysconfdir}/security/caps/process-capabilities.json
        fi
        #Remove mta dependency from process-capabilities.json
        if ${@bb.utils.contains('DISTRO_FEATURES', 'no_mta_support', 'true', 'false', d)}; then
            sed -i -e 'N;/CcspMtaAgentSsp/,/drop/d;P;D' ${D}${sysconfdir}/security/caps/process-capabilities.json
        fi
}

FILES_${PN} += " ${sysconfdir}/security/caps/process-capabilities.json"
