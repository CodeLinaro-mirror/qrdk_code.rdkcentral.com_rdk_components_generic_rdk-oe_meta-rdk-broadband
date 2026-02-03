# For custom build verification in sprint:
#     Update SRCREV_pn of specific component in this .inc file
#         Example: SRCREV_pn-ccsp-adv-security = "1234abcd"
#     and mention 'nobranch=1' for the same component in generic-branch.inc
# (or)
#     Update feature branch for specific component in generic-branch.inc
#         Example: BRANCH_ccsp_adv_security = "branch=feature/RDKB-12345"

#######################################################################
# Below mentioned SRCREV is not applicable for sprint builds
#######################################################################
SRCREV_ccsp_adv_security = "ebdd3ff628b9e8f4c1223a6065279cf9c1176e14"
SRCREV_ccsp_cm_agent = "4ae04eb68bf948607f19da52c4af1eca7b595b1f"
SRCREV_ccsp_common_library = "aa2292bd70649c7f271bc46cdaeb4178e5b939d5"
SRCREV_ccsp_cr = "7397f714551d767c9c035655ad9f1910bac337a1"
SRCREV_ccsp_dmcli = "5b2cf7d7632715fcec895e9b36bdfa31463a86ca"
SRCREV_ccsp_eth_agent = "c1c1d4f3de408279f429b35bad814a5fdc68c6d7"
SRCREV_ccsp_gwprovapp = "1af3b5ebec817b64c4e94c9c0bb60d6dc397a173"
SRCREV_ccsp_gwprovapp_ethwan = "5133ed4745360a13261225eb244f04e647ccc2f0"
SRCREV_ccsp_home_security = "17e08b7126fe41ed2730dd5b7d4ca5489df0f658"
SRCREV_ccsp_hotspot = "a8bc9d66c4ada31ac3e2f60c7f0c7289c544072e"
SRCREV_ccsp_hotspot_kmod = "6fde1011bfe31170bcab6a9c2eb78b7af0d764d3"
SRCREV_ccsp_lm_lite = "78dd836a64aab8ed003e067179f6f8901805ac54"
SRCREV_ccsp_misc = "adff392649aeab24c10c84dc9d057d6a029929b7"
SRCREV_ccsp_moca = "bee6e3b0645873cd708bceb8363eb89c7ae9729a"
SRCREV_ccsp_mta_agent = "6b857b5f91e88df666a9db8e95d60d7ff6e43e4c"
SRCREV_ccsp_p_and_m = "b348d19d21cbe3b1c5f914a094ed467400438230"
SRCREV_ccsp_psm = "605a286750bae9c9501c2ef64114f6e2e716ac20"
SRCREV_ccsp_snmp_pa = "4f2a396711cd871d08f2233cdda1e2a46b8b036c"
SRCREV_ccsp_tr069_pa = "5168ea5ba9ff7fb6f9c2b02c0a7f652ecb1e7404"
SRCREV_ccsp_webui_bci_jst = "87ade3c41fed92f077b2675afd1e719e586afa01"
SRCREV_ccsp_webui_bci_php = "87ade3c41fed92f077b2675afd1e719e586afa01"
SRCREV_ccsp_webui_jst = "962874d2cae993e6b45425d8de194fcde3405971"
SRCREV_ccsp_webui_php = "962874d2cae993e6b45425d8de194fcde3405971"
SRCREV_ccsp_xconf = "290caf35e179019d78d1602c5a52b313c554e261"
SRCREV_ccsp_xdns = "d53e6ff212cd2a7a0faabe3d18bf9fdb2d1300da"
SRCREV_core_net_lib = "d1570227e209a1f509fdaa2276963e33a520e78f"
SRCREV_halinterface = "56bb2bde5e702d45dac2ddca7e1711d6bc2e834a"
SRCREV_hardware_abstraction_layer = "872e007b99c82418611445274e85780d27a0cb57"
SRCREV_harvester = "ea0f93e91246a83f272e59ed9f365b56f0d361b6"
SRCREV_json_hal_lib = "e723b0c8a8c7652fe65506676430bbd4a7d15579"
SRCREV_jst = "13c953b8002c6c2d31246f9ecc9314201761d92c"
SRCREV_lanmanager = "aa42d810ce60c80071eefbc9347e528f5f4589af"
SRCREV_notify_comp = "0dd52a26cf5d834f7f3f30dd09103071a3dff00d"
SRCREV_ovs_agent = "c99a379f7d4d4844c23f35073b32dae7018397c1"
SRCREV_powermgr = "1217b920d99f9c1970ea03de9105ab7a096877d7"
SRCREV_rdk_fwupgrade_manager = "4b97b49dce1c36eda0353be3a70510fc1abb2c69"
SRCREV_rdk_cellularmanager = "856b20d928704aa4d15d64690098738e9d9a15e3"
SRCREV_start_parodus = "cef021170cf9336db877e76d044961f8b83d3e98"
SRCREV_test_and_diagnostic = "dd1f2f9a9d8753f602687cab0ef4706117990fc0"
SRCREV_utopia = "c8a9d17dbc1e5f99c63b33b7847241c2fbb0efc4"
SRCREV_sysint-broadband = "061ed28e290b107577f7130378308076c4b7aafd"

def srcrev_for_branch(d, default_rev):
    branch = d.getVar('PROJECT_BRANCH', True) or ''
    distro_features = d.getVar('DISTRO_FEATURES', True) or ''

    if 'custom_branch_verification' in distro_features:
        return '${AUTOREV}'
    if 'sprint' in branch:
        return '${AUTOREV}'
    return default_rev

#######################################################################
# Update SRCREV_pn here for custom build verification in sprint
#######################################################################
SRCREV_pn-ccsp-adv-security = "${@srcrev_for_branch(d, '${SRCREV_ccsp_adv_security}')}"
SRCREV_pn-ccsp-cm-agent = "${@srcrev_for_branch(d, '${SRCREV_ccsp_cm_agent}')}"
SRCREV_pn-ccsp-common-library = "${@srcrev_for_branch(d, '${SRCREV_ccsp_common_library}')}"
SRCREV_pn-ccsp-common-library-native = "${SRCREV_pn-ccsp-common-library}"
SRCREV_pn-ccsp-cr = "${@srcrev_for_branch(d, '${SRCREV_ccsp_cr}')}"
SRCREV_pn-ccsp-dmcli = "${@srcrev_for_branch(d, '${SRCREV_ccsp_dmcli}')}"
SRCREV_pn-ccsp-eth-agent = "${@srcrev_for_branch(d, '${SRCREV_ccsp_eth_agent}')}"
SRCREV_pn-ccsp-gwprovapp = "${@srcrev_for_branch(d, '${SRCREV_ccsp_gwprovapp}')}"
SRCREV_pn-ccsp-gwprovapp-ethwan = "${@srcrev_for_branch(d, '${SRCREV_ccsp_gwprovapp_ethwan}')}"
SRCREV_pn-ccsp-home-security = "${@srcrev_for_branch(d, '${SRCREV_ccsp_home_security}')}"
SRCREV_pn-ccsp-hotspot = "${@srcrev_for_branch(d, '${SRCREV_ccsp_hotspot}')}"
SRCREV_pn-ccsp-hotspot-kmod = "${@srcrev_for_branch(d, '${SRCREV_ccsp_hotspot_kmod}')}"
SRCREV_pn-ccsp-lm-lite = "${@srcrev_for_branch(d, '${SRCREV_ccsp_lm_lite}')}"
SRCREV_pn-ccsp-misc = "${@srcrev_for_branch(d, '${SRCREV_ccsp_misc}')}"
SRCREV_pn-ccsp-moca = "${@srcrev_for_branch(d, '${SRCREV_ccsp_moca}')}"
SRCREV_pn-ccsp-mta-agent = "${@srcrev_for_branch(d, '${SRCREV_ccsp_mta_agent}')}"
SRCREV_pn-ccsp-p-and-m = "${@srcrev_for_branch(d, '${SRCREV_ccsp_p_and_m}')}"
SRCREV_CcspPsm_pn-ccsp-psm = "${@srcrev_for_branch(d, '${SRCREV_ccsp_psm}')}"
SRCREV_pn-ccsp-snmp-pa = "${@srcrev_for_branch(d, '${SRCREV_ccsp_snmp_pa}')}"
SRCREV_pn-ccsp-tr069-pa = "${@srcrev_for_branch(d, '${SRCREV_ccsp_tr069_pa}')}"
SRCREV_pn-ccsp-webui-bci-jst = "${@srcrev_for_branch(d, '${SRCREV_ccsp_webui_bci_jst}')}"
SRCREV_pn-ccsp-webui-bci-php = "${@srcrev_for_branch(d, '${SRCREV_ccsp_webui_bci_php}')}"
SRCREV_pn-ccsp-webui-jst = "${@srcrev_for_branch(d, '${SRCREV_ccsp_webui_jst}')}"
SRCREV_pn-ccsp-webui-php = "${@srcrev_for_branch(d, '${SRCREV_ccsp_webui_php}')}"
SRCREV_pn-ccsp-xconf = "${@srcrev_for_branch(d, '${SRCREV_ccsp_xconf}')}"
SRCREV_pn-ccsp-xdns = "${@srcrev_for_branch(d, '${SRCREV_ccsp_xdns}')}"
SRCREV_pn-core-net-lib = "${@srcrev_for_branch(d, '${SRCREV_core_net_lib}')}"
SRCREV_pn-halinterface = "${@srcrev_for_branch(d, '${SRCREV_halinterface}')}"
SRCREV_bridgeutilhal_pn-hal-bridgeutil = "${@srcrev_for_branch(d, '${SRCREV_hardware_abstraction_layer}')}"
SRCREV_cmhal_pn-hal-cm-generic = "${@srcrev_for_branch(d, '${SRCREV_hardware_abstraction_layer}')}"
SRCREV_dhcpv4hal_pn-hal-dhcpv4c-generic = "${@srcrev_for_branch(d, '${SRCREV_hardware_abstraction_layer}')}"
SRCREV_eponhal_pn-hal-epon-generic = "${@srcrev_for_branch(d, '${SRCREV_hardware_abstraction_layer}')}"
SRCREV_ethswhal_pn-hal-ethsw-generic = "${@srcrev_for_branch(d, '${SRCREV_hardware_abstraction_layer}')}"
SRCREV_firewallhal_pn-hal-firewall-generic = "${@srcrev_for_branch(d, '${SRCREV_hardware_abstraction_layer}')}"
SRCREV_mocahal_pn-hal-moca-generic = "${@srcrev_for_branch(d, '${SRCREV_hardware_abstraction_layer}')}"
SRCREV_msomgmthal_pn-hal-mso-mgmt-generic = "${@srcrev_for_branch(d, '${SRCREV_hardware_abstraction_layer}')}"
SRCREV_mtahal_pn-hal-mta-generic = "${@srcrev_for_branch(d, '${SRCREV_hardware_abstraction_layer}')}"
SRCREV_platformhal_pn-hal-platform-generic = "${@srcrev_for_branch(d, '${SRCREV_hardware_abstraction_layer}')}"
SRCREV_vlanhal_pn-hal-vlan-generic = "${@srcrev_for_branch(d, '${SRCREV_hardware_abstraction_layer}')}"
SRCREV_wifihal_pn-hal-wifi-generic = "${@srcrev_for_branch(d, '${SRCREV_hardware_abstraction_layer}')}"
SRCREV_pn-harvester = "${@srcrev_for_branch(d, '${SRCREV_harvester}')}"
SRCREV_pn-json-hal-lib = "${@srcrev_for_branch(d, '${SRCREV_json_hal_lib}')}"
SRCREV_pn-jst = "${@srcrev_for_branch(d, '${SRCREV_jst}')}"
SRCREV_pn-lanmanager = "${@srcrev_for_branch(d, '${SRCREV_lanmanager}')}"
SRCREV_pn-notify-comp = "${@srcrev_for_branch(d, '${SRCREV_notify_comp}')}"
SRCREV_pn-ovs-agent = "${@srcrev_for_branch(d, '${SRCREV_ovs_agent}')}"
SRCREV_pn-powermgr = "${@srcrev_for_branch(d, '${SRCREV_powermgr}')}"
SRCREV_pn-rdk-fwupgrade-manager = "${@srcrev_for_branch(d, '${SRCREV_rdk_fwupgrade_manager}')}"
SRCREV_pn-rdk-cellularmanager = "${@srcrev_for_branch(d, '${SRCREV_rdk_cellularmanager}')}"
SRCREV_pn-start-parodus = "${@srcrev_for_branch(d, '${SRCREV_start_parodus}')}"
SRCREV_pn-test-and-diagnostic = "${@srcrev_for_branch(d, '${SRCREV_test_and_diagnostic}')}"
SRCREV_pn-utopia = "${@srcrev_for_branch(d, '${SRCREV_utopia}')}"
SRCREV_sysintbroadband_pn-sysint-broadband = "${@srcrev_for_branch(d, '${SRCREV_sysint-broadband}')}"
