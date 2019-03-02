package ru.nsu.molochev.mtc;

import ru.nsu.molochev.mtc.alphabets.Alphabet;
import ru.nsu.molochev.mtc.exceptions.DuplicatedTransitionException;
import ru.nsu.molochev.mtc.exceptions.NonAlphabetSymbolException;
import ru.nsu.molochev.mtc.exceptions.UndefinedStateException;
import ru.nsu.molochev.mtc.exceptions.UndefinedTransitionException;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class StateMachine<T extends Alphabet> {
    private T alphabet;
    private Map<Integer, State<T>> states = new HashMap<>();
    private State<T> currentState;

    public StateMachine(T alpha, int starterState){
        alphabet = alpha;
        State<T> state = new State<>(alphabet, starterState);
        states.put(starterState, state);
        currentState = state;
    }

    public StateMachine(T alpha){
        this(alpha, 1);
    }

    public void addFinalState(int state){
        if (!states.containsKey(state)){
            State<T> s = new State<>(alphabet, state);
            states.put(state, s);
        }
        State s = states.get(state);
        s.setFinal(true);
    }

    public void addTransition(int from, char c, int to) throws NonAlphabetSymbolException, DuplicatedTransitionException {
        if (!alphabet.isInAlphabet(c)) throw new NonAlphabetSymbolException();
        if (!states.containsKey(from)){
            State<T> state = new State<>(alphabet, from);
            states.put(from, state);
        }
        if (!states.containsKey(to)){
            State<T> state = new State<>(alphabet, to);
            states.put(to, state);
        }
        states.get(from).addTransition(c, to);
    }

    public boolean resolve(Reader expressionReader) throws IOException {
        int symbol = expressionReader.read();
        while (symbol != -1){
            try {
                int next = currentState.getNextState((char) symbol);
                State<T> nextState = states.get(next);
                if (nextState == null) throw new UndefinedStateException();
                currentState = nextState;
                symbol = expressionReader.read();
            } catch (NonAlphabetSymbolException | UndefinedTransitionException | UndefinedStateException ex){
                return false;
            }
        }
        return currentState.isFinal();
    }
}
