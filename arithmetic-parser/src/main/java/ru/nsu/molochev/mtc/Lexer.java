package ru.nsu.molochev.mtc;

import ru.nsu.molochev.mtc.exceptions.LexerException;

import java.io.IOException;
import java.io.Reader;

import static ru.nsu.molochev.mtc.Lexeme.LexemeType.*;

public class Lexer {
    private Reader reader;
    private int current;

    public Lexer(Reader reader) throws IOException {
        this.reader = reader;
        current = reader.read();
    }

    public Lexeme getLexeme() throws LexerException{
        if (current == -1){
            return new Lexeme(EOF);
        }
        try {
            while (Character.isWhitespace((char)current)){
                current = reader.read();
                if (current == -1){
                    return new Lexeme(EOF);
                }
            }
            char symbol = (char)current;

            Lexeme out;
            switch (symbol) {
                case '+':
                    current = reader.read();
                    out = new Lexeme(PLUS);
                    break;
                case '-':
                    current = reader.read();
                    out = new Lexeme(MINUS);
                    break;
                case '*':
                    current = reader.read();
                    out = new Lexeme(MULTIPLE);
                    break;
                case '/':
                    current = reader.read();
                    out = new Lexeme(DIVIDE);
                    break;
                case '^':
                    current = reader.read();
                    out = new Lexeme(POWER);
                    break;
                case '(':
                    current = reader.read();
                    out = new Lexeme(OPEN_BRACER);
                    break;
                case ')':
                    current = reader.read();
                    out = new Lexeme(CLOSE_BRACER);
                    break;
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    current = reader.read();
                    if (current == -1){
                        out = new Lexeme(String.valueOf(symbol), NUMBER);
                        break;
                    }
                    StringBuilder builder = new StringBuilder(String.valueOf(symbol));
                    while (Character.isDigit((char)current)){
                        builder.append((char)current);
                        current = reader.read();
                        if (current == -1){
                            break;
                        }
                    }
                    out = new Lexeme(builder.toString(), NUMBER);
                    break;
                 default:
                     throw new LexerException();
            }
            return out;
        } catch (IOException e){
            throw new LexerException();
        }
    }
}
