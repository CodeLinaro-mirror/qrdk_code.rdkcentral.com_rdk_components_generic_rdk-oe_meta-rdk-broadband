# generating minidumps symbols
inherit breakpad-wrapper
DEPENDS += "breakpad breakpad-wrapper"
BREAKPAD_BIN:append = " thermalmanager"

LDFLAGS += "-lbreakpadwrapper -lpthread -lstdc++"

# generating minidumps
PACKAGECONFIG:append = " breakpad"

