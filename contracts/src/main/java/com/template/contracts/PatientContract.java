package com.template.contracts;
import com.template.states.PatientInfoState;
import com.template.states.TemplateState;
import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.CommandWithParties;
import net.corda.core.contracts.Contract;
import net.corda.core.transactions.LedgerTransaction;

import static net.corda.core.contracts.ContractsDSL.requireSingleCommand;
import static net.corda.core.contracts.ContractsDSL.requireThat;
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

        if (commandData.equals(new PatientContract.Commands.SendInfo())) {
            //Retrieve the output state of the transaction
            PatientInfoState output = tx.outputsOfType(PatientInfoState.class).get(0);

            //Using Corda DSL function requireThat to replicate conditions-checks
            requireThat(require -> {
                require.using("No inputs should be consumed when sending the just patient information", tx.getInputStates().size() == 0);
                //require.using("Patient should currently not have any doses", output.getDose() != 0);
                return null;
            });
        }
    }

    // Used to indicate the transaction's intent.
    public interface Commands extends CommandData {
        //In our hello-world app, We will only have one command.
        class SendInfo implements PatientContract.Commands {}
    }
}