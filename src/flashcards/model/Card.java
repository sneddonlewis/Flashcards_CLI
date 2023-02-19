package flashcards.model;

import java.io.Serial;
import java.io.Serializable;

public class Card implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final String term;
    private final String definition;
    private int mistakeCount;

    public Card(String term, String definition, int mistakeCount) {
        this.term = term;
        this.definition = definition;
        this.mistakeCount = mistakeCount;
    }

    public boolean guessTerm(String guess) {
        return guess.equals(definition);
    }

    public String getTerm() {
        return term;
    }

    public String getDefinition() {
        return definition;
    }

    public int getMistakeCount() {
        return mistakeCount;
    }

    public void resetMistakeCount() {
        this.mistakeCount = 0;
    }

    public void incrementMistakeCount() {
        this.mistakeCount += 1;
    }
}
