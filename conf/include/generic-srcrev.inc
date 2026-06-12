# For custom build verification in sprint:
#     Update SRCREV_pn of specific component in this .inc file
#         Example: SRCREV:pn-ccsp-adv-security = "1234abcd"
#     and mention 'nobranch=1' for the same component in generic-branch.inc
# (or)
#     Update feature branch for specific component in generic-branch.inc
#         Example: BRANCH_ccsp_adv_security = "branch=feature/RDKB-12345"

#######################################################################
# Below mentioned SRCREV is not applicable for sprint builds
#######################################################################
SRCREV_ccsp_adv_security = "ebdd3ff628b9e8f4c1223a6065279cf9c1176e14"
SRCREV_ccsp_cm_agent = "058ee3ae096e71d47cca72262fd83c17be8c4453"
SRCREV_ccsp_common_library = "c483614694220b9aff95dea72ae62411d81d4837"
SRCREV_ccsp_cr = "7397f714551d767c9c035655ad9f1910bac337a1"
SRCREV_ccsp_dmcli = "f203bb731ea869409cdf83e7a7707c00dd321980"
SRCREV_ccsp_eth_agent = "c1c1d4f3de408279f429b35bad814a5fdc68c6d7"
SRCREV_ccsp_gwprovapp = "1af3b5ebec817b64c4e94c9c0bb60d6dc397a173"
SRCREV_ccsp_gwprovapp_ethwan = "5133ed4745360a13261225eb244f04e647ccc2f0"
SRCREV_ccsp_home_security = "17e08b7126fe41ed2730dd5b7d4ca5489df0f658"
SRCREV_ccsp_hotspot = "a8bc9d66c4ada31ac3e2f60c7f0c7289c544072e"
SRCREV_ccsp_hotspot_kmod = "6fde1011bfe31170bcab6a9c2eb78b7af0d764d3"
SRCREV_ccsp_lm_lite = "845fe0221465e8e146a48512eecc6c5ac9f04337"
SRCREV_ccsp_misc = "75bc528e8a6dc3ed8d77ee1bed4a5fa703293839"
SRCREV_ccsp_moca = "d0b623a922299a08fed4ff854b28ac048ac27d5a"
SRCREV_ccsp_mta_agent = "6b857b5f91e88df666a9db8e95d60d7ff6e43e4c"
SRCREV_ccsp_p_and_m = "19aa42f467db14503182c15b3d64ffeef88ac9ee"
SRCREV_ccsp_psm = "00859265411a6a764d83d8e8c7f3757c8d992f90"
SRCREV_ccsp_snmp_pa = "4f2a396711cd871d08f2233cdda1e2a46b8b036c"
SRCREV_ccsp_tr069_pa = "136db10cac8eb8c4253082cac2a8d297ec62a27d"
SRCREV_ccsp_webui_bci_jst = "87ade3c41fed92f077b2675afd1e719e586afa01"
SRCREV_ccsp_webui_bci_php = "87ade3c41fed92f077b2675afd1e719e586afa01"
SRCREV_ccsp_webui_jst = "962874d2cae993e6b45425d8de194fcde3405971"
SRCREV_ccsp_webui_php = "962874d2cae993e6b45425d8de194fcde3405971"
SRCREV_ccsp_xconf = "50e24d1112440b5e1cf6a26c75be5df1a7134063"
SRCREV_ccsp_xdns = "d53e6ff212cd2a7a0faabe3d18bf9fdb2d1300da"
SRCREV_core_net_lib = "d1570227e209a1f509fdaa2276963e33a520e78f"
SRCREV_halinterface = "56bb2bde5e702d45dac2ddca7e1711d6bc2e834a"
SRCREV_hardware_abstraction_layer = "872e007b99c82418611445274e85780d27a0cb57"
SRCREV_harvester = "fdf3b8d1f343e61a57a2b5d6312a5f37c1d4347a"
SRCREV_json_hal_lib = "e723b0c8a8c7652fe65506676430bbd4a7d15579"
SRCREV_jst = "13c953b8002c6c2d31246f9ecc9314201761d92c"
SRCREV_lanmanager = "aa42d810ce60c80071eefbc9347e528f5f4589af"
SRCREV_notify_comp = "3cb9874b8bb9bc654b984a538ce4cd0fa278e4b4"
SRCREV_ovs_agent = "c99a379f7d4d4844c23f35073b32dae7018397c1"
SRCREV_powermgr = "1217b920d99f9c1970ea03de9105ab7a096877d7"
SRCREV_rdk_fwupgrade_manager = "eae8e768a83fe5ba88d33f2023c9d194e3c22353"
SRCREV_rdk_cellularmanager = "856b20d928704aa4d15d64690098738e9d9a15e3"
SRCREV_start_parodus = "cef021170cf9336db877e76d044961f8b83d3e98"
SRCREV_test_and_diagnostic = "5658ca54fb1e67b1982b8945fbb11e6a8aa4d4e3"
SRCREV_utopia = "e54b40ff5302a4378643382be27a5413bd9bd85b"
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
SRCREV:pn-ccsp-adv-security = "${@srcrev_for_branch(d, '${SRCREV_ccsp_adv_security}')}"
SRCREV:pn-ccsp-cm-agent = "${@srcrev_for_branch(d, '${SRCREV_ccsp_cm_agent}')}"
SRCREV:pn-ccsp-common-library = "${@srcrev_for_branch(d, '${SRCREV_ccsp_common_library}')}"
SRCREV:pn-ccsp-common-library-native = "${SRCREV:pn-ccsp-common-library}"
SRCREV:pn-ccsp-cr = "${@srcrev_for_branch(d, '${SRCREV_ccsp_cr}')}"
SRCREV:pn-ccsp-dmcli = "${@srcrev_for_branch(d, '${SRCREV_ccsp_dmcli}')}"
SRCREV:pn-ccsp-eth-agent = "${@srcrev_for_branch(d, '${SRCREV_ccsp_eth_agent}')}"
SRCREV:pn-ccsp-gwprovapp = "${@srcrev_for_branch(d, '${SRCREV_ccsp_gwprovapp}')}"
SRCREV:pn-ccsp-gwprovapp-ethwan = "${@srcrev_for_branch(d, '${SRCREV_ccsp_gwprovapp_ethwan}')}"
SRCREV:pn-ccsp-home-security = "${@srcrev_for_branch(d, '${SRCREV_ccsp_home_security}')}"
SRCREV:pn-ccsp-hotspot = "${@srcrev_for_branch(d, '${SRCREV_ccsp_hotspot}')}"
SRCREV:pn-ccsp-hotspot-kmod = "${@srcrev_for_branch(d, '${SRCREV_ccsp_hotspot_kmod}')}"
SRCREV:pn-ccsp-lm-lite = "${@srcrev_for_branch(d, '${SRCREV_ccsp_lm_lite}')}"
SRCREV:pn-ccsp-misc = "${@srcrev_for_branch(d, '${SRCREV_ccsp_misc}')}"
SRCREV:pn-ccsp-moca = "${@srcrev_for_branch(d, '${SRCREV_ccsp_moca}')}"
SRCREV:pn-ccsp-mta-agent = "${@srcrev_for_branch(d, '${SRCREV_ccsp_mta_agent}')}"
SRCREV:pn-ccsp-p-and-m = "${@srcrev_for_branch(d, '${SRCREV_ccsp_p_and_m}')}"
SRCREV_CcspPsm:pn-ccsp-psm = "${@srcrev_for_branch(d, '${SRCREV_ccsp_psm}')}"
SRCREV:pn-ccsp-snmp-pa = "${@srcrev_for_branch(d, '${SRCREV_ccsp_snmp_pa}')}"
SRCREV:pn-ccsp-tr069-pa = "${@srcrev_for_branch(d, '${SRCREV_ccsp_tr069_pa}')}"
SRCREV:pn-ccsp-webui-bci-jst = "${@srcrev_for_branch(d, '${SRCREV_ccsp_webui_bci_jst}')}"
SRCREV:pn-ccsp-webui-bci-php = "${@srcrev_for_branch(d, '${SRCREV_ccsp_webui_bci_php}')}"
SRCREV:pn-ccsp-webui-jst = "${@srcrev_for_branch(d, '${SRCREV_ccsp_webui_jst}')}"
SRCREV:pn-ccsp-webui-php = "${@srcrev_for_branch(d, '${SRCREV_ccsp_webui_php}')}"
SRCREV:pn-ccsp-xconf = "${@srcrev_for_branch(d, '${SRCREV_ccsp_xconf}')}"
SRCREV:pn-ccsp-xdns = "${@srcrev_for_branch(d, '${SRCREV_ccsp_xdns}')}"
SRCREV:pn-core-net-lib = "${@srcrev_for_branch(d, '${SRCREV_core_net_lib}')}"
SRCREV:pn-halinterface = "${@srcrev_for_branch(d, '${SRCREV_halinterface}')}"
SRCREV_bridgeutilhal:pn-hal-bridgeutil = "${@srcrev_for_branch(d, '${SRCREV_hardware_abstraction_layer}')}"
SRCREV_cmhal:pn-hal-cm-generic = "${@srcrev_for_branch(d, '${SRCREV_hardware_abstraction_layer}')}"
SRCREV_dhcpv4hal:pn-hal-dhcpv4c-generic = "${@srcrev_for_branch(d, '${SRCREV_hardware_abstraction_layer}')}"
SRCREV_eponhal:pn-hal-epon-generic = "${@srcrev_for_branch(d, '${SRCREV_hardware_abstraction_layer}')}"
SRCREV_ethswhal:pn-hal-ethsw-generic = "${@srcrev_for_branch(d, '${SRCREV_hardware_abstraction_layer}')}"
SRCREV_firewallhal:pn-hal-firewall-generic = "${@srcrev_for_branch(d, '${SRCREV_hardware_abstraction_layer}')}"
SRCREV_mocahal:pn-hal-moca-generic = "${@srcrev_for_branch(d, '${SRCREV_hardware_abstraction_layer}')}"
SRCREV_msomgmthal:pn-hal-mso-mgmt-generic = "${@srcrev_for_branch(d, '${SRCREV_hardware_abstraction_layer}')}"
SRCREV_mtahal:pn-hal-mta-generic = "${@srcrev_for_branch(d, '${SRCREV_hardware_abstraction_layer}')}"
SRCREV_platformhal:pn-hal-platform-generic = "${@srcrev_for_branch(d, '${SRCREV_hardware_abstraction_layer}')}"
SRCREV_vlanhal:pn-hal-vlan-generic = "${@srcrev_for_branch(d, '${SRCREV_hardware_abstraction_layer}')}"
SRCREV_wifihal:pn-hal-wifi-generic = "${@srcrev_for_branch(d, '${SRCREV_hardware_abstraction_layer}')}"
SRCREV:pn-harvester = "${@srcrev_for_branch(d, '${SRCREV_harvester}')}"
SRCREV:pn-json-hal-lib = "${@srcrev_for_branch(d, '${SRCREV_json_hal_lib}')}"
SRCREV:pn-jst = "${@srcrev_for_branch(d, '${SRCREV_jst}')}"
SRCREV:pn-lanmanager = "${@srcrev_for_branch(d, '${SRCREV_lanmanager}')}"
SRCREV:pn-notify-comp = "${@srcrev_for_branch(d, '${SRCREV_notify_comp}')}"
SRCREV:pn-ovs-agent = "${@srcrev_for_branch(d, '${SRCREV_ovs_agent}')}"
SRCREV:pn-powermgr = "${@srcrev_for_branch(d, '${SRCREV_powermgr}')}"
SRCREV:pn-rdk-fwupgrade-manager = "${@srcrev_for_branch(d, '${SRCREV_rdk_fwupgrade_manager}')}"
SRCREV:pn-rdk-cellularmanager = "${@srcrev_for_branch(d, '${SRCREV_rdk_cellularmanager}')}"
SRCREV:pn-start-parodus = "${@srcrev_for_branch(d, '${SRCREV_start_parodus}')}"
SRCREV:pn-test-and-diagnostic = "${@srcrev_for_branch(d, '${SRCREV_test_and_diagnostic}')}"
SRCREV:pn-utopia = "${@srcrev_for_branch(d, '${SRCREV_utopia}')}"
SRCREV_sysintbroadband:pn-sysint-broadband = "${@srcrev_for_branch(d, '${SRCREV_sysint-broadband}')}"
