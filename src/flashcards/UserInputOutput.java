package flashcards;

import java.util.List;
import java.util.Scanner;

public class UserInputOutput {
    private final Logger logger;
    private final Scanner reader;

    public UserInputOutput(Logger logger, Scanner reader) {
        this.logger = logger;
        this.reader = reader;
    }

    public String readLine() {
        return logger.logInput(reader.nextLine());
    }

    public void writeLine(String line) {
        System.out.println(logger.logOutput(line));
    }

    public List<String> getLogs() {
        return logger.getLogs();
    }
}
