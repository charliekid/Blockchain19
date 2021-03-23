package com.template.contracts;

import com.template.states.PatientInfoState;
import net.corda.core.identity.CordaX500Name;
import net.corda.core.contracts.Contract;
import net.corda.testing.core.TestIdentity;
import net.corda.testing.node.MockServices;
import static net.corda.testing.node.NodeTestUtils.transaction;
import org.junit.Test;

import java.util.Arrays;

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

    // todo: fix whatever error this is: [WARN] 05:15:15,149 [Test worker] contracts.TransactionState. - State class com.template.states.PatientInfoState belongs to contract com.template.contracts.PatientContract, but is bundled with contract com.template.contacts.PatientContract in TransactionState. Annotate PatientInfoState with @BelongsToContract(com.template.contacts.PatientContract.class) to remove this warning.
    @Test(expected=AssertionError.class)
    public void tokenContractRequiresZeroInputsInTheTransaction() {
        transaction(ledgerServices, tx -> {
            // Has an input, will fail.
            tx.input(PatientContract.ID, patientInfoState);
            tx.output(PatientContract.ID, patientInfoState);
            tx.command(Arrays.asList(marc.getPublicKey(), jorge.getPublicKey()), new PatientContract.Commands.SendInfo());
            tx.fails();
            return null;
        });

        transaction(ledgerServices, tx -> {
            // Has no input, will verify.
            tx.output(PatientContract.ID, patientInfoState);
            tx.command(Arrays.asList(marc.getPublicKey(), jorge.getPublicKey()), new PatientContract.Commands.SendInfo());
            tx.verifies();
            return null;
        });
    }
//
//    @Test
//    public void tokenContractRequiresOneOutputInTheTransaction() {
//        transaction(ledgerServices, tx -> {
//            // Has two outputs, will fail.
//            tx.output(TokenContract.ID, tokenState);
//            tx.output(TokenContract.ID, tokenState);
//            tx.command(Arrays.asList(alice.getPublicKey(), bob.getPublicKey()), new TokenContract.Commands.Issue());
//            tx.fails();
//            return null;
//        });
//
//        transaction(ledgerServices, tx -> {
//            // Has one output, will verify.
//            tx.output(TokenContract.ID, tokenState);
//            tx.command(Arrays.asList(alice.getPublicKey(), bob.getPublicKey()), new TokenContract.Commands.Issue());
//            tx.verifies();
//            return null;
//        });
//    }
//
//    @Test
//    public void tokenContractRequiresOneCommandInTheTransaction() {
//        transaction(ledgerServices, tx -> {
//            tx.output(TokenContract.ID, tokenState);
//            // Has two commands, will fail.
//            tx.command(Arrays.asList(alice.getPublicKey(), bob.getPublicKey()), new TokenContract.Commands.Issue());
//            tx.command(Arrays.asList(alice.getPublicKey(), bob.getPublicKey()), new TokenContract.Commands.Issue());
//            tx.fails();
//            return null;
//        });
//
//        transaction(ledgerServices, tx -> {
//            tx.output(TokenContract.ID, tokenState);
//            // Has one command, will verify.
//            tx.command(Arrays.asList(alice.getPublicKey(), bob.getPublicKey()), new TokenContract.Commands.Issue());
//            tx.verifies();
//            return null;
//        });
//    }
//
//    @Test
//    public void tokenContractRequiresTheTransactionsOutputToBeATokenState() {
//        transaction(ledgerServices, tx -> {
//            // Has wrong output type, will fail.
//            tx.output(TokenContract.ID, new DummyState());
//            tx.command(Arrays.asList(alice.getPublicKey(), bob.getPublicKey()), new TokenContract.Commands.Issue());
//            tx.fails();
//            return null;
//        });
//
//        transaction(ledgerServices, tx -> {
//            // Has correct output type, will verify.
//            tx.output(TokenContract.ID, tokenState);
//            tx.command(Arrays.asList(alice.getPublicKey(), bob.getPublicKey()), new TokenContract.Commands.Issue());
//            tx.verifies();
//            return null;
//        });
//    }
//
//    @Test
//    public void tokenContractRequiresTheTransactionsOutputToHaveAPositiveAmount() {
//        TokenState zeroTokenState = new TokenState(alice.getParty(), bob.getParty(), 0);
//        TokenState negativeTokenState = new TokenState(alice.getParty(), bob.getParty(), -1);
//        TokenState positiveTokenState = new TokenState(alice.getParty(), bob.getParty(), 2);
//
//        transaction(ledgerServices, tx -> {
//            // Has zero-amount TokenState, will fail.
//            tx.output(TokenContract.ID, zeroTokenState);
//            tx.command(Arrays.asList(alice.getPublicKey(), bob.getPublicKey()), new TokenContract.Commands.Issue());
//            tx.fails();
//            return null;
//        });
//
//        transaction(ledgerServices, tx -> {
//            // Has negative-amount TokenState, will fail.
//            tx.output(TokenContract.ID, negativeTokenState);
//            tx.command(Arrays.asList(alice.getPublicKey(), bob.getPublicKey()), new TokenContract.Commands.Issue());
//            tx.fails();
//            return null;
//        });
//
//        transaction(ledgerServices, tx -> {
//            // Has positive-amount TokenState, will verify.
//            tx.output(TokenContract.ID, tokenState);
//            tx.command(Arrays.asList(alice.getPublicKey(), bob.getPublicKey()), new TokenContract.Commands.Issue());
//            tx.verifies();
//            return null;
//        });
//
//        transaction(ledgerServices, tx -> {
//            // Also has positive-amount TokenState, will verify.
//            tx.output(TokenContract.ID, positiveTokenState);
//            tx.command(Arrays.asList(alice.getPublicKey(), bob.getPublicKey()), new TokenContract.Commands.Issue());
//            tx.verifies();
//            return null;
//        });
//    }
//
//    @Test
//    public void tokenContractRequiresTheTransactionsCommandToBeAnIssueCommand() {
//        transaction(ledgerServices, tx -> {
//            // Has wrong command type, will fail.
//            tx.output(TokenContract.ID, tokenState);
//            tx.command(Arrays.asList(alice.getPublicKey(), bob.getPublicKey()), DummyCommandData.INSTANCE);
//            tx.fails();
//            return null;
//        });
//
//        transaction(ledgerServices, tx -> {
//            // Has correct command type, will verify.
//            tx.output(TokenContract.ID, tokenState);
//            tx.command(Arrays.asList(alice.getPublicKey(), bob.getPublicKey()), new TokenContract.Commands.Issue());
//            tx.verifies();
//            return null;
//        });
//    }
//
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