package ru.nsu.molochev.mtc;

public class Lexeme {
    public enum LexemeType{
        MULTIPLE("*"),
        DIVIDE("/"),
        PLUS("+"),
        MINUS("-"),
        POWER("^"),
        OPEN_BRACER("("),
        CLOSE_BRACER(")"),
        NUMBER("0"),
        EOF("");

        private String text;

        LexemeType(String text){ this.text = text; }

        public String getText() {
            return text;
        }
    }

    private String text;
    private LexemeType type;

    public Lexeme(String text, LexemeType type) {
        this.text = text;
        this.type = type;
    }

    public Lexeme(LexemeType type){
        this(type.getText(), type);
    }

    public String getText() {
        return text;
    }

    public LexemeType getType() {
        return type;
    }
}
