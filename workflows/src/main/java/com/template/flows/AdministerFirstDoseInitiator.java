package com.template.flows;

import co.paralleluniverse.fibers.Suspendable;
import com.template.contracts.PatientContract;
import com.template.states.PatientInfoState;
import net.corda.core.contracts.Command;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.flows.*;
import net.corda.core.identity.Party;
import net.corda.core.node.ServiceHub;
import net.corda.core.node.services.*;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import net.corda.core.utilities.ProgressTracker;



import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

// ******************
// * Initiator flow *
// ******************
@InitiatingFlow
@StartableByRPC

public class AdministerFirstDoseInitiator extends FlowLogic<SignedTransaction>  {


    // We will not use these ProgressTracker for this Hello-World sample
    private final ProgressTracker progressTracker = new ProgressTracker();
    @Override
    public ProgressTracker getProgressTracker() {
        return progressTracker;
    }

    //private variables
    private final String firstName;
    private final String lastName;
    private final int dose;

    private final boolean approvedForVaccination;

    private final Date firstDoseDate;
    private final String firstDoseLot;
    private final String firstDoseManufacturer;

    private final Date secondDoseDate;
    private final String secondDoseManufacturer;
    private final String secondDoseLot;

    private final boolean vaccinationProcessComplete;

    private final Party patientFullName;
    private final Party doctor;
    private final Party patientEmployer;
    private final Party clinicAdmin;

    public AdministerFirstDoseInitiator(String firstName,
                                   String lastName,
                                   int dose,
                                   boolean approvedForVaccination,
                                   Date firstDoseDate,
                                   String firstDoseLot,
                                   String firstDoseManufacturer,
                                   Date secondDoseDate,
                                   String secondDoseLot,
                                   String secondDoseManufacturer,
                                   boolean vaccinationProcessComplete,
                                   Party patientFullName,
                                   Party doctor,
                                   Party patientEmployer,
                                   Party clinicAdmin) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dose = dose;

        this.approvedForVaccination = approvedForVaccination;

        this.firstDoseDate = firstDoseDate;
        this.firstDoseLot = firstDoseLot;
        this.firstDoseManufacturer = firstDoseManufacturer;

        this.secondDoseDate = secondDoseDate;
        this.secondDoseLot = secondDoseLot;
        this.secondDoseManufacturer = secondDoseManufacturer;

        this.vaccinationProcessComplete = vaccinationProcessComplete;

        this.patientFullName = patientFullName;
        this.doctor = doctor;
        this.patientEmployer = patientEmployer;
        this.clinicAdmin = clinicAdmin;
    }

    @Suspendable
    @Override
    public SignedTransaction call() throws FlowException {
        // TODO: facilitate inputs from ApprovePatient flow.

        if (!getOurIdentity().equals(clinicAdmin)) {
            throw new IllegalStateException("This transaction needs to be initiated by a clinic administrator.");
        }

        // Step 1. Get a reference to the notary service on our network and our key pair.
        // Note: ongoing work to support multiple notary identities is still in progress.
        final Party notary = getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0);

        // get vault
        List<StateAndRef<PatientInfoState>> patientInfoStateAndRefs = getServiceHub().getVaultService().queryBy(PatientInfoState.class).getStates();
        System.out.println(patientInfoStateAndRefs.size());

        // find incoming patient from vault
        StateAndRef<PatientInfoState> inputPatientInfoStateAndRef = patientInfoStateAndRefs
                .stream().filter(patientInfoStateAndRef -> {
                    PatientInfoState patientInfoState = patientInfoStateAndRef.getState().getData();
                    return patientInfoState.getFirstName().equals(firstName) && patientInfoState.getLastName().equals(lastName) && patientInfoState.isApprovedForVaccination();
                }).findAny().orElseThrow(() -> new IllegalArgumentException("The patient was not found."));

        PatientInfoState inputPatientInfoState = inputPatientInfoStateAndRef.getState().getData();

        // check if the patient's doctor has started this transaction
        if (!(getOurIdentity().equals(inputPatientInfoState.getClinicAdmin()))) {
            throw new IllegalStateException("The patient's clinic administrator must start this transaction.");
        }

        // check for any prior dosages
        if (inputPatientInfoState.getDose() >= 1) {
            throw new IllegalArgumentException("The patient has already received their first dose.");
        }

        //Compose the State that carries the Hello World message
        final PatientInfoState output =
                new PatientInfoState(firstName,
                        lastName,
                        dose,
                        approvedForVaccination,
                        firstDoseDate,
                        firstDoseLot,
                        firstDoseManufacturer,
                        secondDoseDate,
                        secondDoseLot,
                        secondDoseManufacturer,
                        vaccinationProcessComplete,
                        patientFullName,
                        doctor,
                        patientEmployer,
                        clinicAdmin);

        // Step 3. Create a new TransactionBuilder object.
        final TransactionBuilder builder = new TransactionBuilder(notary);

        // Step 4. Add the iou as an output state, as well as a command to the transaction builder.
        builder.addInputState(inputPatientInfoStateAndRef);
        builder.addOutputState(output);
        builder.addCommand(new PatientContract.Commands.AdministerFirstDose(),
                Arrays.asList(this.patientFullName.getOwningKey(), this.doctor.getOwningKey(), this.patientEmployer.getOwningKey(), this.clinicAdmin.getOwningKey()));

        // Step 5. Verify and sign it with our KeyPair.
        builder.verify(getServiceHub());
        final SignedTransaction ptx = getServiceHub().signInitialTransaction(builder);


        // Step 6. Collect the other party's signature using the SignTransactionFlow.
        List<Party> otherParties = output.getParticipants().stream().map(el -> (Party)el).collect(Collectors.toList());
        otherParties.remove(getOurIdentity());
        List<FlowSession> sessions = otherParties.stream().map(el -> initiateFlow(el)).collect(Collectors.toList());

        SignedTransaction stx = subFlow(new CollectSignaturesFlow(ptx, sessions));

        // Step 7. Assuming no exceptions, we can now finalise the transaction
        return subFlow(new FinalityFlow(stx, sessions));
    }

}
