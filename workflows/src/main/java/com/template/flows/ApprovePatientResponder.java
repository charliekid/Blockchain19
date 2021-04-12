package com.template.flows;
import co.paralleluniverse.fibers.Suspendable;
import com.template.states.PatientInfoState;
import net.corda.core.contracts.ContractState;
import net.corda.core.flows.*;
import net.corda.core.transactions.SignedTransaction;

import static net.corda.core.contracts.ContractsDSL.requireThat;

// ******************
// * Responder flow *
// ******************
@InitiatedBy(ApprovePatientInitiator.class)

public class ApprovePatientResponder extends FlowLogic<SignedTransaction>  {

    //private variable
    private FlowSession counterpartySession;

    //Constructor
    public ApprovePatientResponder(FlowSession counterpartySession) {
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
                    ContractState output = stx.getTx().getOutputs().get(0).getData();

                    req.using("This is for sending info.", output instanceof PatientInfoState);
                    PatientInfoState patientinfo = (PatientInfoState) output;
                    req.using("This patient must have no prior dosages.", patientinfo.getDose() == 0);
                    req.using("Patient should now be ready for vaccination.", patientinfo.isApprovedForVaccination());
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
