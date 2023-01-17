#!/bin/sh

usage()
{
    echo "Usage: deviceinfo.sh -[mo|sn|fw|ms|mu|cmac|cip|cipv6|emac|eip|eipv6|lmac|lip|lipv6]"
    echo "       -mo:    Model number"
    echo "       -sn:    Serial number"
    echo "       -fw:    Firmware version"
    echo "       -ms:    Mesh State"
    echo "       -mu:    Mesh URL"
    echo "       -cmac:  CM MAC"
    echo "       -cip:   CM IP"
    echo "       -cipv6: CM IPv6"
    echo "       -emac:  WAN MAC"
    echo "       -eip:   WAN IP"
    echo "       -eipv6: WAN IPv6"
    echo "       -lmac:  LAN MAC"
    echo "       -lip:   LAN IP"
    echo "       -lipv6: LAN IPv6"
    echo "       -eb_enable: ETHBACKHAUL ENABLE"
    echo "       -eb_disable: ETHBACKHAUL DISABLE"
    echo "       -mode: Current Operational Mode - Gateway or ExtenderMode"
    echo "       -off_chan: Off channel scan status(enabled/disabled)"
    exit 1
}

if [ $# -ne 1 ]; then
	usage;
fi

case $1 in
    -mo)
        DEVICE_MODE="`syscfg get Device_Mode`"
        if [ "$DEVICE_MODE" == "1" ]; then
            echo "XLETEST"
        else
            grep -a "MODEL " /tmp/factory_nvram.data | cut -d" " -f2
        fi
        shift 1
        ;;

    -sn)
        grep -a "Serial " /tmp/factory_nvram.data | cut -d" " -f2
        shift 1
        ;;

    -fw)
        grep "imagename:" /version.txt | cut -d":" -f2
        shift 1
        ;;

    -ms)
        syscfg get mesh_state
        shift 1
        ;;

    -mu)
        syscfg get mesh_url
        shift 1
        ;;

    -cmac)
        grep -a "CM " /tmp/factory_nvram.data | cut -d" " -f2
        shift 1
        ;;

    -cip)
        PEER_INTERFACE_IP=`grep PEER_INTERFACE /etc/device.properties | cut -d"=" -f2`
        snmpget -cpub -v2c -Ov $PEER_INTERFACE_IP 1.3.6.1.4.1.4413.2.2.2.1.2.12161.1.2.2.0 | cut -c12-23 | awk '{printf "%d.%d.%d.%d\n", "0x"$1, "0x"$2, "0x"$3, "0x"$4}'
        shift 1
        ;;

    -cipv6)
        PEER_INTERFACE_IP=`grep PEER_INTERFACE /etc/device.properties | cut -d"=" -f2`
        snmpget -cpub -v2c -Ov $PEER_INTERFACE_IP 1.3.6.1.4.1.4413.2.2.2.1.2.12161.1.3.2.0 | cut -c13- | tr -d ' ' | sed 's/.\{4\}/&:/g' | cut -c1-39 | tr '[:upper:]' '[:lower:]'
        shift 1
        ;;

    -emac)
        ifconfig erouter0 | grep HWaddr | cut -d"r" -f5 | cut -d" " -f2
        shift 1
        ;;

    -eip)
        ifconfig erouter0 | grep "inet addr:" | cut -d":" -f2 | cut -d" " -f1
        shift 1
        ;;

    -eipv6)
        ifconfig erouter0 | grep "Scope:Global" | cut -d"r" -f2 | cut -d" " -f2| cut -d"/" -f1
        shift 1
        ;;

    -lmac)
        ifconfig brlan0 | grep HWaddr | cut -d"r" -f4 | cut -d" " -f2
        shift 1
        ;;

    -lip)
        ifconfig brlan0 | grep "inet addr:" | cut -d":" -f2 | cut -d" " -f1
        shift 1
        ;;

    -lipv6)
        ifconfig brlan0 | grep "inet6 addr" | cut -d"r" -f2 | cut -d" " -f2| cut -d"/" -f1
        shift 1
        ;;
    -eb_enable)
        sysevent set meshethbhaul-up 0
        shift 1
        ;;

    -eb_disable)
        sysevent set meshethbhaul-down 0
        shift 1
        ;;

    -mode)
        mode=`syscfg get Device_Mode`
        if [ "$mode" == "1" ]; then
            echo "Extender"
        else
            echo "Gateway"
        fi
        shift 1
        ;;

    -off_chan)
        off_chan_status=`psmcli get Device.DeviceInfo.X_RDK_RFC.Feature.OffChannelScan.Enable`
        echo "$off_chan_status"
        shift 1
        ;;
    *)
        usage;
        ;;

esac

exit 0

