package flashcards;

import java.io.IOException;
import java.util.List;

public class CommandLineInterface {
    private final UserInputOutput io;
    private final Cards repository;
    private final Options options;

    public CommandLineInterface(UserInputOutput io, Cards repository, Options options) {
        this.io = io;
        this.repository = repository;
        this.options = options;
    }

    public void start() {
        var importFileOptional = options.getImportFile();
        var exportFileOptional = options.getExportFile();
        importFileOptional.ifPresent(this::handleImport);
        while (true) {
            io.writeLine(CommandLineResponses.MENU);
            String choice = io.readLine();
            switch (choice) {
                case "add" -> handleAdd();
                case "remove" -> handleRemove();
                case "import" -> handleImport();
                case "export" -> handleExport();
                case "ask" -> handleAsk();
                case "log" -> handleLog();
                case "hardest card" -> handleHardestCard();
                case "reset stats" -> handleResetStats();
                case "exit" -> {
                    exportFileOptional.ifPresent(this::handleExport);
                    io.writeLine(CommandLineResponses.BYE);
                    return;
                }
                default -> throw new RuntimeException("Invalid Menu choice");
            }
        }
    }

    private void handleResetStats() {
        repository.resetStatistics();
        io.writeLine(CommandLineResponses.RESET_STATS);
    }

    private void handleHardestCard() {
        List<Card> hardestCards = repository.getHardestCards();
        if (hardestCards.size() == 0) {
            io.writeLine(CommandLineResponses.NO_MISTAKES);
            return;
        }
        if (hardestCards.size() == 1) {
            Card card = hardestCards.stream().findFirst().get();
            var response = CommandLineResponses.hardestCard(card.getTerm(), card.getMistakeCount());
            io.writeLine(response);
            return;
        }
        var response = CommandLineResponses.hardestCards(
                hardestCards.stream().map(Card::getTerm),
                hardestCards.stream().findFirst().orElseThrow().getMistakeCount());
        io.writeLine(response);
    }

    private void handleLog() {
        io.writeLine(CommandLineResponses.ASK_FILE_NAME);
        String fileName = io.readLine();
        io.writeLine(CommandLineResponses.LOG_SAVED);
        FileManager.saveLogToFile(fileName, io.getLogs());
    }

    private void handleAdd() {
        io.writeLine(CommandLineResponses.ADD_TERM_REQUEST);
        String term = io.readLine();

        if (repository.termExists(term)) {
            io.writeLine(CommandLineResponses.termExistsError(term));
            return;
        }
        io.writeLine(CommandLineResponses.ADD_DEFINITION_REQUEST);

        String definition = io.readLine();

        if (repository.definitionExists(definition)) {
            io.writeLine(CommandLineResponses.definitionExistsError(definition));
            return;
        }
        repository.add(term, definition);
        var response = CommandLineResponses.pairAdded(term, definition);
        io.writeLine(response);
    }

    private void handleRemove() {
        io.writeLine(CommandLineResponses.REMOVE_REQUEST);
        String card = io.readLine();
        if (repository.termExists(card)) {
            repository.delete(card);
            io.writeLine(CommandLineResponses.REMOVED);
        } else {
            io.writeLine(CommandLineResponses.cannotRemove(card));
        }
    }

    private void handleImport() {
        io.writeLine(CommandLineResponses.ASK_FILE_NAME);
        String filename = io.readLine();
        handleImport(filename);
    }

    private void handleImport(String filename) {
        try {
            int numberAdded = importFromFile(filename);
            String response = CommandLineResponses.cardsLoaded(numberAdded);
            io.writeLine(response);
        } catch (IOException ignored) {
            io.writeLine(CommandLineResponses.FILE_NOT_FOUND);
        }
    }

    // CardService method
    private int importFromFile(String filename) throws IOException {
        List<Card> cardsToAdd = FileManager.loadFromFile(filename);
        return repository.addMany(cardsToAdd);
    }

    private void handleExport() {
        io.writeLine(CommandLineResponses.ASK_FILE_NAME);
        String filename = io.readLine();
        handleExport(filename);
    }

    private void handleExport(String filename) {
        int persistedCardCount = persistToFile(filename);
        String response = CommandLineResponses.cardsSaved(persistedCardCount);
        io.writeLine(response);
    }

    // CardService method
    private int persistToFile(String filename) {
        List<Card> cards = repository.getAsList();
        FileManager.saveToFile(filename, cards);
        return cards.size();
    }

    private void handleAsk() {
        io.writeLine(CommandLineResponses.ASK_TIMES);
        int timesToAsk = Integer.parseInt(io.readLine());
        assessUser(timesToAsk);
    }
    private void assessUser(int times) {
        List<Card> cards = repository.getAsList();
        for (int i = 0; i < times; i++) {
            Card current = cards.get(i);
            io.writeLine(CommandLineResponses.askForDefinition(current.getTerm()));
            String guess = io.readLine();
            String response = getResponseToGuess(current, guess);
            io.writeLine(response);
        }
    }
    private String getResponseToGuess(Card currentCard, String guess) {
        boolean result = repository.guess(currentCard, guess);
        boolean definitionExists = repository.definitionExists(guess);
        if (result) {
            return CommandLineResponses.CORRECT;
        }
        repository.incrementMistakeCount(currentCard);
        return definitionExists ?
                CommandLineResponses.incorrectForTerm(
                        currentCard.getDefinition(),
                        repository.getTermForDefinition(guess)
                ) :
                CommandLineResponses.incorrectForAnyTerm(currentCard.getDefinition());
    }
}