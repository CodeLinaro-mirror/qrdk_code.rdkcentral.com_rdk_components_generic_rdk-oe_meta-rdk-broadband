do_install:append() {
    # Remove unwanted binaries
    rm -rf ${D}${bindir}
}

FILES:${PN}-consolefonts:remove = "${datadir}/consolefonts"
FILES:${PN}-keymaps:remove = "${datadir}/keymaps"
