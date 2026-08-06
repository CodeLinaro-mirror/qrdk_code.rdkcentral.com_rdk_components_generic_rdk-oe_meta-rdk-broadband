do_install_append() {

        install -d ${D}${systemd_unitdir}/system
        install -m 0644 ${WORKDIR}/git/coredump-upload.service ${D}${systemd_unitdir}/system/
        install -m 0644 ${WORKDIR}/git/coredump-upload.path ${D}${systemd_unitdir}/system/
        install -m 0644 ${WORKDIR}/git/minidump-on-bootup-upload.service ${D}${systemd_unitdir}/system/
        install -m 0644 ${WORKDIR}/git/minidump-on-bootup-upload.timer ${D}${systemd_unitdir}/system/
        sed -i -e "\$aType=oneshot\n\n[Install]\nWantedBy=multi-user.target\n" ${D}${systemd_unitdir}/system/coredump-upload.service
        sed -i -e '/Path Exists.*/aAfter=network-online.target\nRequires=network-online.target' ${D}${systemd_unitdir}/system/coredump-upload.path
        sed -i -e '/PathChanged=.*/aUnit=coredump-upload.service'  ${D}${systemd_unitdir}/system/coredump-upload.path
        sed -i -- 's/WantedBy=.*/WantedBy=wan-initialized.target/g' ${D}${systemd_unitdir}/system/coredump-upload.path
        sed -i -- 's/WantedBy=.*/WantedBy=wan-initialized.target/g' ${D}${systemd_unitdir}/system/coredump-upload.service

}

do_configure_prepend() {
    if [ "${IS_EXTENDER}" = "false" ]; then
        sed -i -e 's/ -lrfcapi//g' ${WORKDIR}/git/c_sourcecode/src/Makefile.am
    fi
    
}

SYSTEMD_SERVICE_${PN}_append = " coredump-upload.service \
                                 coredump-upload.path \
                                 minidump-on-bootup-upload.service \
                                 minidump-on-bootup-upload.timer \
"

DEPENDS_append = "${@bb.utils.contains('IS_EXTENDER', 'true', '', ' utopia', d)}"
LDFLAGS_append = "${@bb.utils.contains('IS_EXTENDER', 'true', '', ' -lsyscfg -lsysevent', d)}"
EXTRA_OEMAKE_append = "${@bb.utils.contains('IS_EXTENDER', 'true', '', ' LIBS=\'-lsysevent -lsyscfg\'', d)}"
CFLAGS_append = "${@bb.utils.contains('IS_EXTENDER', 'true', '', ' -DBROADBAND', d)}"
