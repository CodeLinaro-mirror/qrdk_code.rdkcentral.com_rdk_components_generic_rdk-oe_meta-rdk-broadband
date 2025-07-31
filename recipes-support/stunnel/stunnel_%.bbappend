DEPENDS:remove = "systemd"
SYSTEMD_SERVICE_${PN}:remove = "stunnel.service"
FILES_${PN}:remove = "/lib/systemd/system/stunnel.service"
PACKAGECONFIG:remove = "systemd"
