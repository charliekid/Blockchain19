package com.template.contracts;
import com.template.states.PatientInfoState;
import com.template.states.TemplateState;
import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.CommandWithParties;
import net.corda.core.contracts.Contract;
import net.corda.core.contracts.ContractState;
import net.corda.core.transactions.LedgerTransaction;

import static net.corda.core.contracts.ContractsDSL.*;
import java.util.List;

// ************
// * Contract *
// ************
public class PatientContract implements Contract {
    // This is used to identify our contract when building a transaction.
    public static final String ID = "com.template.contacts.PatientContract";

    // A transaction is valid if the verify() function of the contract of all the transaction's input and output states
    // does not throw an exception.
    @Override
    public void verify(LedgerTransaction tx) {

        /* We can use the requireSingleCommand function to extract command data from transaction.
         * However, it is possible to have multiple commands in a signle transaction.*/
        final CommandWithParties<PatientContract.Commands> command = requireSingleCommand(tx.getCommands(), PatientContract.Commands.class);
        final PatientContract.Commands commandData = command.getValue();

        List<ContractState> inputs = tx.getInputStates();
        List<ContractState> outputs = tx.getInputStates();
        List<CommandWithParties<CommandData>> commands = tx.getCommands();

        if (command.getValue() instanceof PatientContract.Commands.SendInfo) {
            requireThat(req -> {

                return null;
            });
        }


        if (commandData.equals(new PatientContract.Commands.SendInfo())) {
            //Retrieve the output state of the transaction
            PatientInfoState output = tx.outputsOfType(PatientInfoState.class).get(0);

            //Using Corda DSL function requireThat to replicate conditions-checks
            requireThat(require -> {
                require.using("No inputs should be consumed when sending just the patient information", tx.getInputStates().size() == 0);
                require.using("Patient should currently not have any doses", output.getDose() != 0);
                return null;
            });
        }

        // todo: modify the above template for the commands accordingly for inputs
        if (commandData.equals(new PatientContract.Commands.ApprovePatient())) {
            //Retrieve the output state of the transaction
            PatientInfoState output = tx.outputsOfType(PatientInfoState.class).get(0);

            //Using Corda DSL function requireThat to replicate conditions-checks
            requireThat(require -> {
//                require.using("No inputs should be consumed when sending just the patient information", tx.getInputStates().size() == 0);


                require.using("Patient should currently not have any doses", output.getDose() != 0);
                return null;
            });
        }

        // todo: modify the above template for the commands accordingly for inputs
        if (commandData.equals(new PatientContract.Commands.AdministerFirstDose())) {
            //Retrieve the output state of the transaction
            PatientInfoState output = tx.outputsOfType(PatientInfoState.class).get(0);

            //Using Corda DSL function requireThat to replicate conditions-checks
            requireThat(require -> {
//                require.using("No inputs should be consumed when sending just the patient information", tx.getInputStates().size() == 0);

                require.using("Patient should currently not have any doses", output.getDose() != 0);
                return null;
            });
        }

        // todo: modify the above template for the commands accordingly for inputs
        if (commandData.equals(new PatientContract.Commands.AdministerSecondDose())) {
            //Retrieve the output state of the transaction
            PatientInfoState output = tx.outputsOfType(PatientInfoState.class).get(0);

            //Using Corda DSL function requireThat to replicate conditions-checks
            requireThat(require -> {
//                require.using("No inputs should be consumed when sending just the patient information", tx.getInputStates().size() == 0);

                require.using("Patient should currently have one dose.", output.getDose() != 1);
                return null;
            });
        }

        // todo: modify the above template for the commands accordingly for inputs
        if (commandData.equals(new PatientContract.Commands.ApprovePatientForWork())) {
            //Retrieve the output state of the transaction
            PatientInfoState output = tx.outputsOfType(PatientInfoState.class).get(0);

            //Using Corda DSL function requireThat to replicate conditions-checks
            requireThat(require -> {
//                require.using("No inputs should be consumed when sending just the patient information", tx.getInputStates().size() == 0);

                // todo: check for input with pfizer, moderna
                require.using("Patient should have their two doses.", !(output.getDose() == 2 || output.getFirstDoseManufacturer() == null));
                return null;
            });
        }




    }

    // Used to indicate the transaction's intent.
    public interface Commands extends CommandData {
        // Multiple commands for
        class SendInfo implements PatientContract.Commands {}
        class ApprovePatient implements PatientContract.Commands {}
        class AdministerOneVaccine implements PatientContract.Commands {} // dont worry aboout this command till later
        class AdministerFirstDose implements PatientContract.Commands {}
        class AdministerSecondDose implements PatientContract.Commands {}
        class ApprovePatientForWork implements PatientContract.Commands {}

    }
}