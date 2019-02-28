package ru.nsu.molochev.mtc

data class KtLexeme(val text: String, val type: KtLexemeType) {
    enum class KtLexemeType(val text: String){
        MULTIPLE("*"),
        DIVIDE("/"),
        PLUS("+"),
        MINUS("-"),
        POWER("^"),
        OPEN_BRACER("("),
        CLOSE_BRACER(")"),
        NUMBER("0"),
        EOF("")
    }

    constructor(type: KtLexemeType): this(type.text, type)
}