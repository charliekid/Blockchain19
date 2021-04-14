package com.template.contracts;

import com.template.states.PatientInfoState;
import net.corda.core.identity.CordaX500Name;
import net.corda.core.contracts.Contract;
import net.corda.testing.contracts.DummyState;
import net.corda.testing.core.DummyCommandData;
import net.corda.testing.core.TestIdentity;
import net.corda.testing.node.MockServices;
import static net.corda.testing.node.NodeTestUtils.transaction;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class ContractTests {

    private final TestIdentity charlie = new TestIdentity(new CordaX500Name("Charlie", "", "GB"));
    private final TestIdentity jorge = new TestIdentity(new CordaX500Name("Jorge", "", "GB"));
    private final TestIdentity marc = new TestIdentity(new CordaX500Name("Marc", "", "GB"));
    private final TestIdentity jonathan = new TestIdentity(new CordaX500Name("Jonathan", "", "GB"));

    private final MockServices ledgerServices = new MockServices();

    private PatientInfoState patientInfoState = new PatientInfoState("marc", "alejandro", 0, false, null, null, null, null, null, null, false, marc.getParty(), jorge.getParty(), charlie.getParty(), jonathan.getParty());

    @Test
    public void dummyTest() {

    }

    @Test
    public void patientContractImplementsContract() {
        assert(new PatientContract() instanceof Contract);
    }

    // refering to: https://github.com/corda/bootcamp-cordapp/blob/v4/src/test/java/bootcamp/ContractTests.java

    @Test
    public void sendPatientInfo() {

    }

    //    private TokenState tokenState = new TokenState(alice.getParty(), bob.getParty(), 1);

    @Test
    public void patientContractRequiresZeroInputsInTheTransaction() {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        Date placeholder = new Date();

        try {
            placeholder = parser.parse("0000-00-00");

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }


        PatientInfoState patientInfoState = new PatientInfoState("marc",
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
                marc.getParty(),
                jorge.getParty(),
                charlie.getParty(),
                jonathan.getParty());

        transaction(ledgerServices, tx -> {
            // Has an input, will fail.
            tx.input(PatientContract.ID, patientInfoState);
            tx.output(PatientContract.ID, patientInfoState);
            tx.command(Arrays.asList(marc.getPublicKey(), jorge.getPublicKey(), charlie.getPublicKey(), jonathan.getPublicKey()), new PatientContract.Commands.SendInfo());
            tx.fails();
            return null;
        });

        transaction(ledgerServices, tx -> {
            // Has no input, will verify.
            tx.output(PatientContract.ID, patientInfoState);
            tx.command(Arrays.asList(marc.getPublicKey(), jorge.getPublicKey(), charlie.getPublicKey(), jonathan.getPublicKey()), new PatientContract.Commands.SendInfo());
            tx.verifies();
            return null;
        });
    }

    @Test(expected=AssertionError.class)
    public void patientContractRequiresOneOutputInTheTransaction() {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        Date placeholder = new Date();

        try {
            placeholder = parser.parse("0000-00-00");

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }


        PatientInfoState patientInfoState = new PatientInfoState("marc",
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
                marc.getParty(),
                jorge.getParty(),
                charlie.getParty(),
                jonathan.getParty());

        transaction(ledgerServices, tx -> {
            // Has two outputs, will fail.
            tx.output(PatientContract.ID, patientInfoState);
            tx.output(PatientContract.ID, patientInfoState);
            tx.command(Arrays.asList(marc.getPublicKey(), jorge.getPublicKey(), charlie.getPublicKey(), jonathan.getPublicKey()), new PatientContract.Commands.SendInfo());
            tx.fails();
            return null;
        });

        transaction(ledgerServices, tx -> {
            // Has one output, will verify.
            tx.output(PatientContract.ID, patientInfoState);
            tx.command(Arrays.asList(marc.getPublicKey(), jorge.getPublicKey(), charlie.getPublicKey(), jonathan.getPublicKey()), new PatientContract.Commands.SendInfo());
            tx.verifies();
            return null;
        });
    }

    @Test
    public void patientContractRequiresOneCommandInTheTransaction() {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        Date placeholder = new Date();

        try {
            placeholder = parser.parse("0000-00-00");

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }


        PatientInfoState patientInfoState = new PatientInfoState("marc",
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
                marc.getParty(),
                jorge.getParty(),
                charlie.getParty(),
                jonathan.getParty());

        transaction(ledgerServices, tx -> {
            tx.output(PatientContract.ID, patientInfoState);
            // Has two commands, will fail.
            tx.command(Arrays.asList(marc.getPublicKey(), jorge.getPublicKey(), charlie.getPublicKey(), jonathan.getPublicKey()), new PatientContract.Commands.SendInfo());
            tx.command(Arrays.asList(marc.getPublicKey(), jorge.getPublicKey(), charlie.getPublicKey(), jonathan.getPublicKey()), new PatientContract.Commands.SendInfo());
            tx.fails();
            return null;
        });

        transaction(ledgerServices, tx -> {
            tx.output(PatientContract.ID, patientInfoState);
            // Has one command, will verify.
            tx.command(Arrays.asList(marc.getPublicKey(), jorge.getPublicKey(), charlie.getPublicKey(), jonathan.getPublicKey()), new PatientContract.Commands.SendInfo());
            tx.verifies();
            return null;
        });
    }

    @Test
    public void patientContractRequiresTheTransactionsOutputToBeAPatientState() {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        Date placeholder = new Date();

        try {
            placeholder = parser.parse("0000-00-00");

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }


        PatientInfoState patientInfoState = new PatientInfoState("marc",
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
                marc.getParty(),
                jorge.getParty(),
                charlie.getParty(),
                jonathan.getParty());

        transaction(ledgerServices, tx -> {
            // Has wrong output type, will fail.
            tx.output(PatientContract.ID, new DummyState());
            tx.command(Arrays.asList(marc.getPublicKey(), jorge.getPublicKey(), charlie.getPublicKey(), jonathan.getPublicKey()), new PatientContract.Commands.SendInfo());
            tx.fails();
            return null;
        });

        transaction(ledgerServices, tx -> {
            // Has correct output type, will verify.
            tx.output(PatientContract.ID, patientInfoState);
            tx.command(Arrays.asList(marc.getPublicKey(), jorge.getPublicKey(), charlie.getPublicKey(), jonathan.getPublicKey()), new PatientContract.Commands.SendInfo());
            tx.verifies();
            return null;
        });
    }



    @Test
    public void thePatientSendInfoCommandNeedsTheDosageToBeZero() {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        Date placeholder = new Date();

        try {
            placeholder = parser.parse("0000-00-00");

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        PatientInfoState zeroDose = new PatientInfoState("marc",
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
                marc.getParty(),
                jorge.getParty(),
                charlie.getParty(),
                jonathan.getParty());

        PatientInfoState oneDose = new PatientInfoState("marc",
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
                marc.getParty(),
                jorge.getParty(),
                charlie.getParty(),
                jonathan.getParty());

        PatientInfoState twoDose = new PatientInfoState("marc",
                "alejandro",
                2,
                false,
                placeholder,
                "none",
                "none",
                placeholder,
                "none",
                "none",
                false,
                marc.getParty(),
                jorge.getParty(),
                charlie.getParty(),
                jonathan.getParty());

        transaction(ledgerServices, tx -> {
            // Has one dose, will fail.
            tx.output(PatientContract.ID, oneDose);
            tx.command(Arrays.asList(marc.getPublicKey(), jorge.getPublicKey(), charlie.getPublicKey(), jonathan.getPublicKey()), new PatientContract.Commands.SendInfo());
            tx.fails();
            return null;
        });

        transaction(ledgerServices, tx -> {
            // Has two dose, will fail.
            tx.output(PatientContract.ID, twoDose);
            tx.command(Arrays.asList(marc.getPublicKey(), jorge.getPublicKey(), charlie.getPublicKey(), jonathan.getPublicKey()), new PatientContract.Commands.SendInfo());
            tx.fails();
            return null;
        });

        transaction(ledgerServices, tx -> {
            // Has no dose, will verify.
            tx.output(PatientContract.ID, zeroDose);
            tx.command(Arrays.asList(marc.getPublicKey(), jorge.getPublicKey(), charlie.getPublicKey(), jonathan.getPublicKey()), new PatientContract.Commands.SendInfo());
            tx.verifies();
            return null;
        });
    }

    @Test
    public void tokenContractRequiresTheTransactionsCommandToBeAnIssueCommand() {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        Date placeholder = new Date();

        try {
            placeholder = parser.parse("0000-00-00");

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }


        PatientInfoState patientInfoState = new PatientInfoState("marc",
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
                marc.getParty(),
                jorge.getParty(),
                charlie.getParty(),
                jonathan.getParty());

        transaction(ledgerServices, tx -> {
            // Has wrong command type, will fail.
            tx.output(PatientContract.ID, patientInfoState);
            tx.command(Arrays.asList(marc.getPublicKey(), jorge.getPublicKey(), charlie.getPublicKey(), jonathan.getPublicKey()), DummyCommandData.INSTANCE);
            tx.fails();
            return null;
        });

        transaction(ledgerServices, tx -> {
            // Has correct command type, will verify.
            tx.output(PatientContract.ID, patientInfoState);
            tx.command(Arrays.asList(marc.getPublicKey(), jorge.getPublicKey(), charlie.getPublicKey(), jonathan.getPublicKey()), new PatientContract.Commands.SendInfo());
            tx.verifies();
            return null;
        });
    }

    //testing for the ApprovePatient command
    @Test
    public void patientContractForApprovePatientRequiresOneInputInTheTransaction() {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        Date placeholder = new Date();

        try {
            placeholder = parser.parse("0000-00-00");

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }


        PatientInfoState inputPatientInfoState = new PatientInfoState("marc",
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
                marc.getParty(),
                jorge.getParty(),
                charlie.getParty(),
                jonathan.getParty());

        PatientInfoState outputPatientInfoState = new PatientInfoState("marc",
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
                marc.getParty(),
                jorge.getParty(),
                charlie.getParty(),
                jonathan.getParty());

        transaction(ledgerServices, tx -> {
            // Has an input, will verify.
            tx.input(PatientContract.ID, inputPatientInfoState);
            tx.output(PatientContract.ID, outputPatientInfoState);
            tx.command(Arrays.asList(marc.getPublicKey(), jorge.getPublicKey(), charlie.getPublicKey(), jonathan.getPublicKey()), new PatientContract.Commands.ApprovePatient());
            tx.verifies();
            return null;
        });

        transaction(ledgerServices, tx -> {
            // Has no input, will fail.
            tx.output(PatientContract.ID, outputPatientInfoState);
            tx.command(Arrays.asList(marc.getPublicKey(), jorge.getPublicKey(), charlie.getPublicKey(), jonathan.getPublicKey()), new PatientContract.Commands.ApprovePatient());
            tx.fails();
            return null;
        });
    }

    //administerfirstdose

    @Test
    public void patientContractForAdministerFirstDoseRequiresOneInputInTheTransaction() {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        Date placeholder = new Date();

        try {
            placeholder = parser.parse("0000-00-00");

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        Date firstDoseDate = new Date();

        try {
            firstDoseDate = parser.parse("2021-03-03");
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }


        PatientInfoState inputPatientInfoState = new PatientInfoState("marc",
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
                marc.getParty(),
                jorge.getParty(),
                charlie.getParty(),
                jonathan.getParty());

        PatientInfoState outputPatientInfoState = new PatientInfoState("marc",
                "alejandro",
                1,
                true,
                firstDoseDate,
                "123a45b",
                "pfizer",
                placeholder,
                "none",
                "none",
                false,
                marc.getParty(),
                jorge.getParty(),
                charlie.getParty(),
                jonathan.getParty());

        transaction(ledgerServices, tx -> {
            // Has an input, will verify.
            tx.input(PatientContract.ID, inputPatientInfoState);
            tx.output(PatientContract.ID, outputPatientInfoState);
            tx.command(Arrays.asList(marc.getPublicKey(), jorge.getPublicKey(), charlie.getPublicKey(), jonathan.getPublicKey()), new PatientContract.Commands.AdministerFirstDose());
            tx.verifies();
            return null;
        });

        transaction(ledgerServices, tx -> {
            // Has no input, will fail.
            tx.output(PatientContract.ID, outputPatientInfoState);
            tx.command(Arrays.asList(marc.getPublicKey(), jorge.getPublicKey(), charlie.getPublicKey(), jonathan.getPublicKey()), new PatientContract.Commands.AdministerFirstDose());
            tx.fails();
            return null;
        });
    }



//    @Test
//    public void tokenContractRequiresTheIssuerToBeARequiredSignerInTheTransaction() {
//        TokenState tokenStateWhereBobIsIssuer = new TokenState(bob.getParty(), alice.getParty(), 1);
//
//        transaction(ledgerServices, tx -> {
//            // Issuer is not a required signer, will fail.
//            tx.output(TokenContract.ID, tokenState);
//            tx.command(bob.getPublicKey(), new TokenContract.Commands.Issue());
//            tx.fails();
//            return null;
//        });
//
//        transaction(ledgerServices, tx -> {
//            // Issuer is also not a required signer, will fail.
//            tx.output(TokenContract.ID, tokenStateWhereBobIsIssuer);
//            tx.command(alice.getPublicKey(), new TokenContract.Commands.Issue());
//            tx.fails();
//            return null;
//        });
//
//        transaction(ledgerServices, tx -> {
//            // Issuer is a required signer, will verify.
//            tx.output(TokenContract.ID, tokenState);
//            tx.command(Arrays.asList(alice.getPublicKey(), bob.getPublicKey()), new TokenContract.Commands.Issue());
//            tx.verifies();
//            return null;
//        });
//
//        transaction(ledgerServices, tx -> {
//            // Issuer is also a required signer, will verify.
//            tx.output(TokenContract.ID, tokenStateWhereBobIsIssuer);
//            tx.command(Arrays.asList(alice.getPublicKey(), bob.getPublicKey()), new TokenContract.Commands.Issue());
//            tx.verifies();
//            return null;
//        });
//    }
}