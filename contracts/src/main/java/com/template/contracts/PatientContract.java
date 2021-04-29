package com.template.contracts;
import com.template.states.PatientInfoState;
import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.CommandWithParties;
import net.corda.core.contracts.Contract;
import net.corda.core.contracts.ContractState;
import net.corda.core.transactions.LedgerTransaction;

import static net.corda.core.contracts.ContractsDSL.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

// ************
// * Contract *
// ************
public class PatientContract implements Contract {
    // This is used to identify our contract when building a transaction.
    public static final String ID = "com.template.contracts.PatientContract";

    // A transaction is valid if the verify() function of the contract of all the transaction's input and output states
    // does not throw an exception.
    @Override
    public void verify(LedgerTransaction tx) throws IllegalArgumentException {

        /* We can use the requireSingleCommand function to extract command data from transaction.
         * However, it is possible to have multiple commands in a single transaction.*/
        final CommandWithParties<PatientContract.Commands> command = requireSingleCommand(tx.getCommands(), PatientContract.Commands.class);
        final PatientContract.Commands commandData = command.getValue();

        List<ContractState> inputs = tx.getInputStates();
        List<ContractState> outputs = tx.getInputStates();
        List<CommandWithParties<CommandData>> commands = tx.getCommands();

        if (command.getValue() instanceof Commands.SendInfo) {
            PatientInfoState output = tx.outputsOfType(PatientInfoState.class).get(0);

            //Using Corda DSL function requireThat to replicate conditions-checks
            requireThat(require -> {
                SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
                Date placeholder = new Date();

                try {
                    placeholder = parser.parse("0000-00-00");

                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }

                require.using("No inputs should be consumed when sending just the patient information", tx.getInputStates().size() == 0);
                require.using("Patient must have no prior dosages.", output.getDose() == 0);
                require.using("Patient is not yet approved for vaccination.", !output.isApprovedForVaccination());

                //check for placeholders
//                require.using("Patient should have no first dose date", output.getFirstDoseDate().equals(placeholder));
                require.using("Patient should have no first dose lot", output.getFirstDoseLot().equals("none"));
                require.using("Patient should have no first dose manufacturer", output.getFirstDoseManufacturer().equals("none"));
//                require.using("Patient should have no second dose date", output.getSecondDoseDate().equals(placeholder));
                require.using("Patient should have no second dose lot", output.getSecondDoseLot().equals("none"));
                require.using("Patient should have no second dose manufacturer", output.getSecondDoseManufacturer().equals("none"));
                require.using("Patient's vaccination process is not complete.'", !output.isVaccinationProcessComplete());

                return null;
            });

        } else if (command.getValue() instanceof Commands.ApprovePatient) {
            //Retrieve the output state of the transaction
            PatientInfoState output = tx.outputsOfType(PatientInfoState.class).get(0);

            //Retrieve the input state of the transaction
            PatientInfoState input = tx.inputsOfType(PatientInfoState.class).get(0);

            //Using Corda DSL function requireThat to replicate conditions-checks
            requireThat(require -> {
                require.using("Information for a singular patient must be consumed as input.", tx.getInputStates().size() == 1);

                //check input
                require.using("Incoming patient must have no prior dosages.", input.getDose() == 0);
                require.using("Incoming patient's vaccination process is not complete.'", !input.isVaccinationProcessComplete());
                require.using("Incoming patient is not yet approved for vaccination.", !input.isApprovedForVaccination());

                //check output
                require.using("Patient must have no prior dosages.", output.getDose() == 0);
                require.using("Patient's vaccination process is not complete.'", !output.isVaccinationProcessComplete());
                require.using("Patient should now be ready for vaccination.", output.isApprovedForVaccination());
                return null;
            });
        } else if (command.getValue() instanceof Commands.AdministerFirstDose) {
            //Retrieve the output state of the transaction
            PatientInfoState output = tx.outputsOfType(PatientInfoState.class).get(0);

            //Retrieve the input state of the transaction
            PatientInfoState input = tx.inputsOfType(PatientInfoState.class).get(0);

            //Using Corda DSL function requireThat to replicate conditions-checks
            requireThat(require -> {
                SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
                Date placeholder = new Date();

                try {
                    placeholder = parser.parse("0000-00-00");

                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }

                require.using("Information for a singular patient must be consumed as input.", tx.getInputStates().size() == 1);

                //check input
                require.using("Incoming patient must have no prior dosages.", input.getDose() == 0);
                require.using("Incoming patient's vaccination process is not complete.", !input.isVaccinationProcessComplete());
                require.using("Patient should now be ready for vaccination.", input.isApprovedForVaccination());

                //check output
                require.using("Patient must have a singular dose.", output.getDose() == 1);
                require.using("Patient's vaccination process is not complete.", !output.isVaccinationProcessComplete());
                require.using("Patient should now be ready for vaccination.", output.isApprovedForVaccination());

                require.using("Lot number required.", !(output.getFirstDoseLot().isEmpty() || output.getFirstDoseLot().equalsIgnoreCase("none")));
                require.using("Manufacturer required.", !(output.getFirstDoseManufacturer().isEmpty() || output.getFirstDoseManufacturer().equalsIgnoreCase("none")));
//                require.using("Date required.", !(output.getFirstDoseDate().equals(placeholder)));

                return null;
            });
        } else if (command.getValue() instanceof Commands.AdministerSecondDose) {
            //Retrieve the output state of the transaction
            PatientInfoState output = tx.outputsOfType(PatientInfoState.class).get(0);

            //Retrieve the input state of the transaction
            PatientInfoState input = tx.inputsOfType(PatientInfoState.class).get(0);

            //Using Corda DSL function requireThat to replicate conditions-checks
            requireThat(require -> {
                SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
                Date placeholder = new Date();

                try {
                    placeholder = parser.parse("0000-00-00");

                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }

                require.using("Information for a singular patient must be consumed as input.", tx.getInputStates().size() == 1);

                //check input
                require.using("Incoming patient must have one prior dosage.", input.getDose() == 1);
                require.using("Incoming patient's vaccination process is not complete.", !input.isVaccinationProcessComplete());
                require.using("Patient should now be ready for vaccination.", input.isApprovedForVaccination());

                //check output
                require.using("Patient must have a second dose.", output.getDose() == 2);
//                require.using("Patient's vaccination process is not complete.", !output.isVaccinationProcessComplete());
                require.using("Patient should now be ready for vaccination.", output.isApprovedForVaccination());


                require.using("Lot number required.", !(output.getFirstDoseLot().isEmpty() || output.getFirstDoseLot().equalsIgnoreCase("none")));
                require.using("Manufacturer required.", !(output.getFirstDoseManufacturer().isEmpty() || output.getFirstDoseManufacturer().equalsIgnoreCase("none")));
                require.using("Lot number required.", !(output.getSecondDoseLot().isEmpty() || output.getSecondDoseLot().equalsIgnoreCase("none")));
                require.using("Manufacturer required.", !(output.getSecondDoseManufacturer().isEmpty() || output.getSecondDoseManufacturer().equalsIgnoreCase("none")));


//                require.using("Date required.", !(output.getFirstDoseDate().equals(placeholder)));
//                require.using("Date required.", !(output.getSecondDoseDate().equals(placeholder)));

                return null;
            });
        } else if (command.getValue() instanceof Commands.ApprovePatientForWork) {
            //Retrieve the output state of the transaction
            PatientInfoState output = tx.outputsOfType(PatientInfoState.class).get(0);

            //Retrieve the input state of the transaction
            PatientInfoState input = tx.inputsOfType(PatientInfoState.class).get(0);

            //Using Corda DSL function requireThat to replicate conditions-checks
            requireThat(require -> {
                SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
                Date placeholder = new Date();

                try {
                    placeholder = parser.parse("0000-00-00");

                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }

                require.using("Information for a singular patient must be consumed as input.", tx.getInputStates().size() == 1);

                //check input
                require.using("Incoming patient must have two doses.", input.getDose() == 2);
                require.using("Incoming patient's vaccination process is not complete.", !input.isVaccinationProcessComplete());
                require.using("Patient should now be ready for vaccination.", input.isApprovedForVaccination());

                //check output
                require.using("Patient must have a second dose.", output.getDose() == 2);
                require.using("Patient's vaccination process should now be complete.", output.isVaccinationProcessComplete());
                require.using("Patient should now be ready for vaccination.", output.isApprovedForVaccination());


                require.using("Lot number required.", !(output.getFirstDoseLot().isEmpty() || output.getFirstDoseLot().equalsIgnoreCase("none")));
                require.using("Manufacturer required.", !(output.getFirstDoseManufacturer().isEmpty() || output.getFirstDoseManufacturer().equalsIgnoreCase("none")));
                require.using("Lot number required.", !(output.getSecondDoseLot().isEmpty() || output.getSecondDoseLot().equalsIgnoreCase("none")));
                require.using("Manufacturer required.", !(output.getSecondDoseManufacturer().isEmpty() || output.getSecondDoseManufacturer().equalsIgnoreCase("none")));

                /* check date here*/
//                require.using("Date required.", !(output.getFirstDoseDate().equals(placeholder)));
//                require.using("Date required.", !(output.getSecondDoseDate().equals(placeholder)));

                return null;
            });
        } else {
            throw new IllegalArgumentException("Unrecognized command.");
        }
    }

    // Used to indicate the transaction's intent.
    public interface Commands extends CommandData {
        // Multiple commands for
        class SendInfo implements PatientContract.Commands {}
        class ApprovePatient implements PatientContract.Commands {}
        class AdministerFirstDose implements PatientContract.Commands {}
        class AdministerSecondDose implements PatientContract.Commands {}
        class ApprovePatientForWork implements PatientContract.Commands {}

    }
}