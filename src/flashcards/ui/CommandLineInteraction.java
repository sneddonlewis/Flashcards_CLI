package flashcards.ui;

import flashcards.exception.DuplicateDefinitionException;
import flashcards.exception.DuplicateTermException;
import flashcards.service.CardService;
import flashcards.util.FileManager;
import flashcards.configuration.Options;
import flashcards.model.Card;

import java.io.IOException;
import java.util.List;

public class CommandLineInteraction {
    private final UserInputOutput io;
    private final CardService service;
    private final Options options;

    public CommandLineInteraction(UserInputOutput io, CardService service, Options options) {
        this.io = io;
        this.service = service;
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
        service.resetStatistics();
        io.writeLine(CommandLineResponses.RESET_STATS);
    }

    private void handleHardestCard() {
        List<Card> hardestCards = service.getHardestCards();
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
        try {
            io.writeLine(CommandLineResponses.ADD_TERM_REQUEST);
            String term = io.readLine();

            if (service.termExists(term)) {
                throw new DuplicateTermException(term);
            }
            io.writeLine(CommandLineResponses.ADD_DEFINITION_REQUEST);

            String definition = io.readLine();
            service.add(term, definition);
            var response = CommandLineResponses.pairAdded(term, definition);
            io.writeLine(response);
        } catch (DuplicateTermException e) {
            io.writeLine(CommandLineResponses.termExistsError(e.getMessage()));
            return;
        } catch (DuplicateDefinitionException e) {
            io.writeLine(CommandLineResponses.definitionExistsError(e.getMessage()));
            return;
        }
    }

    private void handleRemove() {
        io.writeLine(CommandLineResponses.REMOVE_REQUEST);
        String card = io.readLine();
        if (service.termExists(card)) {
            service.delete(card);
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
            int numberAdded = service.importFromFile(filename);
            String response = CommandLineResponses.cardsLoaded(numberAdded);
            io.writeLine(response);
        } catch (IOException ignored) {
            io.writeLine(CommandLineResponses.FILE_NOT_FOUND);
        }
    }

    private void handleExport() {
        io.writeLine(CommandLineResponses.ASK_FILE_NAME);
        String filename = io.readLine();
        handleExport(filename);
    }

    private void handleExport(String filename) {
        int persistedCardCount = service.persistToFile(filename);
        String response = CommandLineResponses.cardsSaved(persistedCardCount);
        io.writeLine(response);
    }

    private void handleAsk() {
        io.writeLine(CommandLineResponses.ASK_TIMES);
        int timesToAsk = Integer.parseInt(io.readLine());
        assessUser(timesToAsk);
    }
    private void assessUser(int count) {
        service.getSome(count).forEach(current -> {
            io.writeLine(CommandLineResponses.askForDefinition(current.getTerm()));
            String guess = io.readLine();
            String response = getResponseToGuess(current, guess);
            io.writeLine(response);
        });
    }
    private String getResponseToGuess(Card currentCard, String guess) {
        boolean result = service.guess(currentCard, guess);
        boolean definitionExists = service.definitionExists(guess);
        if (result) {
            return CommandLineResponses.CORRECT;
        }
        service.incrementMistakeCount(currentCard);
        return definitionExists ?
                CommandLineResponses.incorrectForTerm(
                        currentCard.getDefinition(),
                        service.getTermForDefinition(guess)
                ) :
                CommandLineResponses.incorrectForAnyTerm(currentCard.getDefinition());
    }
}