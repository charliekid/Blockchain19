package com.template.contracts;

import com.template.states.PatientInfoState;
import net.corda.core.contracts.ContractState;
import net.corda.core.identity.CordaX500Name;
import net.corda.testing.core.TestIdentity;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

public class StateTests {

    private final TestIdentity charlie = new TestIdentity(new CordaX500Name("Charlie", "", "GB"));
    private final TestIdentity jorge = new TestIdentity(new CordaX500Name("Jorge", "", "GB"));
    private final TestIdentity marc = new TestIdentity(new CordaX500Name("Marc", "", "GB"));
    private final TestIdentity jonathan =  new TestIdentity(new CordaX500Name("Jonathan", "", "GB"));
    @Test
    public void patientInfoStateConstructor() {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        Date placeholder = new Date();

        try {
            placeholder = parser.parse("0000-00-00");

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }


        PatientInfoState patientInfoState = new PatientInfoState("marc",
                "alejandro",
                0,
                false,
                placeholder,
                "none",
                "none",
                placeholder,
                "none",
                "none",
                false,
                marc.getParty(),
                jorge.getParty(),
                charlie.getParty(),
                jonathan.getParty());
    }

    @Test
    public void tokenStateHasGettersForIssuerOwnerAndAmount() {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        Date placeholder = new Date();

        try {
            placeholder = parser.parse("0000-00-00");

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }


        PatientInfoState patientInfoState = new PatientInfoState("marc",
                "alejandro",
                0,
                false,
                placeholder,
                "none",
                "none",
                placeholder,
                "none",
                "none",
                false,
                marc.getParty(),
                jorge.getParty(),
                charlie.getParty(),
                jonathan.getParty());

        assertEquals(0, patientInfoState.getDose());
        assertFalse(patientInfoState.isVaccinationProcessComplete());
        assertEquals(patientInfoState.getFirstDoseLot(), "none");
        assertEquals(patientInfoState.getFirstDoseManufacturer(), "none");
        assertEquals(patientInfoState.getSecondDoseLot(), "none");
        assertEquals(patientInfoState.getSecondDoseManufacturer(), "none");
        assertEquals(marc.getParty(), patientInfoState.getPatientFullName());
        assertEquals(jorge.getParty(), patientInfoState.getDoctor());
        assertEquals(charlie.getParty(), patientInfoState.getPatientEmployer());
    }
//
    @Test
    public void tokenStateImplementsContractState() {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        Date placeholder = new Date();

        try {
            placeholder = parser.parse("0000-00-00");

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        assertTrue(new PatientInfoState("marc",
                "alejandro",
                0,
                false,
                placeholder,
                "none",
                "none",
                placeholder,
                "none",
                "none",
                false,
                marc.getParty(),
                jorge.getParty(),
                charlie.getParty(),
                jonathan.getParty()) instanceof ContractState);
    }

    @Test
    public void patientStateHasPatientDoctorEmployerAndClinicAdmin() {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        Date placeholder = new Date();

        try {
            placeholder = parser.parse("0000-00-00");

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }


        PatientInfoState patientInfoState = new PatientInfoState("marc",
                "alejandro",
                0,
                false,
                placeholder,
                "none",
                "none",
                placeholder,
                "none",
                "none",
                false,
                marc.getParty(),
                jorge.getParty(),
                charlie.getParty(),
                jonathan.getParty());
        assertEquals(4, patientInfoState.getParticipants().size());
        assertTrue(patientInfoState.getParticipants().contains(marc.getParty()));
        assertTrue(patientInfoState.getParticipants().contains(jorge.getParty()));
        assertTrue(patientInfoState.getParticipants().contains(charlie.getParty()));
        assertTrue(patientInfoState.getParticipants().contains(jonathan.getParty()));
    }

}