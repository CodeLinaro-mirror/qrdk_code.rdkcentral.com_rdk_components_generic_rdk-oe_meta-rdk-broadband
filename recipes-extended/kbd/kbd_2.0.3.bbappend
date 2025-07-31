do_install:append() {
    # Remove unwanted binaries
    rm -rf ${D}${bindir}
}

FILES_${PN}-consolefonts:remove = "${datadir}/consolefonts"
FILES_${PN}-keymaps:remove = "${datadir}/keymaps"
