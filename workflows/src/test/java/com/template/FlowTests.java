package com.template;

import com.google.common.collect.ImmutableList;
import com.template.contracts.PatientContract;
import com.template.flows.PatientSendInfoInitiator;
import com.template.flows.PatientSendInfoResponder;
import com.template.flows.Responder;
import com.template.states.PatientInfoState;
import net.corda.core.concurrent.CordaFuture;
import net.corda.core.contracts.Command;
import net.corda.core.contracts.TransactionState;
import net.corda.core.identity.CordaX500Name;
import net.corda.core.identity.Party;
import net.corda.core.transactions.SignedTransaction;
import net.corda.testing.core.TestIdentity;
import net.corda.testing.node.MockNetwork;
import net.corda.testing.node.MockNetworkParameters;
import net.corda.testing.node.StartedMockNode;
import net.corda.testing.node.TestCordapp;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.*;

public class FlowTests {
    private MockNetwork network;
    private StartedMockNode a, b, c, d;

    // im not sure if we use those or not, but we will leave those here just in case.
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
        d = network.createPartyNode(null);
        ArrayList<StartedMockNode> startedNodes = new ArrayList<>();
        startedNodes.add(a);
        startedNodes.add(b);
        startedNodes.add(c);
        startedNodes.add(d);
//
//        // For real nodes this happens automatically, but we have to manually register the flow for tests.
        for (StartedMockNode node : ImmutableList.of(a, b, c, d)) {
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
    // tests appropriated from from https://github.com/corda/bootcamp-cordapp/blob/v4/src/test/java/bootcamp/FlowTests.java and https://docs.corda.net/docs/corda-os/4.7/flow-testing.html

    @Test
    public void transactionConstructedByFlowUsesTheCorrectNotary() throws Exception {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        Date placeholder = new Date();

        try {
            placeholder = parser.parse("0000-00-00");

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        PatientSendInfoInitiator flow = new PatientSendInfoInitiator("marc",
                "alejandro",
                0,
                false,
                placeholder,
                "none",
                "none",
                placeholder,
                "none",
                "none",
                false,
                a.getInfo().getLegalIdentities().get(0),
                b.getInfo().getLegalIdentities().get(0),
                c.getInfo().getLegalIdentities().get(0),
                d.getInfo().getLegalIdentities().get(0));

        CordaFuture<SignedTransaction> future = a.startFlow(flow);
        network.runNetwork();
        SignedTransaction signedTransaction = future.get();

        assertEquals(1, signedTransaction.getTx().getOutputStates().size());
        TransactionState output = signedTransaction.getTx().getOutputs().get(0);

        assertEquals(network.getNotaryNodes().get(0).getInfo().getLegalIdentities().get(0), output.getNotary());
    }

    @Test
    public void transactionConstructedByFlowHasOneTokenStateOutputWithTheCorrectAmountAndOwner() throws Exception {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        Date placeholder = new Date();

        try {
            placeholder = parser.parse("0000-00-00");

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        PatientSendInfoInitiator flow = new PatientSendInfoInitiator("marc",
                "alejandro",
                0,
                false,
                placeholder,
                "none",
                "none",
                placeholder,
                "none",
                "none",
                false,
                a.getInfo().getLegalIdentities().get(0),
                b.getInfo().getLegalIdentities().get(0),
                c.getInfo().getLegalIdentities().get(0),
                d.getInfo().getLegalIdentities().get(0));

        CordaFuture<SignedTransaction> future = a.startFlow(flow);
        network.runNetwork();
        SignedTransaction signedTransaction = future.get();

        assertEquals(1, signedTransaction.getTx().getOutputStates().size());
        PatientInfoState output = signedTransaction.getTx().outputsOfType(PatientInfoState.class).get(0);

        assertEquals(b.getInfo().getLegalIdentities().get(0), output.getDoctor());
        assertEquals(0, output.getDose());
    }


    @Test
    public void transactionConstructedByFlowHasOneOutputUsingTheCorrectContract() throws Exception {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        Date placeholder = new Date();

        try {
            placeholder = parser.parse("0000-00-00");

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        PatientSendInfoInitiator flow = new PatientSendInfoInitiator("marc",
                "alejandro",
                0,
                false,
                placeholder,
                "none",
                "none",
                placeholder,
                "none",
                "none",
                false,
                a.getInfo().getLegalIdentities().get(0),
                b.getInfo().getLegalIdentities().get(0),
                c.getInfo().getLegalIdentities().get(0),
                d.getInfo().getLegalIdentities().get(0));

        CordaFuture<SignedTransaction> future = a.startFlow(flow);
        network.runNetwork();
        SignedTransaction signedTransaction = future.get();

        assertEquals(1, signedTransaction.getTx().getOutputStates().size());
        TransactionState output = signedTransaction.getTx().getOutputs().get(0);

        assertEquals("com.template.contracts.PatientContract", output.getContract()); // todo: this
    }

    @Test
    public void transactionConstructedByFlowHasOneIssueCommand() throws Exception {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        Date placeholder = new Date();

        try {
            placeholder = parser.parse("0000-00-00");

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        PatientSendInfoInitiator flow = new PatientSendInfoInitiator("marc",
                "alejandro",
                0,
                false,
                placeholder,
                "none",
                "none",
                placeholder,
                "none",
                "none",
                false,
                a.getInfo().getLegalIdentities().get(0),
                b.getInfo().getLegalIdentities().get(0),
                c.getInfo().getLegalIdentities().get(0),
                d.getInfo().getLegalIdentities().get(0));

        CordaFuture<SignedTransaction> future = a.startFlow(flow);
        network.runNetwork();
        SignedTransaction signedTransaction = future.get();

        assertEquals(1, signedTransaction.getTx().getCommands().size());
        Command command = signedTransaction.getTx().getCommands().get(0);

        assert (command.getValue() instanceof PatientContract.Commands.SendInfo);
    }

    @Test
    public void transactionConstructedByFlowHasOneCommandWithTheIssuerAndTheOwnerAsASigners() throws Exception {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        Date placeholder = new Date();

        try {
            placeholder = parser.parse("0000-00-00");

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        PatientSendInfoInitiator flow = new PatientSendInfoInitiator("marc",
                "alejandro",
                0,
                false,
                placeholder,
                "none",
                "none",
                placeholder,
                "none",
                "none",
                false,
                a.getInfo().getLegalIdentities().get(0),
                b.getInfo().getLegalIdentities().get(0),
                c.getInfo().getLegalIdentities().get(0),
                d.getInfo().getLegalIdentities().get(0));

        CordaFuture<SignedTransaction> future = a.startFlow(flow);
        network.runNetwork();
        SignedTransaction signedTransaction = future.get();

        assertEquals(1, signedTransaction.getTx().getCommands().size());
        Command command = signedTransaction.getTx().getCommands().get(0);

        // fails if assertEquals(2, command.getSigners().size());. not sure what to do here
        assertEquals(4, command.getSigners().size());
        assertTrue(command.getSigners().contains(a.getInfo().getLegalIdentities().get(0).getOwningKey()));
        assertTrue(command.getSigners().contains(b.getInfo().getLegalIdentities().get(0).getOwningKey()));
        assertTrue(command.getSigners().contains(c.getInfo().getLegalIdentities().get(0).getOwningKey()));
        assertTrue(command.getSigners().contains(d.getInfo().getLegalIdentities().get(0).getOwningKey()));
    }

    @Test
    public void transactionConstructedByFlowHasNoInputsAttachmentsOrTimeWindows() throws Exception {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        Date placeholder = new Date();

        try {
            placeholder = parser.parse("0000-00-00");

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        PatientSendInfoInitiator flow = new PatientSendInfoInitiator("marc",
                "alejandro",
                0,
                false,
                placeholder,
                "none",
                "none",
                placeholder,
                "none",
                "none",
                false,
                a.getInfo().getLegalIdentities().get(0),
                b.getInfo().getLegalIdentities().get(0),
                c.getInfo().getLegalIdentities().get(0),
                d.getInfo().getLegalIdentities().get(0));
        CordaFuture<SignedTransaction> future = a.startFlow(flow);
        network.runNetwork();
        SignedTransaction signedTransaction = future.get();

        assertEquals(0, signedTransaction.getTx().getInputs().size());
        // The single attachment is the contract attachment.
        assertEquals(1, signedTransaction.getTx().getAttachments().size());
        assertNull(signedTransaction.getTx().getTimeWindow());
    }

    // the following below are original tests for our product.
    @Test
    public void sendInfoToDoctor() throws Exception {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        Date placeholder = new Date();

        try {
            placeholder = parser.parse("0000-00-00");

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        PatientSendInfoInitiator flow = new PatientSendInfoInitiator("marc",
                "alejandro",
                0,
                false,
                placeholder,
                "none",
                "none",
                placeholder,
                "none",
                "none",
                false,
                a.getInfo().getLegalIdentities().get(0),
                b.getInfo().getLegalIdentities().get(0),
                c.getInfo().getLegalIdentities().get(0),
                d.getInfo().getLegalIdentities().get(0));

        Party patient = a.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
        Party doctor = b.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
        Party employer = c.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
        Party clinicAdmin = d.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();

        CordaFuture<SignedTransaction> future = a.startFlow(flow);
        network.runNetwork();
        SignedTransaction signedTransaction = future.get();

//        signedTransaction.getTx().getOutputs().get(0).getData()
        // assert equals some stuf here.
    }



//
//    @Test
//    public void firstDoseApproval() {
//        // pfizer
//    }
//
//    @Test
//    public void firstDose() {
//
//    }
//
//    @Test
//    public void secondDoseApproval() {
//        // pfizer
//    }
//
//    @Test
//    public void secondDose() {
//
//    }
//
//    @Test
//    public void sendInfoToEmployer() {
//
//    }
//
//    @Test
//    public void employerApproval() {
//
//    }
//
//
//
//
//    @Test
//    public void singleDose() {
//        // johnson && johnson
//
//    }





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
