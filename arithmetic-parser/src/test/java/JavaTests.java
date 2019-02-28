import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.nsu.molochev.mtc.Lexeme;
import ru.nsu.molochev.mtc.Lexer;
import ru.nsu.molochev.mtc.Parser;
import ru.nsu.molochev.mtc.exceptions.LexerException;
import ru.nsu.molochev.mtc.exceptions.ParserException;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.nsu.molochev.mtc.Lexeme.LexemeType.*;

class JavaTests {
    @DisplayName("Correct lexemes")
    @Test
    void lexerCorrectTest() throws IOException, LexerException {
        Reader reader = new StringReader("+-       */^ ()0123456789");
        Lexer lexer = new Lexer(reader);
        assertEquals(lexer.getLexeme().getType(), PLUS);
        assertEquals(lexer.getLexeme().getType(), MINUS);
        assertEquals(lexer.getLexeme().getType(), MULTIPLE);
        assertEquals(lexer.getLexeme().getType(), DIVIDE);
        assertEquals(lexer.getLexeme().getType(), POWER);
        assertEquals(lexer.getLexeme().getType(), OPEN_BRACER);
        assertEquals(lexer.getLexeme().getType(), CLOSE_BRACER);
        Lexeme l = lexer.getLexeme();
        assertEquals(l.getType(), NUMBER);
        assertEquals(l.getText(), "0123456789");
        for (int i = 0; i < 5; ++i){
            assertEquals(lexer.getLexeme().getType(), EOF);
        }
    }

    @DisplayName("Incorrect lexemes")
    @Test
    void lexerIncorrectTest() throws IOException, LexerException{
        Reader reader = new StringReader("17-5#");
        Lexer lexer = new Lexer(reader);
        Lexeme l = lexer.getLexeme();
        assertEquals(l.getType(), NUMBER);
        assertEquals(l.getText(), "17");
        assertEquals(lexer.getLexeme().getType(), MINUS);
        l = lexer.getLexeme();
        assertEquals(l.getType(), NUMBER);
        assertEquals(l.getText(), "5");
        for (int i = 0; i < 5; ++i){
            assertThrows(LexerException.class, lexer::getLexeme);
        }
    }

    @DisplayName("Correct number atom")
    @Test
    void parserCorrectNumber() throws IOException, LexerException, ParserException {
        Reader reader = new StringReader("\t  000123456  ");
        Parser parser = new Parser(new Lexer(reader));
        assertEquals(parser.executeExpression(), 123456);
    }

    @DisplayName("Incorrect number atom")
    @Test
    void parserIncorrectNumber() throws IOException, LexerException{
        Reader reader = new StringReader("\t  0001234 56  ");
        Parser parser = new Parser(new Lexer(reader));
        assertThrows(ParserException.class, parser::executeExpression);
    }

    @DisplayName("Correct expression atom")
    @Test
    void parserCorrectBraces() throws IOException, LexerException, ParserException{
        Reader reader = new StringReader("\t ( ((( 000123456) ) ) ) ");
        Parser parser = new Parser(new Lexer(reader));
        assertEquals(parser.executeExpression(), 123456);
    }

    @DisplayName("Incorrect expression atom")
    @Test
    void parserIncorrectBraces() throws IOException, LexerException{
        Reader reader = new StringReader("\t ( ((( 000123456)  ) ) ");
        Parser parser = new Parser(new Lexer(reader));
        assertThrows(ParserException.class, parser::executeExpression);
    }

    @DisplayName("Correct power")
    @Test
    void parserCorrectPower() throws IOException, LexerException, ParserException{
        Reader reader = new StringReader(" \t -75435 \n ");
        Parser parser = new Parser(new Lexer(reader));
        assertEquals(parser.executeExpression(), -75435);
    }

    @DisplayName("Incorrect power")
    @Test
    void parserIncorrectPower() throws IOException, LexerException{
        Reader reader = new StringReader(" \t --75435  ");
        Parser parser = new Parser(new Lexer(reader));
        assertThrows(ParserException.class, parser::executeExpression);
    }

    @DisplayName("Correct factor")
    @Test
    void parserCorrectFactor() throws IOException, LexerException, ParserException{
        Reader reader = new StringReader("12^2");
        Parser parser = new Parser(new Lexer(reader));
        assertEquals(parser.executeExpression(), 144);
    }

    @DisplayName("Incorrect factor")
    @Test
    void parserIncorrectFactor() throws IOException, LexerException{
        Reader reader = new StringReader("^12^2");
        Parser parser = new Parser(new Lexer(reader));
        assertThrows(ParserException.class, parser::executeExpression);
    }

    @DisplayName("Correct term")
    @Test
    void parserCorrectTerm() throws IOException, LexerException, ParserException{
        Reader reader = new StringReader("3*2^3/4");
        Parser parser = new Parser(new Lexer(reader));
        assertEquals(parser.executeExpression(), 6);
    }

    @DisplayName("Division by zero")
    @Test
    void parserArithmeticExceptionTerm() throws IOException, LexerException{
        Reader reader = new StringReader("-135/(13+9+-22)");
        Parser parser = new Parser(new Lexer(reader));
        assertThrows(ArithmeticException.class, parser::executeExpression);
    }

    @DisplayName("Incorrect term")
    @Test
    void parserIncorrectTerm() throws IOException, LexerException{
        Reader reader = new StringReader("2*3/5*6/");
        Parser parser = new Parser(new Lexer(reader));
        assertThrows(ParserException.class, parser::executeExpression);
    }

    @DisplayName("Correct complex expression")
    @Test
    void parserCorrectExpression() throws IOException, LexerException, ParserException{
        Reader reader = new StringReader("+13+5*2-3^(28+-33+7)");
        Parser parser = new Parser(new Lexer(reader));
        assertEquals(parser.executeExpression(), 14);
    }

    @DisplayName("Incorrect complex expression")
    @Test
    void parserIncorrectExpression() throws IOException, LexerException{
        Reader reader = new StringReader("13+5*2-3^(28+-33+7)-35+135436+");
        Parser parser = new Parser(new Lexer(reader));
        assertThrows(ParserException.class, parser::executeExpression);
    }
}
