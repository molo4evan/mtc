import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import ru.nsu.molochev.mtc.KtLexeme
import ru.nsu.molochev.mtc.KtLexeme.KtLexemeType.*
import ru.nsu.molochev.mtc.KtLexer
import ru.nsu.molochev.mtc.KtParser
import ru.nsu.molochev.mtc.exceptions.KtLexerException
import ru.nsu.molochev.mtc.exceptions.KtParserException
import java.io.StringReader
import java.lang.ArithmeticException

class ArithmeticParserTests {

    @DisplayName("Correct lexemes")
    @Test
    fun lexerCorrectTest(){
        val reader = StringReader("+-       */^ ()0123456789")
        val lexer = KtLexer(reader)
        assertEquals(lexer.getLexeme(), KtLexeme(PLUS))
        assertEquals(lexer.getLexeme(), KtLexeme(MINUS))
        assertEquals(lexer.getLexeme(), KtLexeme(MULTIPLE))
        assertEquals(lexer.getLexeme(), KtLexeme(DIVIDE))
        assertEquals(lexer.getLexeme(), KtLexeme(POWER))
        assertEquals(lexer.getLexeme(), KtLexeme(OPEN_BRACER))
        assertEquals(lexer.getLexeme(), KtLexeme(CLOSE_BRACER))
        assertEquals(lexer.getLexeme(), KtLexeme("0123456789", NUMBER))
        for (i in 0..5){
            assertEquals(lexer.getLexeme(), KtLexeme(EOF))
        }
    }

    @DisplayName("Incorrect lexemes")
    @Test
    fun lexerIncorrectTest(){
        val reader = StringReader("17-5#")
        val lexer = KtLexer(reader)
        assertEquals(lexer.getLexeme(), KtLexeme("17", NUMBER))
        assertEquals(lexer.getLexeme(), KtLexeme(MINUS))
        assertEquals(lexer.getLexeme(), KtLexeme("5", NUMBER))
        for (i in 0..5){
            assertThrows(KtLexerException::class.java) {
                lexer.getLexeme()
            }
        }
    }

    @DisplayName("Correct number atom")
    @Test
    fun parserCorrectNumber(){
        val reader = StringReader("\t  000123456  ")
        val parser = KtParser(KtLexer(reader))
        assertEquals(parser.executeExpression(), 123456)
    }

    @DisplayName("Incorrect number atom")
    @Test
    fun parserIncorrectNumber(){
        val reader = StringReader("\t  0001234 56  ")
        val parser = KtParser(KtLexer(reader))
        assertThrows(KtParserException::class.java) {
            parser.executeExpression()
        }
    }

    @DisplayName("Correct expression atom")
    @Test
    fun parserCorrectBraces(){
        val reader = StringReader("\t ( ((( 000123456) ) ) ) ")
        val parser = KtParser(KtLexer(reader))
        assertEquals(parser.executeExpression(), 123456)
    }

    @DisplayName("Incorrect expression atom")
    @Test
    fun parserIncorrectBraces(){
        val reader = StringReader("\t ( ((( 000123456)  ) ) ")
        val parser = KtParser(KtLexer(reader))
        assertThrows(KtParserException::class.java) {
            parser.executeExpression()
        }
    }

    @DisplayName("Correct power")
    @Test
    fun parserCorrectPower(){
        val reader = StringReader(" \t -75435 \n ")
        val parser = KtParser(KtLexer(reader))
        assertEquals(parser.executeExpression(), -75435)
    }

    @DisplayName("Incorrect power")
    @Test
    fun parserIncorrectPower(){
        val reader = StringReader(" \t --75435  ")
        val parser = KtParser(KtLexer(reader))
        assertThrows(KtParserException::class.java){
            parser.executeExpression()
        }
    }

    @DisplayName("Correct factor")
    @Test
    fun parserCorrectFactor(){
        val reader = StringReader("12^2")
        val parser = KtParser(KtLexer(reader))
        assertEquals(parser.executeExpression(), 144)
    }

    @DisplayName("Incorrect factor")
    @Test
    fun parserIncorrectFactor(){
        val reader = StringReader("^12^2")
        val parser = KtParser(KtLexer(reader))
        assertThrows(KtParserException::class.java){
            parser.executeExpression()
        }
    }

    @DisplayName("Correct term")
    @Test
    fun parserCorrectTerm(){
        val reader = StringReader("3*2^3/4")
        val parser = KtParser(KtLexer(reader))
        assertEquals(parser.executeExpression(), 6)
    }

    @DisplayName("Division by zero")
    @Test
    fun parserArithmeticExceptionTerm(){
        val reader = StringReader("-135/(13+9+-22)")
        val parser = KtParser(KtLexer(reader))
        assertThrows(ArithmeticException::class.java){
            parser.executeExpression()
        }
    }

    @DisplayName("Incorrect term")
    @Test
    fun parserIncorrectTerm(){
        val reader = StringReader("2*3/5*6/")
        val parser = KtParser(KtLexer(reader))
        assertThrows(KtParserException::class.java){
            parser.executeExpression()
        }
    }

    @DisplayName("Correct complex expression")
    @Test
    fun parserCorrectExpression(){
        val reader = StringReader("+13+5*2-3^(28+-33+7)")
        val parser = KtParser(KtLexer(reader))
        assertEquals(parser.executeExpression(), 14)
    }

    @DisplayName("Incorrect complex expression")
    @Test
    fun parserIncorrectExpression(){
        val reader = StringReader("13+5*2-3^(28+-33+7)-35+135436+")
        val parser = KtParser(KtLexer(reader))
        assertThrows(KtParserException::class.java){
            parser.executeExpression()
        }
    }
}