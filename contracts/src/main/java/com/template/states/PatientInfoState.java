package com.template.states;

import com.template.contracts.PatientContract;
import com.template.contracts.TemplateContract;
import net.corda.core.contracts.BelongsToContract;
import net.corda.core.contracts.ContractState;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;

import java.util.Arrays;
import java.util.List;

// *********
// * State *
// *********
@BelongsToContract(PatientContract.class)

public class PatientInfoState implements ContractState {

    //private variables
    private final String firstName;
    private final String lastName;
    private int dose;

    private final Party patientFullName;
    private final Party doctor;
    private final Party patientEmployer;
    /* Constructor of your Corda state */


    public PatientInfoState(String firstName, String lastName, int dose, Party patientFullName, Party doctor, Party patientEmployer) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dose = dose;
        this.patientFullName = patientFullName;
        this.doctor = doctor;
        this.patientEmployer = patientEmployer;
    }

    //getters


    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getDose() {
        return dose;
    }

    public Party getPatientFullName() {
        return patientFullName;
    }

    public Party getDoctor() {
        return doctor;
    }

    public Party getPatientEmployer() {
        return patientEmployer;
    }

    /* This method will indicate who are the participants and required signers when
     * this state is used in a transaction. */
    @Override
    public List<AbstractParty> getParticipants() {
        return Arrays.asList(patientFullName,doctor, patientEmployer);
    }
}
