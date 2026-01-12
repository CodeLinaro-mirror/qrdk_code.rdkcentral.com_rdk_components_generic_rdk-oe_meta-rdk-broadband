def pv_for_branch(d, default_pv):
    branch = d.getVar('PROJECT_BRANCH', True) or ''
    if 'sprint' in branch:
        return "{}+git${{SRCPV}}".format(default_pv)
    return default_pv

PV_pn-ccsp-adv-security = "${@pv_for_branch(d, '1.1.2_stable2_20260109')}"
PR_pn-ccsp-adv-security = "r0"

PV_pn-ccsp-cm-agent = "${@pv_for_branch(d, '1.3.0')}"
PR_pn-ccsp-cm-agent = "r0"

PV_pn-ccsp-common-library = "${@pv_for_branch(d, '2.0.0_stable2_20260109')}"
PR_pn-ccsp-common-library = "r0"

PV_pn-ccsp-common-library-native = "${@pv_for_branch(d, '2.0.0_stable2_20260109')}"
PR_pn-ccsp-common-library-native = "r0"

PV_pn-ccsp-cr = "${@pv_for_branch(d, '1.0.0')}"
PR_pn-ccsp-cr = "r0"

PV_pn-ccsp-dmcli = "${@pv_for_branch(d, '2.0.0_stable2_20260109')}"
PR_pn-ccsp-dmcli = "r0"

PV_pn-ccsp-eth-agent = "${@pv_for_branch(d, '1.5.0_stable2_20260109')}"
PR_pn-ccsp-eth-agent = "r0"

PV_pn-ccsp-gwprovapp = "${@pv_for_branch(d, '1.2.0_stable2_20260109')}"
PR_pn-ccsp-gwprovapp = "r0"

PV_pn-ccsp-gwprovapp-ethwan = "${@pv_for_branch(d, '1.0.0')}"
PR_pn-ccsp-gwprovapp-ethwan = "r0"

PV_pn-ccsp-home-security = "${@pv_for_branch(d, '1.2.0_stable2_20260109')}"
PR_pn-ccsp-home-security = "r0"

PV_pn-ccsp-hotspot = "${@pv_for_branch(d, '1.3.0_stable2_20260109')}"
PR_pn-ccsp-hotspot = "r0"

PV_pn-ccsp-hotspot-kmod = "${@pv_for_branch(d, '1.1.0_stable2_20260109')}"
PR_pn-ccsp-hotspot-kmod = "r0"

PV_pn-ccsp-lm-lite = "${@pv_for_branch(d, '2.0.0_stable2_20260109')}"
PR_pn-ccsp-lm-lite = "r0"

PV_pn-ccsp-misc = "${@pv_for_branch(d, '1.4.0_stable2_20260109')}"
PR_pn-ccsp-misc = "r0"

PV_pn-ccsp-moca = "${@pv_for_branch(d, '2.0.0_stable2_20260109')}"
PR_pn-ccsp-moca = "r0"

PV_pn-ccsp-mta-agent = "${@pv_for_branch(d, '1.4.0_stable2_20260109')}"
PR_pn-ccsp-mta-agent = "r0"

PV_pn-ccsp-p-and-m = "${@pv_for_branch(d, '2.0.0_stable2_20260109')}"
PR_pn-ccsp-p-and-m = "r0"

PV_pn-ccsp-psm = "${@pv_for_branch(d, '2.0.0_stable2_20260109')}"
PR_pn-ccsp-psm = "r0"

PV_pn-ccsp-snmp-pa = "${@pv_for_branch(d, '1.1.1_stable2_20260109')}"
PR_pn-ccsp-snmp-pa = "r0"

PV_pn-ccsp-tr069-pa = "${@pv_for_branch(d, '1.0.0')}"
PR_pn-ccsp-tr069-pa = "r0"

PV_pn-ccsp-webui-bci-jst = "${@pv_for_branch(d, '1.3.0_stable2_20260109')}"
PR_pn-ccsp-webui-bci-jst = "r0"

PV_pn-ccsp-webui-bci-php = "${@pv_for_branch(d, '1.3.0_stable2_20260109')}"
PR_pn-ccsp-webui-bci-php = "r0"

PV_pn-ccsp-webui-jst = "${@pv_for_branch(d, '2.0.0_stable2_20260109')}"
PR_pn-ccsp-webui-jst = "r0"

PV_pn-ccsp-webui-php = "${@pv_for_branch(d, '2.0.0_stable2_20260109')}"
PR_pn-ccsp-webui-php = "r0"

PV_pn-ccsp-xconf = "${@pv_for_branch(d, '1.2.0_stable2_20260109')}"
PR_pn-ccsp-xconf = "r0"

PV_pn-ccsp-xdns = "${@pv_for_branch(d, '1.0.0')}"
PR_pn-ccsp-xdns = "r0"

PV_pn-core-net-lib = "${@pv_for_branch(d, '2.0.0_stable2_20260109')}"
PR_pn-core-net-lib = "r0"

PV_pn-halinterface = "${@pv_for_branch(d, '1.1.0')}"
PR_pn-halinterface = "r0"

PV_pn-hal-bridgeutil = "${@pv_for_branch(d, '1.0.0')}"
PR_pn-hal-bridgeutil = "r0"

PV_pn-hal-cm-generic = "${@pv_for_branch(d, '1.0.0')}"
PR_pn-hal-cm-generic = "r0"

PV_pn-hal-dhcpv4c-generic = "${@pv_for_branch(d, '1.0.0')}"
PR_pn-hal-dhcpv4c-generic = "r0"

PV_pn-hal-epon-generic = "${@pv_for_branch(d, '1.0.0')}"
PR_pn-hal-epon-generic = "r0"

PV_pn-hal-ethsw-generic = "${@pv_for_branch(d, '1.0.0')}"
PR_pn-hal-ethsw-generic = "r0"

PV_pn-hal-firewall-generic = "${@pv_for_branch(d, '1.0.0')}"
PR_pn-hal-firewall-generic = "r0"

PV_pn-hal-moca-generic = "${@pv_for_branch(d, '1.0.0')}"
PR_pn-hal-moca-generic = "r0"

PV_pn-hal-mso-mgmt-generic = "${@pv_for_branch(d, '1.0.0')}"
PR_pn-hal-mso-mgmt-generic = "r0"

PV_pn-hal-mta-generic = "${@pv_for_branch(d, '1.0.0')}"
PR_pn-hal-mta-generic = "r0"

PV_pn-hal-platform-generic = "${@pv_for_branch(d, '1.0.0')}"
PR_pn-hal-platform-generic = "r0"

PV_pn-hal-vlan-generic = "${@pv_for_branch(d, '1.0.0')}"
PR_pn-hal-vlan-generic = "r0"

PV_pn-hal-wifi-generic = "${@pv_for_branch(d, '1.0.0')}"
PR_pn-hal-wifi-generic = "r0"

PV_pn-harvester = "${@pv_for_branch(d, '1.1.1_stable2_20260109')}"
PR_pn-harvester = "r0"

PV_pn-json-hal-lib = "${@pv_for_branch(d, '2.0.0')}"
PR_pn-json-hal-lib = "r0"

PV_pn-jst = "${@pv_for_branch(d, '1.0.1')}"
PR_pn-jst = "r0"

PV_pn-lanmanager = "${@pv_for_branch(d, '1.0.0')}"
PR_pn-lanmanager = "r0"

PV_pn-notify-comp = "${@pv_for_branch(d, '1.1.0')}"
PR_pn-notify-comp = "r0"

PV_pn-ovs-agent = "${@pv_for_branch(d, '1.1.0_stable2_20260109')}"
PR_pn-ovs-agent = "r0"

PV_pn-powermgr = "${@pv_for_branch(d, '1.1.0_stable2_20251205')}"
PR_pn-powermgr = "r0"

PV_pn-rdk-fwupgrade-manager = "${@pv_for_branch(d, '1.2.0')}"
PR_pn-rdk-fwupgrade-manager = "r0"

PV_pn-rdk-cellularmanager = "${@pv_for_branch(d, '2.0.0_stable2_20260109')}"
PR_pn-rdk-cellularmanager = "r0"

PV_pn-start-parodus = "${@pv_for_branch(d, '2.0.0_stable2_20260109')}"
PR_pn-start-parodus = "r0"

PV_pn-test-and-diagnostic = "${@pv_for_branch(d, '2.0.0_stable2_20260109')}"
PR_pn-test-and-diagnostic = "r0"

PV_pn-utopia = "${@pv_for_branch(d, '2.0.0_stable2_20260109')}"
PR_pn-utopia = "r0"
