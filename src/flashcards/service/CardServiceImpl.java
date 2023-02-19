package flashcards.service;

import flashcards.model.Card;
import flashcards.repository.CardRepository;
import flashcards.util.FileManager;

import java.io.IOException;
import java.util.List;

public class CardServiceImpl implements CardService {
    private final CardRepository repository;

    public CardServiceImpl(CardRepository repository) {
        this.repository = repository;
    }


    @Override
    public void add(String term, String definition) {
        repository.add(term, definition);
    }

    @Override
    public void delete(String term) {
        repository.delete(term);
    }

    @Override
    public boolean termExists(String term) {
        return repository.termExists(term);
    }

    @Override
    public boolean definitionExists(String definition) {
        return repository.definitionExists(definition);
    }

    @Override
    public List<Card> getAsList() {
        return repository.getAsList();
    }

    @Override
    public String getTermForDefinition(String definition) {
        return repository.getTermForDefinition(definition);
    }

    @Override
    public List<Card> getHardestCards() {
        return repository.getHardestCards();
    }

    @Override
    public boolean guess(Card card, String definitionGuess) {
        return repository.guess(card, definitionGuess);
    }

    @Override
    public void incrementMistakeCount(Card card) {
        repository.incrementMistakeCount(card);
    }

    @Override
    public void resetStatistics() {
        repository.resetStatistics();
    }

    @Override
    public int importFromFile(String filename) throws IOException {
        List<Card> cardsToAdd = FileManager.loadFromFile(filename);
        return repository.addMany(cardsToAdd);
    }

    @Override
    public int persistToFile(String filename) {
        List<Card> cards = repository.getAsList();
        FileManager.saveToFile(filename, cards);
        return cards.size();
    }
}
