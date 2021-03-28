package com.template.webserver;

import com.template.states.PatientInfoState;
import net.corda.client.rpc.CordaRPCClient;
import net.corda.client.rpc.CordaRPCConnection;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.messaging.CordaRPCOps;
import net.corda.core.utilities.NetworkHostAndPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping(value = "/templateendpoint", produces = "text/plain")
    private String templateendpoint() {
        return "Define an endpoint here.";
    }

    /**
     * Gets all the transactions that are in the specified party
     * @param partyid - the partyid that correspond to the username in the db
     * @return a json file with all the data within the node.
     */
    @GetMapping("transaction/list/{partyid}")
    public APIResponse<List<StateAndRef<PatientInfoState>>> getAssetList(@PathVariable String partyid){
        System.out.println("partyid: " + partyid);
        CordaRPCOps activeParty = connectNodeViaRPC(partyid);
        try{
            List<StateAndRef<PatientInfoState>> assetList = activeParty.vaultQuery(PatientInfoState.class).getStates();
            System.out.println("asset list " + assetList);
            return APIResponse.success(assetList);
        }catch(Exception e){
            System.out.println("ERROR in ASSET/LIST");
            return APIResponse.error(e.getMessage());
        }
    }



    @GetMapping("/testingGet/{partyid}")
    public String getAsset(@PathVariable String partyid) {
        return partyid;
    }

    @PostMapping("/login")
    // @RequestHeader String username, @RequestHeader String password
    public String postLogin(@RequestBody String username ){
        return "we are in a post /login " + username;
    }

    @PostMapping("/aPost")
    public String apost(){
        return "we are in a post";
    }
    /***********************************************************************************************************
     *                                              HELPER FUNCTIONS
     ***********************************************************************************************************/
    /**
     * Gets the port address for the retrospective parties
     * This should get updated as you put in more nodes and specify its ports
     * @param partyName
     * @return
     */
    private static int getPortAddress(String partyName) {
        if(partyName.equals("Patient1")) {
            return 10006;
        } else if (partyName.equals("Doctor1")) {
            return 10009;
        } else if (partyName.equals("Employer1")) {
            return 10007;
        } else
            return 0;
    }

    /**
     * connects to a node using RPC client. You just need to past in its name
     * TODO:currently i have hardcoded the username and password. must find better way to do this.
     * @param partyName
     * @return
     */
    private static CordaRPCOps connectNodeViaRPC(String partyName) {
        int port = getPortAddress(partyName);
        String host = "localhost";
        String username = "user1";
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