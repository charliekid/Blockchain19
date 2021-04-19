package com.template;

import com.google.common.collect.ImmutableList;
import com.template.contracts.PatientContract;
import com.template.flows.*;
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
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

public class FlowTests {
    private MockNetwork network;
    private StartedMockNode a, b, c, d;

    // im not sure if we use those or not, but we will leave those here just in case.
    private final TestIdentity charlie = new TestIdentity(new CordaX500Name("Charlie", "", "GB"));
    private final TestIdentity jorge = new TestIdentity(new CordaX500Name("Jorge", "", "GB"));
    private final TestIdentity marc = new TestIdentity(new CordaX500Name("Marc", "", "GB"));
    private final TestIdentity jonathan = new TestIdentity(new CordaX500Name("Jonathan", "", "GB"));

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

        // For real nodes this happens automatically, but we have to manually register the flow for tests.
        for (StartedMockNode node : ImmutableList.of(a, b, c, d)) {
            node.registerInitiatedFlow(PatientSendInfoResponder.class);
            node.registerInitiatedFlow(ApprovePatientResponder.class);
            node.registerInitiatedFlow(AdministerFirstDoseResponder.class);
            // other responders here
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

        Party patient = a.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
        Party doctor = b.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
        Party employer = c.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
        Party clinicAdmin = d.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();

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
                patient,
                doctor,
                employer,
                clinicAdmin);


        CordaFuture<SignedTransaction> future = a.startFlow(flow);
        network.runNetwork();
        SignedTransaction signedTransaction = future.get();

//        signedTransaction.getTx().getOutputs().get(0).getData()
        // assert equals some stuff here.
        PatientInfoState outputPatientInfo = (PatientInfoState) signedTransaction.getTx().getOutputs().get(0).getData();
        assertEquals(outputPatientInfo.getDose(), 0);
    }

    @Test(expected = ExecutionException.class)
    public void nonPatientPartySendsInfoToDoctor() throws Exception {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        Date placeholder = new Date();

        try {
            placeholder = parser.parse("0000-00-00");

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        Party patient = a.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
        Party doctor = b.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
        Party employer = c.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
        Party clinicAdmin = d.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();

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
                patient,
                doctor,
                employer,
                clinicAdmin);

        CordaFuture<SignedTransaction> future = b.startFlow(flow);
        network.runNetwork(); // expects a failure
        SignedTransaction signedTransaction = future.get();
    }

    @Test(expected = ExecutionException.class)
    public void sendInfoButThereIsOneDose() throws Exception {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        Date placeholder = new Date();

        try {
            placeholder = parser.parse("0000-00-00");

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        Party patient = a.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
        Party doctor = b.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
        Party employer = c.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
        Party clinicAdmin = d.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
        PatientSendInfoInitiator flow = new PatientSendInfoInitiator("marc",

                "alejandro",
                1,
                false,
                placeholder,
                "none",
                "none",
                placeholder,
                "none",
                "none",
                false,
                patient,
                doctor,
                employer,
                clinicAdmin);

        CordaFuture<SignedTransaction> future = a.startFlow(flow);
        network.runNetwork(); // expects a failure
        SignedTransaction signedTransaction = future.get();
    }

    @Test
    public void sendInfoToDoctorThenApprovePatientForVaccination() throws Exception {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        Date placeholder = new Date();

        try {
            placeholder = parser.parse("0000-00-00");

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        PatientSendInfoInitiator sendInfoFlow = new PatientSendInfoInitiator("marc",
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

        CordaFuture<SignedTransaction> future = a.startFlow(sendInfoFlow);
        network.runNetwork();
        SignedTransaction signedTransaction = future.get();

//        signedTransaction.getTx().getOutputs().get(0).getData()
        // assert equals some stuff here.
        PatientInfoState outputPatientInfo = (PatientInfoState) signedTransaction.getTx().getOutputs().get(0).getData();
        assertEquals(outputPatientInfo.isApprovedForVaccination(), false);

        ApprovePatientInitiator approvePatientFlow = new ApprovePatientInitiator("marc",
                "alejandro",
                0,
                true,
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

        CordaFuture<SignedTransaction> future2 = b.startFlow(approvePatientFlow);
        network.runNetwork();
        SignedTransaction signedTransaction2 = future2.get();


        PatientInfoState outputPatientInfo2 = (PatientInfoState) signedTransaction2.getTx().getOutputs().get(0).getData();
        assertEquals(outputPatientInfo2.isApprovedForVaccination(), true);
    }

    @Test(expected = ExecutionException.class)
    public void sendInfoToDoctorThenApproveNonexistentPatientForVaccination() throws Exception {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        Date placeholder = new Date();

        try {
            placeholder = parser.parse("0000-00-00");

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        PatientSendInfoInitiator sendInfoFlow = new PatientSendInfoInitiator("marc",
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


        CordaFuture<SignedTransaction> future = a.startFlow(sendInfoFlow);
        network.runNetwork();
        SignedTransaction signedTransaction = future.get();

//        signedTransaction.getTx().getOutputs().get(0).getData()
        // assert equals some stuff here.
        PatientInfoState outputPatientInfo = (PatientInfoState) signedTransaction.getTx().getOutputs().get(0).getData();
        assertEquals(outputPatientInfo.isApprovedForVaccination(), false);

        ApprovePatientInitiator approvePatientFlow = new ApprovePatientInitiator("charlie",
                "nguyen",
                0,
                true,
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

        CordaFuture<SignedTransaction> future2 = b.startFlow(approvePatientFlow);
        network.runNetwork();
        SignedTransaction signedTransaction2 = future2.get();
    }

    @Test(expected = ExecutionException.class)
    public void sendInfoToDoctorButSomeoneWhoIsNotADoctorTriesToApprovePatientForVaccination() throws Exception {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        Date placeholder = new Date();

        try {
            placeholder = parser.parse("0000-00-00");

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        PatientSendInfoInitiator sendInfoFlow = new PatientSendInfoInitiator("marc",
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

        CordaFuture<SignedTransaction> future = a.startFlow(sendInfoFlow);
        network.runNetwork();
        SignedTransaction signedTransaction = future.get();

        PatientInfoState outputPatientInfo = (PatientInfoState) signedTransaction.getTx().getOutputs().get(0).getData();
        assertEquals(outputPatientInfo.isApprovedForVaccination(), false);

        ApprovePatientInitiator approvePatientFlow = new ApprovePatientInitiator("marc",
                "alejandro",
                0,
                true,
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

        CordaFuture<SignedTransaction> future2 = a.startFlow(approvePatientFlow);
        network.runNetwork();
        SignedTransaction signedTransaction2 = future2.get();
    }


    @Test
    public void sendInfoApprovePatientThenAdministerFirstDose() throws Exception {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        Date placeholder = new Date();

        try {
            placeholder = parser.parse("0000-00-00");

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        Party patient = a.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
        Party doctor = b.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
        Party employer = c.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
        Party clinicAdmin = d.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();

        PatientSendInfoInitiator sendInfoFlow = new PatientSendInfoInitiator("marc",
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
                patient,
                doctor,
                employer,
                clinicAdmin);

        CordaFuture<SignedTransaction> future = a.startFlow(sendInfoFlow);
        network.runNetwork();
        SignedTransaction signedTransaction = future.get();
        PatientInfoState outputPatientInfo = (PatientInfoState) signedTransaction.getTx().getOutputs().get(0).getData();

        assertEquals(outputPatientInfo.isApprovedForVaccination(), false);

        ApprovePatientInitiator approvePatientFlow = new ApprovePatientInitiator("marc",
                "alejandro",
                0,
                true,
                placeholder,
                "none",
                "none",
                placeholder,
                "none",
                "none",
                false,
                patient,
                doctor,
                employer,
                clinicAdmin);

        CordaFuture<SignedTransaction> future2 = b.startFlow(approvePatientFlow);
        network.runNetwork();
        SignedTransaction signedTransaction2 = future2.get();


        PatientInfoState outputPatientInfo2 = (PatientInfoState) signedTransaction2.getTx().getOutputs().get(0).getData();
        assertEquals(outputPatientInfo2.isApprovedForVaccination(), true);


        Date firstDoseDate = new Date();

        try {
            firstDoseDate = parser.parse("2021-03-03");
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        AdministerFirstDoseInitiator administerFirstDoseFlow = new AdministerFirstDoseInitiator("marc",
                "alejandro",
                1,
                true,
                firstDoseDate,
                "123a45b",
                "moderna",
                placeholder,
                "none",
                "none",
                false,
                patient,
                doctor,
                employer,
                clinicAdmin);

        CordaFuture<SignedTransaction> future3 = d.startFlow(administerFirstDoseFlow);
        network.runNetwork();
        SignedTransaction signedTransaction3 = future3.get();
        PatientInfoState outputPatientInfo3 = (PatientInfoState) signedTransaction3.getTx().getOutputs().get(0).getData();

        // assert some stuffs
        assertEquals(outputPatientInfo3.getDose(), 1);
        assertEquals(outputPatientInfo3.getFirstDoseLot(), "123a45b");
        assertEquals(outputPatientInfo3.getFirstDoseManufacturer(), "moderna");
        assertNotEquals(outputPatientInfo3.getFirstDoseDate(), placeholder);
    }

    //todo: implement tests for shit administerfirstdoseinitiator flow inputs here

    @Test(expected = ExecutionException.class)
    public void AdministerFirstDoseButItIsNotModernaOrPfizer() throws Exception {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        Date placeholder = new Date();

        try {
            placeholder = parser.parse("0000-00-00");

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        Party patient = a.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
        Party doctor = b.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
        Party employer = c.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
        Party clinicAdmin = d.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();

        PatientSendInfoInitiator sendInfoFlow = new PatientSendInfoInitiator("marc",
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
                patient,
                doctor,
                employer,
                clinicAdmin);

        CordaFuture<SignedTransaction> future = a.startFlow(sendInfoFlow);
        network.runNetwork();
        SignedTransaction signedTransaction = future.get();
        PatientInfoState outputPatientInfo = (PatientInfoState) signedTransaction.getTx().getOutputs().get(0).getData();

        assertEquals(outputPatientInfo.isApprovedForVaccination(), false);

        ApprovePatientInitiator approvePatientFlow = new ApprovePatientInitiator("marc",
                "alejandro",
                0,
                true,
                placeholder,
                "none",
                "none",
                placeholder,
                "none",
                "none",
                false,
                patient,
                doctor,
                employer,
                clinicAdmin);

        CordaFuture<SignedTransaction> future2 = b.startFlow(approvePatientFlow);
        network.runNetwork();
        SignedTransaction signedTransaction2 = future2.get();


        PatientInfoState outputPatientInfo2 = (PatientInfoState) signedTransaction2.getTx().getOutputs().get(0).getData();
        assertEquals(outputPatientInfo2.isApprovedForVaccination(), true);


        Date firstDoseDate = new Date();

        try {
            firstDoseDate = parser.parse("2021-03-03");
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        AdministerFirstDoseInitiator administerFirstDoseFlow = new AdministerFirstDoseInitiator("marc",
                "alejandro",
                1,
                true,
                firstDoseDate,
                "123a45b",
                "astrazeneca",
                placeholder,
                "none",
                "none",
                false,
                patient,
                doctor,
                employer,
                clinicAdmin);

        CordaFuture<SignedTransaction> future3 = d.startFlow(administerFirstDoseFlow);
        network.runNetwork();
        SignedTransaction signedTransaction3 = future3.get();
    }

    @Test(expected = ExecutionException.class)
    public void AdministerFirstDoseButThePatientAlreadyHadOneDose() throws Exception {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        Date placeholder = new Date();

        try {
            placeholder = parser.parse("0000-00-00");

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        Party patient = a.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
        Party doctor = b.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
        Party employer = c.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
        Party clinicAdmin = d.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();

        PatientSendInfoInitiator sendInfoFlow = new PatientSendInfoInitiator("marc",
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
                patient,
                doctor,
                employer,
                clinicAdmin);

        CordaFuture<SignedTransaction> future = a.startFlow(sendInfoFlow);
        network.runNetwork();
        SignedTransaction signedTransaction = future.get();
        PatientInfoState outputPatientInfo = (PatientInfoState) signedTransaction.getTx().getOutputs().get(0).getData();

        assertEquals(outputPatientInfo.isApprovedForVaccination(), false);

        ApprovePatientInitiator approvePatientFlow = new ApprovePatientInitiator("marc",
                "alejandro",
                0,
                true,
                placeholder,
                "none",
                "none",
                placeholder,
                "none",
                "none",
                false,
                patient,
                doctor,
                employer,
                clinicAdmin);

        CordaFuture<SignedTransaction> future2 = b.startFlow(approvePatientFlow);
        network.runNetwork();
        SignedTransaction signedTransaction2 = future2.get();


        PatientInfoState outputPatientInfo2 = (PatientInfoState) signedTransaction2.getTx().getOutputs().get(0).getData();
        assertEquals(outputPatientInfo2.isApprovedForVaccination(), true);


        Date firstDoseDate = new Date();

        try {
            firstDoseDate = parser.parse("2021-03-03");
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        AdministerFirstDoseInitiator administerFirstDoseFlow = new AdministerFirstDoseInitiator("marc",
                "alejandro",
                1,
                true,
                firstDoseDate,
                "123a45b",
                "moderna",
                placeholder,
                "none",
                "none",
                false,
                patient,
                doctor,
                employer,
                clinicAdmin);

        CordaFuture<SignedTransaction> future3 = d.startFlow(administerFirstDoseFlow);
        network.runNetwork();
        SignedTransaction signedTransaction3 = future3.get();

        AdministerFirstDoseInitiator administerFirstDoseFlow2 = new AdministerFirstDoseInitiator("marc",
                "alejandro",
                1,
                true,
                firstDoseDate,
                "123a45b",
                "moderna",
                placeholder,
                "none",
                "none",
                false,
                patient,
                doctor,
                employer,
                clinicAdmin);


        CordaFuture<SignedTransaction> future4 = d.startFlow(administerFirstDoseFlow2);
        network.runNetwork();
        SignedTransaction signedTransaction4 = future4.get();
    }

    @Test(expected = ExecutionException.class)
    public void NonClinicPartyAdministersFirstDose() throws Exception {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        Date placeholder = new Date();

        try {
            placeholder = parser.parse("0000-00-00");

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        Party patient = a.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
        Party doctor = b.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
        Party employer = c.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
        Party clinicAdmin = d.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();

        PatientSendInfoInitiator sendInfoFlow = new PatientSendInfoInitiator("marc",
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
                patient,
                doctor,
                employer,
                clinicAdmin);

        CordaFuture<SignedTransaction> future = a.startFlow(sendInfoFlow);
        network.runNetwork();
        SignedTransaction signedTransaction = future.get();
        PatientInfoState outputPatientInfo = (PatientInfoState) signedTransaction.getTx().getOutputs().get(0).getData();

        assertEquals(outputPatientInfo.isApprovedForVaccination(), false);

        ApprovePatientInitiator approvePatientFlow = new ApprovePatientInitiator("marc",
                "alejandro",
                0,
                true,
                placeholder,
                "none",
                "none",
                placeholder,
                "none",
                "none",
                false,
                patient,
                doctor,
                employer,
                clinicAdmin);

        CordaFuture<SignedTransaction> future2 = b.startFlow(approvePatientFlow);
        network.runNetwork();
        SignedTransaction signedTransaction2 = future2.get();


        PatientInfoState outputPatientInfo2 = (PatientInfoState) signedTransaction2.getTx().getOutputs().get(0).getData();
        assertEquals(outputPatientInfo2.isApprovedForVaccination(), true);


        Date firstDoseDate = new Date();

        try {
            firstDoseDate = parser.parse("2021-03-03");
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        AdministerFirstDoseInitiator administerFirstDoseFlow = new AdministerFirstDoseInitiator("marc",
                "alejandro",
                1,
                true,
                firstDoseDate,
                "123a45b",
                "moderna",
                placeholder,
                "none",
                "none",
                false,
                patient,
                doctor,
                employer,
                clinicAdmin);

        CordaFuture<SignedTransaction> future3 = a.startFlow(administerFirstDoseFlow);
        network.runNetwork();
        SignedTransaction signedTransaction3 = future3.get();
    }

    @Test(expected = ExecutionException.class)
    public void AdministerFirstDoseToNonexistentPatient() throws Exception {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        Date placeholder = new Date();

        try {
            placeholder = parser.parse("0000-00-00");

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        Party patient = a.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
        Party doctor = b.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
        Party employer = c.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
        Party clinicAdmin = d.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();

        PatientSendInfoInitiator sendInfoFlow = new PatientSendInfoInitiator("marc",
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
                patient,
                doctor,
                employer,
                clinicAdmin);

        CordaFuture<SignedTransaction> future = a.startFlow(sendInfoFlow);
        network.runNetwork();
        SignedTransaction signedTransaction = future.get();
        PatientInfoState outputPatientInfo = (PatientInfoState) signedTransaction.getTx().getOutputs().get(0).getData();

        assertEquals(outputPatientInfo.isApprovedForVaccination(), false);

        ApprovePatientInitiator approvePatientFlow = new ApprovePatientInitiator("marc",
                "alejandro",
                0,
                true,
                placeholder,
                "none",
                "none",
                placeholder,
                "none",
                "none",
                false,
                patient,
                doctor,
                employer,
                clinicAdmin);

        CordaFuture<SignedTransaction> future2 = b.startFlow(approvePatientFlow);
        network.runNetwork();
        SignedTransaction signedTransaction2 = future2.get();


        PatientInfoState outputPatientInfo2 = (PatientInfoState) signedTransaction2.getTx().getOutputs().get(0).getData();
        assertEquals(outputPatientInfo2.isApprovedForVaccination(), true);


        Date firstDoseDate = new Date();

        try {
            firstDoseDate = parser.parse("2021-03-03");
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        AdministerFirstDoseInitiator administerFirstDoseFlow = new AdministerFirstDoseInitiator("charlie",
                "nguyen",
                1,
                true,
                firstDoseDate,
                "123a45b",
                "moderna",
                placeholder,
                "none",
                "none",
                false,
                patient,
                doctor,
                employer,
                clinicAdmin);

        CordaFuture<SignedTransaction> future3 = d.startFlow(administerFirstDoseFlow);
        network.runNetwork();
        SignedTransaction signedTransaction3 = future3.get();
    }


    @Test
    public void AdministerSecondDose() throws Exception {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        Date placeholder = new Date();

        try {
            placeholder = parser.parse("0000-00-00");

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        Party patient = a.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
        Party doctor = b.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
        Party employer = c.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
        Party clinicAdmin = d.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();

        PatientSendInfoInitiator sendInfoFlow = new PatientSendInfoInitiator("marc",
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
                patient,
                doctor,
                employer,
                clinicAdmin);

        CordaFuture<SignedTransaction> future = a.startFlow(sendInfoFlow);
        network.runNetwork();
        SignedTransaction signedTransaction = future.get();
        PatientInfoState outputPatientInfo = (PatientInfoState) signedTransaction.getTx().getOutputs().get(0).getData();

        assertEquals(outputPatientInfo.isApprovedForVaccination(), false);

        ApprovePatientInitiator approvePatientFlow = new ApprovePatientInitiator("marc",
                "alejandro",
                0,
                true,
                placeholder,
                "none",
                "none",
                placeholder,
                "none",
                "none",
                false,
                patient,
                doctor,
                employer,
                clinicAdmin);

        CordaFuture<SignedTransaction> future2 = b.startFlow(approvePatientFlow);
        network.runNetwork();
        SignedTransaction signedTransaction2 = future2.get();


        PatientInfoState outputPatientInfo2 = (PatientInfoState) signedTransaction2.getTx().getOutputs().get(0).getData();
        assertEquals(outputPatientInfo2.isApprovedForVaccination(), true);


        Date firstDoseDate = new Date();

        try {
            firstDoseDate = parser.parse("2021-03-03");
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        AdministerFirstDoseInitiator administerFirstDoseFlow = new AdministerFirstDoseInitiator("marc",
                "alejandro",
                1,
                true,
                firstDoseDate,
                "123a45b",
                "moderna",
                placeholder,
                "none",
                "none",
                false,
                patient,
                doctor,
                employer,
                clinicAdmin);

        CordaFuture<SignedTransaction> future3 = d.startFlow(administerFirstDoseFlow);
        network.runNetwork();
        SignedTransaction signedTransaction3 = future3.get();
        PatientInfoState outputPatientInfo3 = (PatientInfoState) signedTransaction3.getTx().getOutputs().get(0).getData();

        // assert some stuffs
        assertEquals(outputPatientInfo3.getDose(), 1);
        assertEquals(outputPatientInfo3.getFirstDoseLot(), "123a45b");
        assertEquals(outputPatientInfo3.getFirstDoseManufacturer(), "moderna");
        assertNotEquals(outputPatientInfo3.getFirstDoseDate(), placeholder);

        Date secondDoseDate = new Date();

        try {
            secondDoseDate = parser.parse("2021-03-24");
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        AdministerSecondDoseInitiator administerSecondDoseFlow = new AdministerSecondDoseInitiator("marc",
                "alejandro",
                2,
                true,
                firstDoseDate,
                "123a45b",
                "moderna",
                secondDoseDate,
                "456c78d",
                "moderna",
                false,
                patient,
                doctor,
                employer,
                clinicAdmin);

        CordaFuture<SignedTransaction> future4 = d.startFlow(administerSecondDoseFlow);
        network.runNetwork();
        SignedTransaction signedTransaction4 = future4.get();
        PatientInfoState outputPatientInfo4 = (PatientInfoState) signedTransaction4.getTx().getOutputs().get(0).getData();

        assertEquals(outputPatientInfo4.getDose(), 2);
        assertEquals(outputPatientInfo4.getSecondDoseLot(), "456c78d");
        assertEquals(outputPatientInfo4.getSecondDoseManufacturer(), "moderna");
        assertNotEquals(outputPatientInfo4.getFirstDoseDate(), placeholder);
    }



    //todo: implement tests for shit administerseconddoseinitiator flow inputs here

    @Test(expected = ExecutionException.class)
    public void AdministerSecondDoseButItIsNotModernaOrPfizer() throws Exception {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        Date placeholder = new Date();

        try {
            placeholder = parser.parse("0000-00-00");

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        Party patient = a.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
        Party doctor = b.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
        Party employer = c.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
        Party clinicAdmin = d.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();

        PatientSendInfoInitiator sendInfoFlow = new PatientSendInfoInitiator("marc",
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
                patient,
                doctor,
                employer,
                clinicAdmin);

        CordaFuture<SignedTransaction> future = a.startFlow(sendInfoFlow);
        network.runNetwork();
        SignedTransaction signedTransaction = future.get();
        PatientInfoState outputPatientInfo = (PatientInfoState) signedTransaction.getTx().getOutputs().get(0).getData();

        assertEquals(outputPatientInfo.isApprovedForVaccination(), false);

        ApprovePatientInitiator approvePatientFlow = new ApprovePatientInitiator("marc",
                "alejandro",
                0,
                true,
                placeholder,
                "none",
                "none",
                placeholder,
                "none",
                "none",
                false,
                patient,
                doctor,
                employer,
                clinicAdmin);

        CordaFuture<SignedTransaction> future2 = b.startFlow(approvePatientFlow);
        network.runNetwork();
        SignedTransaction signedTransaction2 = future2.get();


        PatientInfoState outputPatientInfo2 = (PatientInfoState) signedTransaction2.getTx().getOutputs().get(0).getData();
        assertEquals(outputPatientInfo2.isApprovedForVaccination(), true);


        Date firstDoseDate = new Date();

        try {
            firstDoseDate = parser.parse("2021-03-03");
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        AdministerFirstDoseInitiator administerFirstDoseFlow = new AdministerFirstDoseInitiator("marc",
                "alejandro",
                1,
                true,
                firstDoseDate,
                "123a45b",
                "moderna",
                placeholder,
                "none",
                "none",
                false,
                patient,
                doctor,
                employer,
                clinicAdmin);

        CordaFuture<SignedTransaction> future3 = d.startFlow(administerFirstDoseFlow);
        network.runNetwork();
        SignedTransaction signedTransaction3 = future3.get();
        PatientInfoState outputPatientInfo3 = (PatientInfoState) signedTransaction3.getTx().getOutputs().get(0).getData();

        // assert some stuffs
        assertEquals(outputPatientInfo3.getDose(), 1);
        assertEquals(outputPatientInfo3.getFirstDoseLot(), "123a45b");
        assertEquals(outputPatientInfo3.getFirstDoseManufacturer(), "moderna");
        assertNotEquals(outputPatientInfo3.getFirstDoseDate(), placeholder);

        Date secondDoseDate = new Date();

        try {
            secondDoseDate = parser.parse("2021-03-24");
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        AdministerSecondDoseInitiator administerSecondDoseFlow = new AdministerSecondDoseInitiator("marc",
                "alejandro",
                2,
                true,
                firstDoseDate,
                "123a45b",
                "astrazeneaca",
                secondDoseDate,
                "456c78d",
                "moderna",
                false,
                patient,
                doctor,
                employer,
                clinicAdmin);

        CordaFuture<SignedTransaction> future4 = d.startFlow(administerSecondDoseFlow);
        network.runNetwork();
        SignedTransaction signedTransaction4 = future4.get();
    }

    @Test(expected = ExecutionException.class)
    public void AdministerSecondDoseButThePatientHasNoDoses() throws Exception {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        Date placeholder = new Date();

        try {
            placeholder = parser.parse("0000-00-00");

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        Party patient = a.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
        Party doctor = b.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
        Party employer = c.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
        Party clinicAdmin = d.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();

        PatientSendInfoInitiator sendInfoFlow = new PatientSendInfoInitiator("marc",
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
                patient,
                doctor,
                employer,
                clinicAdmin);

        CordaFuture<SignedTransaction> future = a.startFlow(sendInfoFlow);
        network.runNetwork();
        SignedTransaction signedTransaction = future.get();
        PatientInfoState outputPatientInfo = (PatientInfoState) signedTransaction.getTx().getOutputs().get(0).getData();

        assertEquals(outputPatientInfo.isApprovedForVaccination(), false);

        ApprovePatientInitiator approvePatientFlow = new ApprovePatientInitiator("marc",
                "alejandro",
                0,
                true,
                placeholder,
                "none",
                "none",
                placeholder,
                "none",
                "none",
                false,
                patient,
                doctor,
                employer,
                clinicAdmin);

        CordaFuture<SignedTransaction> future2 = b.startFlow(approvePatientFlow);
        network.runNetwork();
        SignedTransaction signedTransaction2 = future2.get();


        PatientInfoState outputPatientInfo2 = (PatientInfoState) signedTransaction2.getTx().getOutputs().get(0).getData();
        assertEquals(outputPatientInfo2.isApprovedForVaccination(), true);


        Date firstDoseDate = new Date();

        try {
            firstDoseDate = parser.parse("2021-03-03");
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        AdministerFirstDoseInitiator administerFirstDoseFlow = new AdministerFirstDoseInitiator("marc",
                "alejandro",
                1,
                true,
                firstDoseDate,
                "123a45b",
                "moderna",
                placeholder,
                "none",
                "none",
                false,
                patient,
                doctor,
                employer,
                clinicAdmin);

        CordaFuture<SignedTransaction> future3 = d.startFlow(administerFirstDoseFlow);
        network.runNetwork();
        SignedTransaction signedTransaction3 = future3.get();
        PatientInfoState outputPatientInfo3 = (PatientInfoState) signedTransaction3.getTx().getOutputs().get(0).getData();

        // assert some stuffs
        assertEquals(outputPatientInfo3.getDose(), 1);
        assertEquals(outputPatientInfo3.getFirstDoseLot(), "123a45b");
        assertEquals(outputPatientInfo3.getFirstDoseManufacturer(), "moderna");
        assertNotEquals(outputPatientInfo3.getFirstDoseDate(), placeholder);

        Date secondDoseDate = new Date();

        try {
            secondDoseDate = parser.parse("2021-03-24");
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        AdministerSecondDoseInitiator administerSecondDoseFlow = new AdministerSecondDoseInitiator("marc",
                "alejandro",
                1,
                true,
                firstDoseDate,
                "123a45b",
                "moderna",
                secondDoseDate,
                "456c78d",
                "moderna",
                false,
                patient,
                doctor,
                employer,
                clinicAdmin);

        CordaFuture<SignedTransaction> future4 = d.startFlow(administerSecondDoseFlow);
        network.runNetwork();
        SignedTransaction signedTransaction4 = future4.get();
    }

    @Test(expected = ExecutionException.class)
    public void AdministerSecondDoseButThePatientHasTwoDoses() throws Exception {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        Date placeholder = new Date();

        try {
            placeholder = parser.parse("0000-00-00");

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        Party patient = a.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
        Party doctor = b.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
        Party employer = c.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
        Party clinicAdmin = d.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();

        PatientSendInfoInitiator sendInfoFlow = new PatientSendInfoInitiator("marc",
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
                patient,
                doctor,
                employer,
                clinicAdmin);

        CordaFuture<SignedTransaction> future = a.startFlow(sendInfoFlow);
        network.runNetwork();
        SignedTransaction signedTransaction = future.get();
        PatientInfoState outputPatientInfo = (PatientInfoState) signedTransaction.getTx().getOutputs().get(0).getData();

        assertEquals(outputPatientInfo.isApprovedForVaccination(), false);

        ApprovePatientInitiator approvePatientFlow = new ApprovePatientInitiator("marc",
                "alejandro",
                0,
                true,
                placeholder,
                "none",
                "none",
                placeholder,
                "none",
                "none",
                false,
                patient,
                doctor,
                employer,
                clinicAdmin);

        CordaFuture<SignedTransaction> future2 = b.startFlow(approvePatientFlow);
        network.runNetwork();
        SignedTransaction signedTransaction2 = future2.get();


        PatientInfoState outputPatientInfo2 = (PatientInfoState) signedTransaction2.getTx().getOutputs().get(0).getData();
        assertEquals(outputPatientInfo2.isApprovedForVaccination(), true);


        Date firstDoseDate = new Date();

        try {
            firstDoseDate = parser.parse("2021-03-03");
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        AdministerFirstDoseInitiator administerFirstDoseFlow = new AdministerFirstDoseInitiator("marc",
                "alejandro",
                1,
                true,
                firstDoseDate,
                "123a45b",
                "moderna",
                placeholder,
                "none",
                "none",
                false,
                patient,
                doctor,
                employer,
                clinicAdmin);

        CordaFuture<SignedTransaction> future3 = d.startFlow(administerFirstDoseFlow);
        network.runNetwork();
        SignedTransaction signedTransaction3 = future3.get();
        PatientInfoState outputPatientInfo3 = (PatientInfoState) signedTransaction3.getTx().getOutputs().get(0).getData();

        // assert some stuffs
        assertEquals(outputPatientInfo3.getDose(), 1);
        assertEquals(outputPatientInfo3.getFirstDoseLot(), "123a45b");
        assertEquals(outputPatientInfo3.getFirstDoseManufacturer(), "moderna");
        assertNotEquals(outputPatientInfo3.getFirstDoseDate(), placeholder);

        Date secondDoseDate = new Date();

        try {
            secondDoseDate = parser.parse("2021-03-24");
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        AdministerSecondDoseInitiator administerSecondDoseFlow = new AdministerSecondDoseInitiator("marc",
                "alejandro",
                2,
                true,
                firstDoseDate,
                "123a45b",
                "moderna",
                secondDoseDate,
                "456c78d",
                "moderna",
                false,
                patient,
                doctor,
                employer,
                clinicAdmin);

        CordaFuture<SignedTransaction> future4 = d.startFlow(administerSecondDoseFlow);
        network.runNetwork();
        SignedTransaction signedTransaction4 = future4.get();
        PatientInfoState outputPatientInfo4 = (PatientInfoState) signedTransaction4.getTx().getOutputs().get(0).getData();

        AdministerSecondDoseInitiator administerSecondDoseFlow2 = new AdministerSecondDoseInitiator("marc",
                "alejandro",
                2,
                true,
                firstDoseDate,
                "123a45b",
                "moderna",
                secondDoseDate,
                "456c78d",
                "moderna",
                false,
                patient,
                doctor,
                employer,
                clinicAdmin);

        CordaFuture<SignedTransaction> future5 = d.startFlow(administerSecondDoseFlow2);
        network.runNetwork();
        SignedTransaction signedTransaction5 = future5.get();
    }

    @Test(expected = ExecutionException.class)
    public void NonClinicPartyAdministersSecondDose() throws Exception {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        Date placeholder = new Date();

        try {
            placeholder = parser.parse("0000-00-00");

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        Party patient = a.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
        Party doctor = b.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
        Party employer = c.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
        Party clinicAdmin = d.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();

        PatientSendInfoInitiator sendInfoFlow = new PatientSendInfoInitiator("marc",
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
                patient,
                doctor,
                employer,
                clinicAdmin);

        CordaFuture<SignedTransaction> future = a.startFlow(sendInfoFlow);
        network.runNetwork();
        SignedTransaction signedTransaction = future.get();
        PatientInfoState outputPatientInfo = (PatientInfoState) signedTransaction.getTx().getOutputs().get(0).getData();

        assertEquals(outputPatientInfo.isApprovedForVaccination(), false);

        ApprovePatientInitiator approvePatientFlow = new ApprovePatientInitiator("marc",
                "alejandro",
                0,
                true,
                placeholder,
                "none",
                "none",
                placeholder,
                "none",
                "none",
                false,
                patient,
                doctor,
                employer,
                clinicAdmin);

        CordaFuture<SignedTransaction> future2 = b.startFlow(approvePatientFlow);
        network.runNetwork();
        SignedTransaction signedTransaction2 = future2.get();


        PatientInfoState outputPatientInfo2 = (PatientInfoState) signedTransaction2.getTx().getOutputs().get(0).getData();
        assertEquals(outputPatientInfo2.isApprovedForVaccination(), true);


        Date firstDoseDate = new Date();

        try {
            firstDoseDate = parser.parse("2021-03-03");
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        AdministerFirstDoseInitiator administerFirstDoseFlow = new AdministerFirstDoseInitiator("marc",
                "alejandro",
                1,
                true,
                firstDoseDate,
                "123a45b",
                "moderna",
                placeholder,
                "none",
                "none",
                false,
                patient,
                doctor,
                employer,
                clinicAdmin);

        CordaFuture<SignedTransaction> future3 = d.startFlow(administerFirstDoseFlow);
        network.runNetwork();
        SignedTransaction signedTransaction3 = future3.get();
        PatientInfoState outputPatientInfo3 = (PatientInfoState) signedTransaction3.getTx().getOutputs().get(0).getData();

        // assert some stuffs
        assertEquals(outputPatientInfo3.getDose(), 1);
        assertEquals(outputPatientInfo3.getFirstDoseLot(), "123a45b");
        assertEquals(outputPatientInfo3.getFirstDoseManufacturer(), "moderna");
        assertNotEquals(outputPatientInfo3.getFirstDoseDate(), placeholder);

        Date secondDoseDate = new Date();

        try {
            secondDoseDate = parser.parse("2021-03-24");
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        AdministerSecondDoseInitiator administerSecondDoseFlow = new AdministerSecondDoseInitiator("marc",
                "alejandro",
                2,
                true,
                firstDoseDate,
                "123a45b",
                "moderna",
                secondDoseDate,
                "456c78d",
                "moderna",
                false,
                patient,
                doctor,
                employer,
                clinicAdmin);

        CordaFuture<SignedTransaction> future4 = a.startFlow(administerSecondDoseFlow);
        network.runNetwork();
        SignedTransaction signedTransaction4 = future4.get();
    }







    @Test
    public void theWholeProcess() throws Exception{
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        Date placeholder = new Date();

        try {
            placeholder = parser.parse("0000-00-00");

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        Party patient = a.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
        Party doctor = b.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
        Party employer = c.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
        Party clinicAdmin = d.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();

        PatientSendInfoInitiator sendInfoFlow = new PatientSendInfoInitiator("marc",
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
                patient,
                doctor,
                employer,
                clinicAdmin);

        CordaFuture<SignedTransaction> future = a.startFlow(sendInfoFlow);
        network.runNetwork();
        SignedTransaction signedTransaction = future.get();
        PatientInfoState outputPatientInfo = (PatientInfoState) signedTransaction.getTx().getOutputs().get(0).getData();

        assertEquals(outputPatientInfo.isApprovedForVaccination(), false);

        ApprovePatientInitiator approvePatientFlow = new ApprovePatientInitiator("marc",
                "alejandro",
                0,
                true,
                placeholder,
                "none",
                "none",
                placeholder,
                "none",
                "none",
                false,
                patient,
                doctor,
                employer,
                clinicAdmin);

        CordaFuture<SignedTransaction> future2 = b.startFlow(approvePatientFlow);
        network.runNetwork();
        SignedTransaction signedTransaction2 = future2.get();


        PatientInfoState outputPatientInfo2 = (PatientInfoState) signedTransaction2.getTx().getOutputs().get(0).getData();
        assertEquals(outputPatientInfo2.isApprovedForVaccination(), true);


        Date firstDoseDate = new Date();

        try {
            firstDoseDate = parser.parse("2021-03-03");
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        AdministerFirstDoseInitiator administerFirstDoseFlow = new AdministerFirstDoseInitiator("marc",
                "alejandro",
                1,
                true,
                firstDoseDate,
                "123a45b",
                "moderna",
                placeholder,
                "none",
                "none",
                false,
                patient,
                doctor,
                employer,
                clinicAdmin);

        CordaFuture<SignedTransaction> future3 = d.startFlow(administerFirstDoseFlow);
        network.runNetwork();
        SignedTransaction signedTransaction3 = future3.get();
        PatientInfoState outputPatientInfo3 = (PatientInfoState) signedTransaction3.getTx().getOutputs().get(0).getData();

        // assert some stuffs
        assertEquals(outputPatientInfo3.getDose(), 1);
        assertEquals(outputPatientInfo3.getFirstDoseLot(), "123a45b");
        assertEquals(outputPatientInfo3.getFirstDoseManufacturer(), "moderna");
        assertNotEquals(outputPatientInfo3.getFirstDoseDate(), placeholder);

        Date secondDoseDate = new Date();

        try {
            secondDoseDate = parser.parse("2021-03-24");
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        AdministerSecondDoseInitiator administerSecondDoseFlow = new AdministerSecondDoseInitiator("marc",
                "alejandro",
                2,
                true,
                firstDoseDate,
                "123a45b",
                "moderna",
                secondDoseDate,
                "456c78d",
                "moderna",
                false,
                patient,
                doctor,
                employer,
                clinicAdmin);

        CordaFuture<SignedTransaction> future4 = d.startFlow(administerSecondDoseFlow);
        network.runNetwork();
        SignedTransaction signedTransaction4 = future4.get();
        PatientInfoState outputPatientInfo4 = (PatientInfoState) signedTransaction4.getTx().getOutputs().get(0).getData();

        assertEquals(outputPatientInfo4.getDose(), 2);
        assertEquals(outputPatientInfo4.getSecondDoseLot(), "456c78d");
        assertEquals(outputPatientInfo4.getSecondDoseManufacturer(), "moderna");
        assertNotEquals(outputPatientInfo4.getFirstDoseDate(), placeholder);

        ApprovePatientForWorkInitiator approvePatientForWorkFlow = new ApprovePatientForWorkInitiator("marc",
                "alejandro",
                2,
                true,
                firstDoseDate,
                "123a45b",
                "moderna",
                secondDoseDate,
                "456c78d",
                "moderna",
                true,
                patient,
                doctor,
                employer,
                clinicAdmin);

        CordaFuture<SignedTransaction> future5 = c.startFlow(approvePatientForWorkFlow);
        network.runNetwork();
        SignedTransaction signedTransaction5 = future5.get();
        PatientInfoState outputPatientInfo5 = (PatientInfoState) signedTransaction5.getTx().getOutputs().get(0).getData();

        assertEquals(outputPatientInfo5.getDose(), 2);
        assertEquals(outputPatientInfo5.getFirstDoseLot(), "123a45b");
        assertEquals(outputPatientInfo5.getFirstDoseManufacturer(), "moderna");
        assertNotEquals(outputPatientInfo5.getFirstDoseDate(), placeholder);
        assertEquals(outputPatientInfo5.getSecondDoseLot(), "456c78d");
        assertEquals(outputPatientInfo5.getSecondDoseManufacturer(), "moderna");
        assertNotEquals(outputPatientInfo5.getFirstDoseDate(), placeholder);
        assertTrue(outputPatientInfo5.isVaccinationProcessComplete());
    }




    //todo: implement tests for shit approvepatientforworkinitiator flow inputs here


    @Test(expected = ExecutionException.class)
    public void theWholeProcessButPatientCannotBeFound() throws Exception {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        Date placeholder = new Date();

        try {
            placeholder = parser.parse("0000-00-00");

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        Party patient = a.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
        Party doctor = b.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
        Party employer = c.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
        Party clinicAdmin = d.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();

        PatientSendInfoInitiator sendInfoFlow = new PatientSendInfoInitiator("marc",
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
                patient,
                doctor,
                employer,
                clinicAdmin);

        CordaFuture<SignedTransaction> future = a.startFlow(sendInfoFlow);
        network.runNetwork();
        SignedTransaction signedTransaction = future.get();
        PatientInfoState outputPatientInfo = (PatientInfoState) signedTransaction.getTx().getOutputs().get(0).getData();

        assertEquals(outputPatientInfo.isApprovedForVaccination(), false);

        ApprovePatientInitiator approvePatientFlow = new ApprovePatientInitiator("marc",
                "alejandro",
                0,
                true,
                placeholder,
                "none",
                "none",
                placeholder,
                "none",
                "none",
                false,
                patient,
                doctor,
                employer,
                clinicAdmin);

        CordaFuture<SignedTransaction> future2 = b.startFlow(approvePatientFlow);
        network.runNetwork();
        SignedTransaction signedTransaction2 = future2.get();


        PatientInfoState outputPatientInfo2 = (PatientInfoState) signedTransaction2.getTx().getOutputs().get(0).getData();
        assertEquals(outputPatientInfo2.isApprovedForVaccination(), true);


        Date firstDoseDate = new Date();

        try {
            firstDoseDate = parser.parse("2021-03-03");
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        AdministerFirstDoseInitiator administerFirstDoseFlow = new AdministerFirstDoseInitiator("marc",
                "alejandro",
                1,
                true,
                firstDoseDate,
                "123a45b",
                "moderna",
                placeholder,
                "none",
                "none",
                false,
                patient,
                doctor,
                employer,
                clinicAdmin);

        CordaFuture<SignedTransaction> future3 = d.startFlow(administerFirstDoseFlow);
        network.runNetwork();
        SignedTransaction signedTransaction3 = future3.get();
        PatientInfoState outputPatientInfo3 = (PatientInfoState) signedTransaction3.getTx().getOutputs().get(0).getData();

        // assert some stuffs
        assertEquals(outputPatientInfo3.getDose(), 1);
        assertEquals(outputPatientInfo3.getFirstDoseLot(), "123a45b");
        assertEquals(outputPatientInfo3.getFirstDoseManufacturer(), "moderna");
        assertNotEquals(outputPatientInfo3.getFirstDoseDate(), placeholder);

        Date secondDoseDate = new Date();

        try {
            secondDoseDate = parser.parse("2021-03-24");
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        AdministerSecondDoseInitiator administerSecondDoseFlow = new AdministerSecondDoseInitiator("marc",
                "alejandro",
                2,
                true,
                firstDoseDate,
                "123a45b",
                "moderna",
                secondDoseDate,
                "456c78d",
                "moderna",
                false,
                patient,
                doctor,
                employer,
                clinicAdmin);

        CordaFuture<SignedTransaction> future4 = d.startFlow(administerSecondDoseFlow);
        network.runNetwork();
        SignedTransaction signedTransaction4 = future4.get();
        PatientInfoState outputPatientInfo4 = (PatientInfoState) signedTransaction4.getTx().getOutputs().get(0).getData();

        assertEquals(outputPatientInfo4.getDose(), 2);
        assertEquals(outputPatientInfo4.getSecondDoseLot(), "456c78d");
        assertEquals(outputPatientInfo4.getSecondDoseManufacturer(), "moderna");
        assertNotEquals(outputPatientInfo4.getFirstDoseDate(), placeholder);

        ApprovePatientForWorkInitiator approvePatientForWorkFlow = new ApprovePatientForWorkInitiator("charlie",
                "nguyen",
                2,
                true,
                firstDoseDate,
                "123a45b",
                "moderna",
                secondDoseDate,
                "456c78d",
                "moderna",
                true,
                patient,
                doctor,
                employer,
                clinicAdmin);

        CordaFuture<SignedTransaction> future5 = c.startFlow(approvePatientForWorkFlow);
        network.runNetwork();
        SignedTransaction signedTransaction5 = future5.get();
        PatientInfoState outputPatientInfo5 = (PatientInfoState) signedTransaction5.getTx().getOutputs().get(0).getData();
    }










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
