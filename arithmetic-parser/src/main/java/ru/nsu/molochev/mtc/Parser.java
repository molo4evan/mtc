package ru.nsu.molochev.mtc;

import ru.nsu.molochev.mtc.exceptions.LexerException;
import ru.nsu.molochev.mtc.exceptions.ParserException;

import static ru.nsu.molochev.mtc.Lexeme.LexemeType.*;

public class Parser {
    private Lexer lexer;
    private Lexeme current;

    public Parser(Lexer l) throws LexerException {
        lexer = l;
        incrementCurrent();
    }

    private void incrementCurrent() throws LexerException{
        current = lexer.getLexeme();
    }

    public int executeExpression() throws LexerException, ParserException {
        int result = parseExpression();
        if (current.getType() != EOF) throw new ParserException();
        return result;
    }

    private int parseExpression() throws LexerException, ParserException {
        int tmp = parseTerm();
        Lexeme.LexemeType type = current.getType();
        while (type == PLUS || type == MINUS){
            incrementCurrent();
            if (type == PLUS){
                tmp += parseTerm();
            } else {
                tmp -= parseTerm();
            }
            type = current.getType();
        }
        return tmp;
    }

    private int parseTerm() throws LexerException, ParserException {
        int tmp = parseFactor();
        Lexeme.LexemeType type = current.getType();
        while (type == MULTIPLE || type == DIVIDE){
            incrementCurrent();
            if (type == MULTIPLE){
                tmp *= parseFactor();
            } else {
                tmp /= parseFactor();
            }
            type = current.getType();
        }
        return tmp;
    }

    private int parseFactor() throws LexerException, ParserException {
        int tmp = parsePower();
        if (current.getType() == POWER) {
            incrementCurrent();
            tmp = (int) Math.pow((double) tmp, (double) parseFactor());
        }
        return tmp;
    }

    private int parsePower() throws LexerException, ParserException {
        int out;
        switch (current.getType()) {
            case MINUS: {
                incrementCurrent();
                out = -parseAtom();
                break;
            }
            case PLUS: {
                incrementCurrent();
                out = parseAtom();
                break;
            }
            default: out = parseAtom();
        }
        return out;
    }

    private int parseAtom() throws LexerException, ParserException{
        int out;
        switch (current.getType()) {
            case OPEN_BRACER: {
                incrementCurrent();
                int tmp = parseExpression();
                if (current.getType() != CLOSE_BRACER) throw new ParserException();
                incrementCurrent();
                out = tmp;
                break;
            }
            case NUMBER: {
                int tmp = Integer.parseInt(current.getText());
                incrementCurrent();
                out = tmp;
                break;
            }
            default: throw new ParserException();
        }
        return out;
    }
}
