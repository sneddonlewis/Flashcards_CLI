package flashcards.ui;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandLineResponses {
    /**
     * Constant Strings
     */
    public static final String MENU =
            "Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):";
    public static final String ADD_TERM_REQUEST = "The card:";
    public static final String ADD_DEFINITION_REQUEST = "The definition of the card";
    public static String REMOVED = "The card has been removed.";
    public static String BYE = "Bye bye!";
    public static String ASK_TIMES = "How many times to ask?";
    public static String ASK_FILE_NAME = "File name:";
    public static String FILE_NOT_FOUND = "File not found.";
    public static final String CORRECT = "Correct!";
    public static final String REMOVE_REQUEST = "Which card?";
    public static final String NO_MISTAKES = "There are no cards with errors.";
    public static final String RESET_STATS = "Card statistics have been reset.";
    public static final String LOG_SAVED = "The log has been saved.";

    /**
     * Formatted Strings
     */
    public static String pairAdded(String term, String definition) {
        return String.format("The pair (\"%s\":\"%s\") has been added.", term, definition);
    }
    public static String termExistsError(String term) {
        return String.format("The card \"%s\" already exists.", term);
    }
    public static String definitionExistsError(String definition) {
        return String.format("The definition \"%s\" already exists. Try again:", definition);
    }
    public static String cannotRemove(String term) {
        return String.format("Can't remove \"%s\": there is no such card.", term);
    }
    public static String cardsLoaded(int amount) {
        return String.format("%d cards have been loaded.", amount);
    }
    public static String cardsSaved(int amount) {
        return String.format("%d cards have been saved.", amount);
    }
    public static String askForDefinition(String term) {
        return String.format("Print the definition of \"%s\":", term);
    }
    public static String incorrectForTerm(String correctDefinition, String matchingTerm) {
        return String.format(
                "Wrong. The right answer is \"%s\", but your definition is correct for \"%s\".",
                correctDefinition, matchingTerm
                );
    }
    public static String incorrectForAnyTerm(String correctDefinition) {
        return String.format(
                        "Wrong. The right answer is \"%s\"",
                        correctDefinition
                );
    }
    public static String hardestCard(String term, int mistakes) {
        return String.format(
                "The hardest card is \"%s\". You have %d errors answering it",
                term,
                mistakes
        );
    }
    public static String hardestCards(Stream<String> termStream, int mistakes) {
        String terms = termStream
                .map(str -> String.format("\"%s\"", str))
                .collect(Collectors.joining(", "));
        return String.format(
                "The hardest cards are %s you have %d errors answering them",
                terms,
                mistakes
        );
    }
}
