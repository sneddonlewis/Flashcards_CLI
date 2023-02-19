package flashcards.service;

import flashcards.model.Card;
import flashcards.repository.CardRepository;

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
    public int addMany(List<Card> cards) {
        return repository.addMany(cards);
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
}
