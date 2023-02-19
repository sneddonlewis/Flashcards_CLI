package flashcards.service;

import flashcards.model.Card;

import java.io.IOException;
import java.util.List;

public interface CardService {
    void add(String term, String definition);

    void delete(String term);

    boolean termExists(String term);

    boolean definitionExists(String definition);

    int addMany(List<Card> cards);

    List<Card> getAsList();

    String getTermForDefinition(String definition);

    List<Card> getHardestCards();

    boolean guess(Card card, String definitionGuess);

    void incrementMistakeCount(Card card);

    void resetStatistics();

    int importFromFile(String filename) throws IOException;

    int persistToFile(String filename);
}
