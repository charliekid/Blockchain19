package com.template.states;

import com.template.contracts.PatientContract;
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

    private boolean vaccinationProcessComplete;


    private final String firstDoseLot;
    private final String firstDoseManufacturer;
    private final String secondDoseManufacturer;
    private final String secondDoseLot;

    private final Party patientFullName;
    private final Party doctor;
    private final Party patientEmployer;
    /* Constructor of your Corda state */

    public PatientInfoState(String firstName,
                            String lastName,
                            int dose,
                            boolean vaccinationProcessComplete,
                            String firstDoseLot,
                            String firstDoseManufacturer,
                            String secondDoseLot,
                            String secondDoseManufacturer,
                            Party patientFullName,
                            Party doctor,
                            Party patientEmployer) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dose = dose;
        this.patientFullName = patientFullName;
        this.doctor = doctor;
        this.patientEmployer = patientEmployer;
        this.vaccinationProcessComplete = vaccinationProcessComplete;
        this.firstDoseLot = firstDoseLot;
        this.firstDoseManufacturer = firstDoseManufacturer;
        this.secondDoseLot = secondDoseLot;
        this.secondDoseManufacturer = secondDoseManufacturer;
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

    public boolean isVaccinationProcessComplete() {
        return vaccinationProcessComplete;
    }

    public String getFirstDoseLot() {
        return firstDoseLot;
    }

    public String getFirstDoseManufacturer() {
        return firstDoseManufacturer;
    }

    public String getSecondDoseManufacturer() {
        return secondDoseManufacturer;
    }

    public String getSecondDoseLot() {
        return secondDoseLot;
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
