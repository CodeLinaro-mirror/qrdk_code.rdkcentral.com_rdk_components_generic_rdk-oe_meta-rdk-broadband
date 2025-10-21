#!/bin/sh

# Wrapper script around obuspa to maintain backwards compatibility with scripts
# calling /usr/bin/UspPa
exec /usr/bin/obuspa --plugin /usr/libexec/usp-pa-vendor-rdk.so "$@"
