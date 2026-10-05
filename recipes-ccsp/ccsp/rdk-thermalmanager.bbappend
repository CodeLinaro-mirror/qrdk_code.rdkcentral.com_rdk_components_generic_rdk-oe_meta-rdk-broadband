# generating minidumps symbols
inherit breakpad-wrapper
DEPENDS += "breakpad breakpad-wrapper"
BREAKPAD_BIN_append = " thermalmanager"

LDFLAGS += "-lbreakpadwrapper -lpthread -lstdc++"

# generating minidumps
PACKAGECONFIG_append = " breakpad"

