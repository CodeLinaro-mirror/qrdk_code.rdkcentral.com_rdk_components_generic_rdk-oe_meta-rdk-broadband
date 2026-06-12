RDEPENDS:packagegroup-rdk-ccsp-broadband:append = " \
		${@bb.utils.contains('DISTRO_FEATURES', 'Wifi-test-suite', 'wifi-emulator', '', d)} \
		\
"
