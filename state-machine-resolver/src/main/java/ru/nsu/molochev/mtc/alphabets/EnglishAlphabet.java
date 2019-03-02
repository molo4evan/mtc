package ru.nsu.molochev.mtc.alphabets;

import java.util.Arrays;
import java.util.stream.IntStream;

public class EnglishAlphabet implements Alphabet {
    private int[] chars;

    public EnglishAlphabet() {
        chars = IntStream
                .concat(IntStream.rangeClosed('a', 'z'), IntStream.rangeClosed('A', 'Z'))
                .toArray();
    }

    @Override
    public int getSize() {
        return chars.length;
    }

    @Override
    public boolean isInAlphabet(char symbol) {
        return Arrays.stream(chars).anyMatch(x -> x == (int) symbol);
    }
}
