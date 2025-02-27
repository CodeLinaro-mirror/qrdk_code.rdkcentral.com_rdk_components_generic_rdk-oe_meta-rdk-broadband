RDEPENDS_packagegroup-rdk-ccsp-broadband_append = " \
		${@bb.utils.contains('DISTRO_FEATURES', 'wifi-emulator', 'wifi-emulator', '', d)} \
		\
"
