package com.template;

import com.google.common.collect.ImmutableList;
import net.corda.core.concurrent.CordaFuture;
import net.corda.core.identity.CordaX500Name;
import net.corda.core.identity.Party;
import net.corda.testing.core.TestIdentity;
import net.corda.testing.driver.DriverParameters;
import net.corda.testing.driver.NodeHandle;
import net.corda.testing.driver.NodeParameters;
import org.junit.Test;

import java.util.List;

import static net.corda.testing.driver.Driver.driver;
import static org.junit.Assert.assertEquals;

public class DriverBasedTest {
    private final TestIdentity bankA = new TestIdentity(new CordaX500Name("Charlie", "Marina", "US"));
    private final TestIdentity bankB = new TestIdentity(new CordaX500Name("Jorge", "Monterey", "US"));
    private final TestIdentity bankC = new TestIdentity(new CordaX500Name("Marc", "Seaside", "US"));
    private final TestIdentity bankD = new TestIdentity(new CordaX500Name("Jonathan", "Seaside", "US"));

    @Test
    public void nodeTest() { // appropriated from https://github.com/corda/bootcamp-cordapp/blob/v4/src/test/java/bootcamp/IntegrationTest.java
        driver(new DriverParameters().withIsDebug(true).withStartNodesInProcess(true), dsl -> {
            // Start a pair of nodes and wait for them both to be ready.
            List<CordaFuture<NodeHandle>> handleFutures = ImmutableList.of(
                    dsl.startNode(new NodeParameters().withProvidedName(bankA.getName())),
                    dsl.startNode(new NodeParameters().withProvidedName(bankB.getName())),
                    dsl.startNode(new NodeParameters().withProvidedName(bankC.getName())),
                    dsl.startNode(new NodeParameters().withProvidedName(bankD.getName()))
            );

            try {
                NodeHandle partyAHandle = handleFutures.get(0).get();
                NodeHandle partyBHandle = handleFutures.get(1).get();
                NodeHandle partyCHandle = handleFutures.get(2).get();
                NodeHandle partyDHandle = handleFutures.get(3).get();

                Party partyA = partyAHandle.getNodeInfo().getLegalIdentities().get(0);
                Party partyB = partyBHandle.getNodeInfo().getLegalIdentities().get(0);

                // From each node, make an RPC call to retrieve another node's name from the network map, to verify that the
                // nodes have started and can communicate.

                // This is a very basic test: in practice tests would be starting flows, and verifying the states in the vault
                // and other important metrics to ensure that your CorDapp is working as intended.
                assertEquals(partyAHandle.getRpc().wellKnownPartyFromX500Name(bankB.getName()).getName(), bankB.getName());
                assertEquals(partyBHandle.getRpc().wellKnownPartyFromX500Name(bankA.getName()).getName(), bankA.getName());
            } catch (Exception e) {
                throw new RuntimeException("Caught exception during test: ", e);
            }

            return null;
        });
    }
}