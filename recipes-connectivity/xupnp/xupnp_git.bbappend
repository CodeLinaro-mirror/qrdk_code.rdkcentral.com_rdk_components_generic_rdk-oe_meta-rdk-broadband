CFLAGS:prepend += " ${@bb.utils.contains('DISTRO_FEATURES', 'IDM_DEBUG',' -DIDM_DEBUG','', d)}"
DEPENDS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'rdkb_inter_device_manager', ' utopia ','', d)}"
EXTRA_OECONF:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'rdkb_inter_device_manager', ' --enable-rdkb-inter-device-manager-support ', '', d)}"
LDFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'rdkb_inter_device_manager', ' -lsysevent ', '', d)}"
