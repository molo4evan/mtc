import ru.nsu.molochev.mtc.Lexer;
import ru.nsu.molochev.mtc.Parser;

import java.io.Reader;
import java.io.StringReader;

public class Main {
    public static void main(String[] args) throws Exception{
        Reader reader = new StringReader("3+5*2^(2--8)");
        Parser parser = new Parser(new Lexer(reader));
        System.out.println(parser.executeExpression());
    }
}
