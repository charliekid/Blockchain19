package com.template.states;

import com.template.contracts.PatientContract;
import net.corda.core.contracts.BelongsToContract;
import net.corda.core.contracts.ContractState;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

// *********
// * State *
// *********
@BelongsToContract(PatientContract.class)


public class PatientInfoState implements ContractState {

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
    //private final boolean approvedForWork;

    private final Party patientFullName;
    private final Party doctor;
    private final Party patientEmployer;
    private final Party clinicAdmin;

    /* Constructor of your Corda state */

    public PatientInfoState(String firstName,
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
                            /*boolean approvedForWork,*/
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

    public Party getClinicAdmin() {
        return clinicAdmin;
    }

    /* This method will indicate who are the participants and required signers when
     * this state is used in a transaction. */
    @Override
    public List<AbstractParty> getParticipants() {
        return Arrays.asList(patientFullName, doctor, patientEmployer, clinicAdmin);
    }

    public boolean isApprovedForVaccination() {
        return approvedForVaccination;
    }

    public Date getFirstDoseDate() {
        return firstDoseDate;
    }

    public Date getSecondDoseDate() {
        return secondDoseDate;
    }
}
