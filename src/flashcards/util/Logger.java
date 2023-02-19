package flashcards.util;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class Logger {
    private final List<String> logs;

    public Logger(List<String> logs) {
        this.logs = logs;
    }

    public String logInput(String input) {
        logs.add(String.format("[INPUT] : %s", input) + System.lineSeparator());
        return input;
    }

    public String logOutput(String output) {
        logs.add(String.format("[OUTPUT] : %s", output) + System.lineSeparator());
        return output;
    }

    public List<String> getLogs() {
        var logBatch = new LinkedList<>(logs);
        logBatch.addFirst(
                String.format(
                        "*** Start Log Persisted at : %s ***", LocalDateTime.now()
                ) + System.lineSeparator());
        logBatch.addLast(
                String.format(
                        "*** End of Log Persisted at : %s ***\n", LocalDateTime.now()
                ) + System.lineSeparator());
        return logBatch;
    }
}
