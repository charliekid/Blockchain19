package com.template.webserver;

import com.template.contracts.PatientContract;
import com.template.flows.*;
import com.template.states.PatientInfoState;
import net.corda.client.rpc.CordaRPCClient;
import net.corda.client.rpc.CordaRPCConnection;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.messaging.CordaRPCOps;
import net.corda.core.utilities.NetworkHostAndPort;
import org.hibernate.annotations.ParamDef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import net.corda.core.identity.Party;

import java.text.ParseException;
import java.util.List;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Define your API endpoints here.
 */
@RestController
@RequestMapping("/") // The paths for HTTP requests are relative to this base path.
public class Controller {
    private final CordaRPCOps proxy;
    private final static Logger logger = LoggerFactory.getLogger(Controller.class);

    public Controller(NodeRPCConnection rpc) {
        this.proxy = rpc.proxy;
    }


    /**
     * POST - Used for a clinic admin to approve the first dosage administered to a patient
     * @param firstName - first name of the patient
     * @param lastName - last name of the patient
     * @param mfrName - manufacturer of the vaccine
     * @param doseNumber - which dose number is being administered
     * @param dateVaccinated - date the vaccination occurred
     * @param lotNumber - lot number of the vaccine
     * @param username - the username of the party submitting this transaction
     * @return
     * @throws ParseException TODO: Jorge please take care of this parse exception you created
     * Created by Jorge and Charlie
     */
    @PostMapping("clinicAdminFirstApproval")
    public String approval(@RequestHeader String firstName, @RequestHeader String lastName,@RequestHeader String mfrName,
                           @RequestHeader int doseNumber, @RequestHeader String dateVaccinated, @RequestHeader String lotNumber,
                           @RequestHeader String username ) throws ParseException {
        CordaRPCOps activeParty = connectNodeViaRPC(username);

        // We need to get all the parties identies.
        Party patientNode = connectNodeViaRPC("Patient1").nodeInfo().getLegalIdentities().get(0);
        Party doctorNodes = connectNodeViaRPC("Doctor1").nodeInfo().getLegalIdentities().get(0);
        Party employerNode = connectNodeViaRPC("Employer1").nodeInfo().getLegalIdentities().get(0);
        Party clinicAdmin1 = connectNodeViaRPC("ClinicAdmin1").nodeInfo().getLegalIdentities().get(0);

        // Parse the date
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date vaccinatedDate = df.parse(dateVaccinated);

        // Start the transaction and get other parties to signed
        activeParty.startFlowDynamic(AdministerFirstDoseInitiator.class, firstName, lastName, doseNumber,
                true, vaccinatedDate, lotNumber, mfrName,
                new Date(0000-00-00), "none", "none",
                false, patientNode, doctorNodes, employerNode, clinicAdmin1);
        return "Patient: " + firstName + "'s" + " #" + doseNumber + " was registered in the system";

    }

    /**
     * POST - Used for a clinic admin to approve the second dosage administered to a patient
     * @param firstName first name of the patient
     * @param lastName - last name of the patient
     * @param mfrName - manufacturer of the vaccine
     * @param firstDate - date of the first vaccine admin
     * @param lotOne - lot number of the first vaccine
     * @param secDate - date of the second vaccine admin
     * @param secLot - lot number of the second vaccine
     * @param username - the username of the party submitting this transaction
     * @param doseNumber - which dose number is being administered
     * @return
     * @throws ParseException TODO: Jorge please take care of this parse exception you created
     * Created by Jorge and Charlie
     */
    @PostMapping("clinicAdminSecondApproval")
    public String approval(@RequestHeader String firstName, @RequestHeader String lastName,@RequestHeader String mfrName,
                           @RequestHeader String firstDate, @RequestHeader String lotOne, @RequestHeader String secDate,
                           @RequestHeader String secLot, @RequestHeader String username, @RequestHeader int doseNumber ) throws ParseException {
        CordaRPCOps activeParty = connectNodeViaRPC(username);
        // We need to get all the parties identies.
        Party patientNode = connectNodeViaRPC("Patient1").nodeInfo().getLegalIdentities().get(0);
        Party doctorNodes = connectNodeViaRPC("Doctor1").nodeInfo().getLegalIdentities().get(0);
        Party employerNode = connectNodeViaRPC("Employer1").nodeInfo().getLegalIdentities().get(0);
        Party clinicAdmin1 = connectNodeViaRPC("ClinicAdmin1").nodeInfo().getLegalIdentities().get(0);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date firDate = df.parse(firstDate);
        Date secondDate = df.parse(secDate);

        // Start the transaction and get other parties to signed
        activeParty.startFlowDynamic(AdministerSecondDoseInitiator.class, firstName, lastName, doseNumber,true,firDate, lotOne,mfrName,
                secondDate,secLot,mfrName,true,patientNode, doctorNodes,employerNode,clinicAdmin1);

        return "Patient: " + firstName + "'s" + " #" + doseNumber + " was registered in the system";
    }

    // just added this. delete or modify as needed -- marc
    @PostMapping("clinicAdminWorkApproval")
    public String approval(@RequestHeader String firstName, @RequestHeader String lastName,@RequestHeader String mfrName,
                           @RequestHeader String firstDate, @RequestHeader String lotOne, @RequestHeader String secDate,
                           @RequestHeader String secLot, @RequestHeader String username, @RequestHeader int doseNumber, @RequestHeader boolean vaccinationProcessComplete ) throws ParseException {
        CordaRPCOps activeParty = connectNodeViaRPC(username);
        // We need to get all the parties identies.
        Party patientNode = connectNodeViaRPC("Patient1").nodeInfo().getLegalIdentities().get(0);
        Party doctorNodes = connectNodeViaRPC("Doctor1").nodeInfo().getLegalIdentities().get(0);
        Party employerNode = connectNodeViaRPC("Employer1").nodeInfo().getLegalIdentities().get(0);
        Party clinicAdmin1 = connectNodeViaRPC("ClinicAdmin1").nodeInfo().getLegalIdentities().get(0);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date firDate = df.parse(firstDate);
        Date secondDate = df.parse(secDate);

        // gonna go and replace the firstdose with the second dose
        activeParty.startFlowDynamic(ApprovePatientForWorkInitiator.class, firstName, lastName, doseNumber,true,firDate, lotOne,mfrName,
                secondDate,secLot,mfrName,true,patientNode, doctorNodes,employerNode,clinicAdmin1);

        return "Patient: " + firstName + "'s" + " #" + doseNumber + " was registered in the system";
    }

    /**
     * POST - register a patient to be vaccinated
     * @param firstName - first name of the patient
     * @param lastName - last name of the patient
     * @param dose - number of vaccines
     * @param username - the username of the party submitting this transaction
     * @return
     */
    @PostMapping("registerVaccine")
    public String registerVaccine(@RequestHeader String firstName, @RequestHeader String lastName, @RequestHeader int dose, @RequestHeader String username){
        CordaRPCOps activeParty = connectNodeViaRPC(username);
        // We need to get all the parties identies.
        Party patientNode = connectNodeViaRPC("Patient1").nodeInfo().getLegalIdentities().get(0);
        Party doctorNodes = connectNodeViaRPC("Doctor1").nodeInfo().getLegalIdentities().get(0);
        Party employerNode = connectNodeViaRPC("Employer1").nodeInfo().getLegalIdentities().get(0);
        Party clinicAdmin1 = connectNodeViaRPC("ClinicAdmin1").nodeInfo().getLegalIdentities().get(0);

        // Start the transaction and get other parties to signed
        activeParty.startFlowDynamic(PatientSendInfoInitiator.class, firstName, lastName, 0, false,
                new Date(0000-00-00), "none", "none",
                new Date(0000-00-00), "none", "none",
                false, patientNode, doctorNodes, employerNode, clinicAdmin1);
        return "Hi," + firstName + " " + lastName ;

    }

    /**
     * Approve the patient so that the clinic admin can begin to admin vaccines
     * @param firstName - first name of a patient
     * @param lastName - last name of patient
     * @param username - the username of the party submitting this transaction
     * @return
     */
    @PostMapping("approvePatient")
    public String approvePatient(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String username){
        CordaRPCOps activeParty = connectNodeViaRPC(username);
        // We need to get all the parties identities.
        Party patientNode = connectNodeViaRPC("Patient1").nodeInfo().getLegalIdentities().get(0);
        Party doctorNodes = connectNodeViaRPC("Doctor1").nodeInfo().getLegalIdentities().get(0);
        Party employerNode = connectNodeViaRPC("Employer1").nodeInfo().getLegalIdentities().get(0);
        Party clinicAdmin1 = connectNodeViaRPC("ClinicAdmin1").nodeInfo().getLegalIdentities().get(0);

        // Start the transaction and get other parties to signed
        activeParty.startFlowDynamic(ApprovePatientInitiator.class, firstName, lastName, 0, true,
                new Date(0000-00-00), "none", "none",
                new Date(0000-00-00), "none", "none",
                false, patientNode, doctorNodes, employerNode, clinicAdmin1);

        return "patient is now approved!";

    }

    /**
     * This will get a list of transaction associated with the username
     * @param username - the username of the party submitting this transaction
     * @return
     */
    @GetMapping("transaction/list/{username}")
    public APIResponse<List<StateAndRef<PatientInfoState>>> getAssetList(@PathVariable String username){
        CordaRPCOps activeParty = connectNodeViaRPC(username);
        try{
            List<StateAndRef<PatientInfoState>> assetList = activeParty.vaultQuery(PatientInfoState.class).getStates();
            return APIResponse.success(assetList);
        }catch(Exception e){
            System.out.println("ERROR in ASSET/LIST");
            return APIResponse.error(e.getMessage());
        }
    }

    /***********************************************************************************************************
     *                                              HELPER FUNCTIONS
     ***********************************************************************************************************/
    /**
     * Gets the port address for the retrospective parties
     * This should get updated as you put in more nodes and specify its ports
     * @param partyName
     * @return
     * By charlie
     */
    private static int getPortAddress(String partyName) {
        if(partyName.equals("Patient1")) {
            return 10006;
        } else if (partyName.equals("Doctor1")) {
            return 10009;
        } else if (partyName.equals("Employer1")) {
            return 10007;
        } else if (partyName.equals("ClinicAdmin1")) {
            return 10010;
        } else
            return 0;
    }

    /**
     * connects to a node using RPC client. You just need to past in its name
     * TODO:currently i have hardcoded the username and password. must find better way to do this.
     * @param partyName
     * @return
     * By Charlie
     */
    public static CordaRPCOps connectNodeViaRPC(String partyName) {
        int port = getPortAddress(partyName);
        String host = "localhost";
        String username = "";

            if (partyName.equals("ClinicAdmin1")) {
                username = "ClinicAdmin1";
            } else {
                username = "user1";
            }
            String password = "test";
            NetworkHostAndPort nodeAddress = new NetworkHostAndPort(host, port);

            // Sets up the connection to our node with the specified
            // we prolly might want to do a try catch here for when some enters in the wrong user name maybe
            CordaRPCClient client = new CordaRPCClient(nodeAddress);
            CordaRPCConnection connection = client.start(username, password);
            CordaRPCOps cordaRPCOperations = connection.getProxy();
            return cordaRPCOperations;
    }

}