package com.template;

import com.google.common.collect.ImmutableList;
import com.template.flows.PatientSendInfoInitiator;
import com.template.flows.PatientSendInfoResponder;
import com.template.flows.Responder;
import net.corda.core.concurrent.CordaFuture;
import net.corda.core.contracts.TransactionState;
import net.corda.core.identity.CordaX500Name;
import net.corda.core.transactions.SignedTransaction;
import net.corda.testing.core.TestIdentity;
import net.corda.testing.node.MockNetwork;
import net.corda.testing.node.MockNetworkParameters;
import net.corda.testing.node.StartedMockNode;
import net.corda.testing.node.TestCordapp;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class FlowTests {
    private MockNetwork network;
    private StartedMockNode a, b, c;
    private final TestIdentity charlie = new TestIdentity(new CordaX500Name("Charlie", "", "GB"));
    private final TestIdentity jorge = new TestIdentity(new CordaX500Name("Jorge", "", "GB"));
    private final TestIdentity marc = new TestIdentity(new CordaX500Name("Marc", "", "GB"));

    @Before
    public void setup() {
        network = new MockNetwork(new MockNetworkParameters().withCordappsForAllNodes(ImmutableList.of(
                TestCordapp.findCordapp("com.template.contracts"),
                TestCordapp.findCordapp("com.template.flows"))));
        a = network.createPartyNode(null);
        b = network.createPartyNode(null);
        c = network.createPartyNode(null);
        ArrayList<StartedMockNode> startedNodes = new ArrayList<>();
        startedNodes.add(a);
        startedNodes.add(b);
        startedNodes.add(c);
//
//        // For real nodes this happens automatically, but we have to manually register the flow for tests.
        for (StartedMockNode node : ImmutableList.of(a, b, c)) {
            node.registerInitiatedFlow(PatientSendInfoResponder.class);
            // other responders here
//
        }
        network.runNetwork();
    }

    @After
    public void tearDown() {
        network.stopNodes();
    }

    @Test
    public void dummyTest() {

    }
    // todo: appropriate tests from https://github.com/corda/bootcamp-cordapp/blob/v4/src/test/java/bootcamp/FlowTests.java and https://docs.corda.net/docs/corda-os/4.7/flow-testing.html

    @Test
    public void transactionConstructedByFlowUsesTheCorrectNotary() throws Exception {
        PatientSendInfoInitiator flow = new PatientSendInfoInitiator("Marc", "Alejandro", 0, false, null, null, null, null, a.getInfo().getLegalIdentities().get(0), b.getInfo().getLegalIdentities().get(0), c.getInfo().getLegalIdentities().get(0));
        CordaFuture<SignedTransaction> future = a.startFlow(flow);
        network.runNetwork();
        SignedTransaction signedTransaction = future.get();

        assertEquals(1, signedTransaction.getTx().getOutputStates().size());
        TransactionState output = signedTransaction.getTx().getOutputs().get(0);

        assertEquals(network.getNotaryNodes().get(0).getInfo().getLegalIdentities().get(0), output.getNotary());
    }

    @Test
    public void sendInfoToDoctor() {
        // Patient
    }

    @Test
    public void firstDoseApproval() {
        // pfizer
    }

    @Test
    public void firstDose() {

    }

    @Test
    public void secondDoseApproval() {
        // pfizer
    }

    @Test
    public void secondDose() {

    }

    @Test
    public void sendInfoToEmployer() {

    }

    @Test
    public void employerApproval() {

    }




    @Test
    public void singleDose() {
        // johnson && johnson

    }





    /*
    TODO:
        Network setup
        Test for a usual two-dose vaccination, with employer
            - register patient
            - flow to doctor
            - have doctor approve
            - flow to patient
            - first dose
            - flow to patient
            - second dose
            - flow to patient
            - have employer approve
            - assert processComplete == true
        Test for a usual one-dose vaccination, with employer
            - register patient
            - flow to doctor
            - have doctor approve
            - flow to patient
            - dose
            - flow to patient
            - have employer approve
            - assert processComplete == true
        Test for a employer approval without dose
            - register patient
            - have doctor approve
            - have employer approve
            - throw: "Your employee has not been vaccinated yet."
            - assert processComplete == false.
        Test for a usual two-dose vaccination, without employer
     */



}
