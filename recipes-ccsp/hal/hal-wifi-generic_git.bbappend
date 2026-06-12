FILESEXTRAPATHS:prepend := " ${@bb.utils.contains('DISTRO_FEATURES', 'hal-ipc', "${THISDIR}/files:", "", d)}"

SRC_URI:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'hal-ipc', " file://cmxb7-onewifi-hal-ipc.patch", "", d)}"

CFLAGS:append:wrynose = " \
    -Wno-incompatible-pointer-types \
    -Wno-implicit-function-declaration \
    -Wno-int-conversion \
    -Wno-return-mismatch \
    -Wno-implicit-int \
"
