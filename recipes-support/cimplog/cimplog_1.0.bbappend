EXTRA_OECMAKE:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'bci', '', ' -DFEATURE_SUPPORT_ONBOARD_LOGGING=false',d)}"
