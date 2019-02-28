import ru.nsu.molochev.mtc.KtLexer
import ru.nsu.molochev.mtc.KtParser
import java.io.StringReader

fun main(args: Array<String>) {
    val reader = StringReader("3+5*2^(2--8)")
    val parser = KtParser(KtLexer(reader))
    println(parser.executeExpression())
}