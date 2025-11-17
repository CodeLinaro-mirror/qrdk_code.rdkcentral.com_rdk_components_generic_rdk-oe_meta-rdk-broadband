SUMMARY = "CCSP Hotspot Kernel Module"
HOMEPAGE = "https://github.com/belvedere-yocto/hotspot"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=90a09ab320e2368b0ee7213fd5be2d5c"

SRC_URI = "${CMF_GITHUB_ROOT}/mtu-modifier;protocol=https;${BRANCH_ccsp_hotspot_kmod}"

CFLAGS += " -Wall -Werror -Wextra -Wno-pointer-sign -Wno-sign-compare "

do_compile[lockfiles] = "${TMPDIR}/kernel-scripts.lock"

S = "${WORKDIR}/git"

inherit module

PACKAGES += "kernel-module-${PN}"
