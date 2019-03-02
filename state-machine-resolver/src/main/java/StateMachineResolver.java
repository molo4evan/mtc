import ru.nsu.molochev.mtc.StateMachine;
import ru.nsu.molochev.mtc.StateMachineBuilder;
import ru.nsu.molochev.mtc.alphabets.Alphabet;
import ru.nsu.molochev.mtc.alphabets.EnglishAlphabet;
import ru.nsu.molochev.mtc.exceptions.IncorrectConfigurationException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;

public class StateMachineResolver {
    public static void main(String[] args) throws IOException, IncorrectConfigurationException {
        if (args.length < 2) return;
        boolean requireStart = Arrays.asList(args).contains("-s");
        if (requireStart && args.length < 3) return;
        int index = requireStart ? 1 : 0;
        String conf = args[index++];
        String candidate = args[index];
        Alphabet alpha = new EnglishAlphabet();
        Reader reader = new FileReader(conf);
        StateMachine machine = new StateMachineBuilder<>().build(reader, requireStart, alpha);
        reader = new FileReader(candidate);
        System.out.println(machine.resolve(reader));
    }
}
