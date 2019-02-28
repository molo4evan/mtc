package ru.nsu.molochev.mtc

import ru.nsu.molochev.mtc.KtLexeme.KtLexemeType.*
import ru.nsu.molochev.mtc.exceptions.KtParserException
import kotlin.math.pow

class KtParser(private val lexer: KtLexer) {
    private var current = lexer.getLexeme()

    fun executeExpression(): Int {
        val result = parseExpression()
        if (current.type != EOF) throw KtParserException()
        return result
    }

    private fun incrementCurrent() {
        current = lexer.getLexeme()
    }

    private fun parseExpression(): Int {
        var tmp = parseTerm()
        var type = current.type
        while (type == PLUS || type == MINUS){
            incrementCurrent()
            if (type == PLUS){
                tmp += parseTerm()
            } else {
                tmp -= parseTerm()
            }
            type = current.type
        }
        return tmp
    }

    private fun parseTerm(): Int {
        var tmp = parseFactor()
        var type = current.type
        while (type == MULTIPLE || type == DIVIDE){
            incrementCurrent()
            if (type == MULTIPLE){
                tmp *= parseFactor()
            } else {
                tmp /= parseFactor()
            }
            type = current.type
        }
        return tmp
    }

    private fun parseFactor(): Int {
        val tmp = parsePower()
        return if (current.type == POWER) {
            incrementCurrent()
            tmp.toDouble().pow(parseFactor()).toInt()
        } else tmp
    }

    private fun parsePower(): Int {
        return when (current.type) {
            MINUS -> {
                incrementCurrent()
                -parseAtom()
            }
            PLUS -> {
                incrementCurrent()
                parseAtom()
            }
            else -> parseAtom()
        }
    }

    private fun parseAtom(): Int {
        return when (current.type) {
            OPEN_BRACER -> {
                incrementCurrent()
                val tmp = parseExpression()
                if (current.type != CLOSE_BRACER) throw KtParserException()
               incrementCurrent()
                tmp
            }
            NUMBER -> {
                val tmp = current.text.toInt()
                incrementCurrent()
                tmp
            }
            else -> throw KtParserException()
        }
    }
}