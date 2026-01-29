# For custom build verification in sprint:
# Usage Example:
#     update BRANCH_ccsp_adv_security = "nobranch=1"
#     and update commit ID of ccsp_adv_security in generic-srcrev.inc
#         Example: SRCREV_pn-ccsp-adv-security = "1234abcd"
# (or)
#     Update feature branch for specific component here,
#         Example: BRANCH_ccsp_adv_security = "branch=feature/RDKB-12345"

def branch_update(d):
    branch = d.getVar('PROJECT_BRANCH', True) or ''
    if 'sprint' in branch:
        return 'branch=develop'
    return 'nobranch=1'

BRANCH_ccsp_adv_security = "${@branch_update(d)}"
BRANCH_ccsp_cm_agent = "${@branch_update(d)}"
BRANCH_ccsp_common_library = "${@branch_update(d)}"
BRANCH_ccsp_cr = "${@branch_update(d)}"
BRANCH_ccsp_dmcli = "${@branch_update(d)}"
BRANCH_ccsp_eth_agent = "${@branch_update(d)}"
BRANCH_ccsp_gwprovapp = "${@branch_update(d)}"
BRANCH_ccsp_gwprovapp_ethwan = "${@branch_update(d)}"
BRANCH_ccsp_home_security = "${@branch_update(d)}"
BRANCH_ccsp_hotspot = "${@branch_update(d)}"
BRANCH_ccsp_hotspot_kmod = "${@branch_update(d)}"
BRANCH_ccsp_lm_lite = "${@branch_update(d)}"
BRANCH_ccsp_misc = "${@branch_update(d)}"
BRANCH_ccsp_moca = "${@branch_update(d)}"
BRANCH_ccsp_mta_agent = "${@branch_update(d)}"
BRANCH_ccsp_p_and_m = "${@branch_update(d)}"
BRANCH_ccsp_psm = "${@branch_update(d)}"
BRANCH_ccsp_snmp_pa = "${@branch_update(d)}"
BRANCH_ccsp_tr069_pa = "${@branch_update(d)}"
BRANCH_ccsp_webui_bci_jst = "${@branch_update(d)}"
BRANCH_ccsp_webui_bci_php = "${@branch_update(d)}"
BRANCH_ccsp_webui_jst = "${@branch_update(d)}"
BRANCH_ccsp_webui_php = "${@branch_update(d)}"
BRANCH_ccsp_xconf = "${@branch_update(d)}"
BRANCH_ccsp_xdns = "${@branch_update(d)}"
BRANCH_core_net_lib = "${@branch_update(d)}"
BRANCH_halinterface = "${@branch_update(d)}"
BRANCH_hal_bridgeutil = "${@branch_update(d)}"
BRANCH_hardware_abstraction_layer = "${@branch_update(d)}"
BRANCH_harvester = "${@branch_update(d)}"
BRANCH_json_hal_lib = "${@branch_update(d)}"
BRANCH_jst = "${@branch_update(d)}"
BRANCH_lanmanager = "${@branch_update(d)}"
BRANCH_notify_comp = "${@branch_update(d)}"
BRANCH_ovs_agent = "${@branch_update(d)}"
BRANCH_powermgr = "${@branch_update(d)}"
BRANCH_rdk_fwupgrade_manager = "${@branch_update(d)}"
BRANCH_rdk_cellularmanager = "${@branch_update(d)}"
BRANCH_start_parodus = "${@branch_update(d)}"
BRANCH_test_and_diagnostic = "${@branch_update(d)}"
BRANCH_utopia = "${@branch_update(d)}"
BRANCH_sysint-broadband = "${@branch_update(d)}"
