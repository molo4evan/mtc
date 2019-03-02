package ru.nsu.molochev.mtc;

import ru.nsu.molochev.mtc.alphabets.Alphabet;
import ru.nsu.molochev.mtc.exceptions.DuplicatedTransitionException;
import ru.nsu.molochev.mtc.exceptions.IncorrectConfigurationException;
import ru.nsu.molochev.mtc.exceptions.NonAlphabetSymbolException;

import java.io.BufferedReader;
import java.io.Reader;

public class StateMachineBuilder<T extends Alphabet> {
    public StateMachine<T> build(Reader configuration, boolean requireStarter, T alphabet) throws IncorrectConfigurationException{
        String[] settings = new BufferedReader(configuration).lines().toArray(String[]::new);
        StateMachine<T> stateMachine;
        int i = 0;
        String current = settings[i++];
        if (current == null) throw new IncorrectConfigurationException("Incomplete configuration");

        if (requireStarter){
            try {
                int starter = Integer.parseInt(current);
                current = settings[i++];
                if (current == null) throw new IncorrectConfigurationException("Incomplete configuration");
                stateMachine = new StateMachine<>(alphabet, starter);
            } catch (NumberFormatException ex){
                throw new IncorrectConfigurationException("Incorrect starter state format");
            }
        } else {
            stateMachine = new StateMachine<>(alphabet);
        }

        try {
            int finalStates = Integer.parseInt(current);
            current = settings[i++];
            if (current == null) throw new IncorrectConfigurationException("Incomplete configuration");

            for (int k = 0; k < finalStates; k++){
                int finalState = Integer.parseInt(current);
                current = settings[i++];
                if (current == null) throw new IncorrectConfigurationException("Incomplete configuration");
                stateMachine.addFinalState(finalState);
            }
        } catch (NumberFormatException ex){
            throw new IncorrectConfigurationException("Incorrect final state format");
        }

        try {
            int transitions = Integer.parseInt(current);

            for (int k = 0; k < transitions; ++k) {
                current = settings[i++];
                if (current == null) throw new IncorrectConfigurationException("Incomplete configuration");
                String[] params = current.split(" ");
                if (params.length != 3 || params[1].length() != 1) throw new IncorrectConfigurationException("Incorrect transition format");
                int from = Integer.parseInt(params[0]);
                char c = params[1].toCharArray()[0];
                int to = Integer.parseInt(params[2]);
                stateMachine.addTransition(from, c, to);
            }
        } catch (NumberFormatException | NonAlphabetSymbolException ex){
            throw new IncorrectConfigurationException("Incorrect transition format");
        } catch (DuplicatedTransitionException ex){
            throw new IncorrectConfigurationException("Duplicated transition");
        }
        return stateMachine;
    }
}
