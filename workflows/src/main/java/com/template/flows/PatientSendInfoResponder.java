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

            @Suspendable
            @Override
            protected void checkTransaction(SignedTransaction stx) throws FlowException {
                requireThat(req -> {
                    ContractState output = stx.getTx().getOutputs().get(0).getData();
                    req.using("This is for sending info to a patient.", output instanceof PatientInfoState);
                    PatientInfoState patientinfo = (PatientInfoState) output;
                    req.using("This patient must have no prior dosages.", patientinfo.getDose() == 0);
                    req.using("Patient is not yet approved for vaccination.", !patientinfo.isApprovedForVaccination());
                    req.using("Patient's vaccination process is not complete.'", !patientinfo.isVaccinationProcessComplete());
                    return null;
                });
            }
        });

        // store the transaction into data base.
        subFlow(new ReceiveFinalityFlow(counterpartySession, signedTransaction.getId()));
        return null;
    }
}


