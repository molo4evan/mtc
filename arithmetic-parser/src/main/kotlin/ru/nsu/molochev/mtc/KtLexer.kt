package ru.nsu.molochev.mtc

import java.io.Reader
import ru.nsu.molochev.mtc.KtLexeme.KtLexemeType.*
import ru.nsu.molochev.mtc.exceptions.KtLexerException

class KtLexer(private val reader: Reader) {
    private var current: Int = reader.read()

    fun getLexeme(): KtLexeme {
        if (current == -1){
            return KtLexeme(EOF)
        }
        while (current.toChar().isWhitespace()){
            current = reader.read()
            if (current == -1){
                return KtLexeme(EOF)
            }
        }
        val symbol = current.toChar()

        return when {
            symbol == '+' -> {
                current = reader.read()
                KtLexeme(PLUS)
            }
            symbol == '-' -> {
                current = reader.read()
                KtLexeme(MINUS)
            }
            symbol == '*' -> {
                current = reader.read()
                KtLexeme(MULTIPLE)
            }
            symbol == '/' -> {
                current = reader.read()
                KtLexeme(DIVIDE)
            }
            symbol == '^' -> {
                current = reader.read()
                KtLexeme(POWER)
            }
            symbol == '(' -> {
                current = reader.read()
                KtLexeme(OPEN_BRACER)
            }
            symbol == ')' -> {
                current = reader.read()
                KtLexeme(CLOSE_BRACER)
            }
            symbol.isDigit() -> {
                current = reader.read()
                if (current == -1){
                    KtLexeme(symbol.toString(), NUMBER)
                }
                val builder = StringBuilder(symbol.toString())
                while (current.toChar().isDigit()){
                    builder.append(current.toChar())
                    current = reader.read()
                    if (current == -1){
                        break
                    }
                }
                KtLexeme(builder.toString(), NUMBER)
            }
            else -> throw KtLexerException()
        }
    }
}