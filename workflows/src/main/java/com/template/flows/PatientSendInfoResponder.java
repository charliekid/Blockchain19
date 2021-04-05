package com.template.flows;
import co.paralleluniverse.fibers.Suspendable;
import com.template.states.PatientInfoState;
import net.corda.core.contracts.ContractState;
import net.corda.core.flows.*;
import net.corda.core.transactions.SignedTransaction;

import java.text.SimpleDateFormat;
import java.util.Date;

import static net.corda.core.contracts.ContractsDSL.requireThat;

// ******************
// * Responder flow *
// ******************
@InitiatedBy(PatientSendInfoInitiator.class)
public class PatientSendInfoResponder extends FlowLogic<SignedTransaction> {

    //private variable
    private FlowSession counterpartySession;

    //Constructor
    public PatientSendInfoResponder(FlowSession counterpartySession) {
        this.counterpartySession = counterpartySession;
    }

    @Suspendable
    @Override
    public SignedTransaction call() throws FlowException {
        SignedTransaction signedTransaction = subFlow(new SignTransactionFlow(counterpartySession) {


            /**
             * The checkTransaction function should be used only to model business logic.
             * A contract’s verify function should be used to define what is and is not
             *  \possible within a transaction.
             * @param stx
             * @throws FlowException
             */
            @Suspendable
            @Override
            protected void checkTransaction(SignedTransaction stx) throws FlowException {
                /*
                 * SignTransactionFlow will automatically verify the transaction and its signatures before signing it.
                 * However, just because a transaction is contractually valid doesn’t mean we necessarily want to sign.
                 * What if we don’t want to deal with the counterparty in question, or the value is too high,
                 * or we’re not happy with the transaction’s structure? checkTransaction
                 * allows us to define these additional checks. If any of these conditions are not met,
                 * we will not sign the transaction - even if the transaction and its signatures are contractually valid.
                 * ----------
                 * For this hello-world cordapp, we will not implement any aditional checks.
                 * */
                requireThat(req -> {
                    SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
                    Date placeholder = new Date();

                    try {
                        placeholder = parser.parse("0000-00-00");

                    } catch (java.text.ParseException e) {
                        e.printStackTrace();
                    }

                    ContractState output = stx.getTx().getOutputs().get(0).getData();
                    req.using("This is for sending info to a patient.", output instanceof PatientInfoState);
                    PatientInfoState patientinfo = (PatientInfoState) output;
                    req.using("This patient must have no prior dosages.", patientinfo.getDose() == 0);
                    req.using("Patient is not yet approved for vaccination.", !patientinfo.isApprovedForVaccination());

                    //check for placeholders
                    req.using("Patient should have no first dose date", patientinfo.getFirstDoseDate().equals(placeholder));
                    req.using("Patient should have no first dose lot", patientinfo.getFirstDoseLot().equals("none"));
                    req.using("Patient should have no first dose manufacturer", patientinfo.getFirstDoseManufacturer().equals("none"));
                    req.using("Patient should have no second dose date", patientinfo.getSecondDoseDate().equals(placeholder));
                    req.using("Patient should have no second dose lot", patientinfo.getSecondDoseLot().equals("none"));
                    req.using("Patient should have no second dose manufacturer", patientinfo.getSecondDoseManufacturer().equals("none"));
                    req.using("Patient's vaccination process is not complete.'", !patientinfo.isVaccinationProcessComplete());

                    return null;
                });

            }
        });
        //Stored the transaction into data base.
        subFlow(new ReceiveFinalityFlow(counterpartySession, signedTransaction.getId()));
        return null;
    }
}


