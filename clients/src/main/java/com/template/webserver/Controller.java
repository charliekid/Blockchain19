package com.template.webserver;

import com.template.states.PatientInfoState;
import net.corda.client.rpc.CordaRPCClient;
import net.corda.client.rpc.CordaRPCConnection;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.messaging.CordaRPCOps;
import net.corda.core.utilities.NetworkHostAndPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * Gets the asset list pertaining to the party. In this case, this is all the transaction that
     * is viewable by the party.
     * @return
     */
    @GetMapping("transaction/list")
    public APIResponse<List<StateAndRef<PatientInfoState>>> getAssetList(){
        NetworkHostAndPort nodeAddress = new NetworkHostAndPort("localhost", 10006);

        // Sets up the connection to our node with the specified
        // we prolly might want to do a try catch here for when some enters in the wrong user name maybe
        CordaRPCClient client = new CordaRPCClient(nodeAddress);
        CordaRPCConnection connection = client.start("user1", "test");
        CordaRPCOps activeParty = connection.getProxy();
        try{
            List<StateAndRef<PatientInfoState>> assetList = activeParty.vaultQuery(PatientInfoState.class).getStates();
            System.out.println("asset list " + assetList);
            return APIResponse.success(assetList);
        }catch(Exception e){
            System.out.println("ERROR in ASSET/LIST");
            return APIResponse.error(e.getMessage());
        }
    }

}