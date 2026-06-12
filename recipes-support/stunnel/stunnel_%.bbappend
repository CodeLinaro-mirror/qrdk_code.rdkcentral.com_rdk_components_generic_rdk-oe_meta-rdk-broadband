DEPENDS:remove = "systemd"
SYSTEMD_SERVICE:${PN}:remove = "stunnel.service"
FILES:${PN}:remove = "/lib/systemd/system/stunnel.service"
PACKAGECONFIG:remove = "systemd"
