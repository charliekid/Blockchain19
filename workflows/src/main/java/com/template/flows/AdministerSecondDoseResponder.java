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
@InitiatedBy(AdministerSecondDoseInitiator.class)

public class AdministerSecondDoseResponder extends FlowLogic<SignedTransaction>  {

    // private variable
    private FlowSession counterpartySession;

    //Constructor
    public AdministerSecondDoseResponder(FlowSession counterpartySession) {
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

                    req.using("This is for sending info.", output instanceof PatientInfoState);
                    PatientInfoState patientinfo = (PatientInfoState) output;
                    req.using("This patient must have two doses.", patientinfo.getDose() == 2);
                    req.using("Patient should now be vaccinating.", patientinfo.isApprovedForVaccination());

                    req.using("Patient must receive a Pfizer or Moderna vaccine.", patientinfo.getFirstDoseManufacturer().equalsIgnoreCase("pfizer") || patientinfo.getFirstDoseManufacturer().equalsIgnoreCase("moderna"));
                    req.using("Patient must receive a Pfizer or Moderna vaccine.", patientinfo.getSecondDoseManufacturer().equalsIgnoreCase("pfizer") || patientinfo.getSecondDoseManufacturer().equalsIgnoreCase("moderna"));

                    return null;
                });
            }
        });

        // store the transaction into data base.
        subFlow(new ReceiveFinalityFlow(counterpartySession, signedTransaction.getId()));
        return null;
    }

}
