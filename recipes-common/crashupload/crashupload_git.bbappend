do_install_append() {

        install -d ${D}${systemd_unitdir}/system
        install -m 0644 ${S}/coredump-upload.service ${D}${systemd_unitdir}/system/
        install -m 0644 ${S}/coredump-upload.path ${D}${systemd_unitdir}/system/
        install -m 0644 ${S}/minidump-on-bootup-upload.service ${D}${systemd_unitdir}/system/
        install -m 0644 ${S}/minidump-on-bootup-upload.timer ${D}${systemd_unitdir}/system/
        
        sed -i -e "\$aType=oneshot\n\n[Install]\nWantedBy=multi-user.target\n" ${D}${systemd_unitdir}/system/coredump-upload.service
        sed -i -e '/Path Exists.*/aAfter=network-online.target\nRequires=network-online.target' ${D}${systemd_unitdir}/system/coredump-upload.path
        sed -i -e '/PathChanged=.*/aUnit=coredump-upload.service'  ${D}${systemd_unitdir}/system/coredump-upload.path
        sed -i -- 's/WantedBy=.*/WantedBy=wan-initialized.target/g' ${D}${systemd_unitdir}/system/coredump-upload.path
        sed -i -- 's/WantedBy=.*/WantedBy=wan-initialized.target/g' ${D}${systemd_unitdir}/system/coredump-upload.service
        
}

SYSTEMD_SERVICE_${PN}_append = " coredump-upload.service \
                                           coredump-upload.path \
                                           minidump-on-bootup-upload.service \
                                           minidump-on-bootup-upload.timer \
"

