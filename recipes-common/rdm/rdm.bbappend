DEPENDS += "mountutils ccsp-common-library"
DEPENDS:remove:class-nativesdk = "mountutils ccsp-common-library"

CFLAGS:append = "${@bb.utils.contains('DISTRO_FEATURES', 'safec',  ' `pkg-config --cflags libsafec` ', ' -fPIC -DSAFEC_DUMMY_API ', d)}"
CFLAGS:remove:wrynose = "${@bb.utils.contains('DISTRO_FEATURES', 'safec',  ' `pkg-config --cflags libsafec` ', ' -fPIC -DSAFEC_DUMMY_API ', d)}"

LDFLAGS:append = "${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' `pkg-config --libs libsafec` ', '', d)}"
LDFLAGS:remove:wrynose = "${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' `pkg-config --libs libsafec` ', '', d)}"
LDFLAGS:append_dunfell = "${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' -lsafec-3.5.1 ', '', d)}"
LDFLAGS:append_kirkstone = "${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' -lsafec ', '', d)}"
LDFLAGS:append_wrynose = "${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' -lsafec ', '', d)}"

EXTRA_OECONF += "--enable-mountutils"
