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
@InitiatedBy(AdministerFirstDoseInitiator.class)

public class AdministerFirstDoseResponder extends FlowLogic<SignedTransaction>  {

    //private variable
    private FlowSession counterpartySession;

    //Constructor
    public AdministerFirstDoseResponder(FlowSession counterpartySession) {
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
                    req.using("This patient must have one dose.", patientinfo.getDose() == 1);
                    req.using("Patient should now be vaccinating.", patientinfo.isApprovedForVaccination());
                    req.using("Patient's vaccination process is not complete.'", !patientinfo.isVaccinationProcessComplete());

                    req.using("Patient must receive a Pfizer or Moderna vaccine.", patientinfo.getFirstDoseManufacturer().equalsIgnoreCase("pfizer") || patientinfo.getFirstDoseManufacturer().equalsIgnoreCase("moderna"));
                    return null;
                });

            }
        });

        // store the transaction into data base.
        subFlow(new ReceiveFinalityFlow(counterpartySession, signedTransaction.getId()));
        return null;
    }

}
