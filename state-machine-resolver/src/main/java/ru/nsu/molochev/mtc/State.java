package ru.nsu.molochev.mtc;

import ru.nsu.molochev.mtc.alphabets.Alphabet;
import ru.nsu.molochev.mtc.exceptions.DuplicatedTransitionException;
import ru.nsu.molochev.mtc.exceptions.NonAlphabetSymbolException;
import ru.nsu.molochev.mtc.exceptions.UndefinedTransitionException;

import java.util.HashMap;
import java.util.Map;

class State<T extends Alphabet> {
    private int index;
    private T alphabet;
    private boolean isFinal = false;
    private Map<Character, Integer> transitionFunctions = new HashMap<>();

    State(T alpha, int myIndex){
        index = myIndex;
        alphabet = alpha;
    }

    void addTransition(char c, int nextState) throws DuplicatedTransitionException, NonAlphabetSymbolException{
        if (!alphabet.isInAlphabet(c)){
            throw new NonAlphabetSymbolException();
        }
        if (containsTransition(c, nextState)){
            throw new DuplicatedTransitionException();
        }
        transitionFunctions.put(c, nextState);
    }

    private boolean containsTransition(char c, int nextState){
        return transitionFunctions.containsKey(c) && transitionFunctions.get(c).equals(nextState);
    }

    int getNextState(char c) throws NonAlphabetSymbolException, UndefinedTransitionException {
        if (!alphabet.isInAlphabet(c)){
            throw new NonAlphabetSymbolException();
        }
        if (!transitionFunctions.containsKey(c)){
            throw new UndefinedTransitionException();
        }
        return transitionFunctions.get(c);
    }

    boolean isFinal(){
        return isFinal;
    }

    void setFinal(boolean state){
        isFinal = state;
    }

    int getIndex(){
        return index;
    }
}
