package flashcards.repository;

import flashcards.exception.DuplicateDefinitionException;
import flashcards.exception.DuplicateTermException;
import flashcards.model.Card;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryCardRepository implements CardRepository {
    private final Set<Card> data;

    public InMemoryCardRepository() {
        this.data = new HashSet<>();
    }

    @Override
    public void add(String term, String definition) throws DuplicateTermException, DuplicateDefinitionException {
        if (termExists(term)) {
            throw new DuplicateTermException();
        }
        if (definitionExists(definition)) {
            throw new DuplicateDefinitionException();
        }
        data.add(new Card(term, definition, 0));
    }

    @Override
    public void delete(String term) {
        data.removeIf(c -> c.getTerm().equals(term));
    }

    @Override
    public boolean termExists(String term) {
        return data.stream().anyMatch(c -> c.getTerm().equals(term));
    }

    @Override
    public boolean definitionExists(String definition) {
        return data.stream().anyMatch(c -> c.getDefinition().equals(definition));
    }

    @Override
    public int addMany(List<Card> cards) {
        cards.forEach(card -> {
            if (termExists(card.getTerm())) {
                data.removeIf(c -> c.getTerm().equals(card.getTerm()));
            }
            data.add(card);
        });
        return cards.size();
    }

    @Override
    public List<Card> getAsList() {
        return new ArrayList<>(data);
    }
    @Override
    public String getTermForDefinition(String definition) {
        return data.stream()
                .filter(e -> e.getDefinition().equals(definition))
                .findFirst()
                .orElseThrow(RuntimeException::new)
                .getTerm();
    }

    @Override
    public List<Card> getHardestCards() {
        int maxMistakes = data.stream().mapToInt(Card::getMistakeCount).max().orElse(0);
        if (maxMistakes == 0) {
            return List.of();
        }
        return data.stream()
                .filter(c -> c.getMistakeCount() == maxMistakes)
                .collect(Collectors.toList());
    }

    @Override
    public boolean guess(Card card, String definitionGuess) {
        return data.stream()
                .filter(c -> c.getTerm().equals(card.getTerm()))
                .findFirst()
                .orElseThrow(RuntimeException::new)
                .guessTerm(definitionGuess);
    }

    @Override
    public void incrementMistakeCount(Card card) {
        data.stream().filter(c -> c.getTerm().equals(card.getTerm()))
                .findFirst()
                .orElseThrow(RuntimeException::new)
                .incrementMistakeCount();
    }

    @Override
    public void resetStatistics() {
        data.forEach(Card::resetMistakeCount);
    }
}
