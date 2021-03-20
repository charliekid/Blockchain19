package com.template.webserver;
import com.template.states.PatientInfoState;
import net.corda.client.rpc.CordaRPCClient;
import net.corda.client.rpc.CordaRPCConnection;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.messaging.CordaRPCOps;
import net.corda.core.utilities.NetworkHostAndPort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/login")
public class LoginController {
    @RequestMapping(value="/", method = RequestMethod.GET)
    public void getStates() {

    }

    @GetMapping("/assetList")
    public APIResponse<List<StateAndRef<PatientInfoState>>> getAssetList(){
        // Lets assume we got the local host and node address somehow
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
            return APIResponse.error(e.getMessage());
        }
    }


}
