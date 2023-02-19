package flashcards;

import java.util.*;
import java.util.stream.Collectors;

public class Cards {
    private final Set<Card> data;

    public Cards() {
        this.data = new HashSet<>();
    }

    public void add(String term, String definition) {
        if (termExists(term)) {
            throw new RuntimeException("Duplicate Term");
        }
        if (definitionExists(definition)) {
            throw new RuntimeException("Duplicate Definition");
        }
        data.add(new Card(term, definition, 0));
    }

    public void delete(String term) {
        data.removeIf(c -> c.getTerm().equals(term));
    }

    public boolean termExists(String term) {
        return data.stream().anyMatch(c -> c.getTerm().equals(term));
    }

    public boolean definitionExists(String definition) {
        return data.stream().anyMatch(c -> c.getDefinition().equals(definition));
    }

    public int addMany(List<Card> cards) {
        cards.forEach(card -> {
            if (termExists(card.getTerm())) {
                data.removeIf(c -> c.getTerm().equals(card.getTerm()));
            }
            data.add(card);
        });
        return cards.size();
    }

    public List<Card> getAsList() {
        return new ArrayList<>(data);
    }
    public String getTermForDefinition(String definition) {
        return data.stream()
                .filter(e -> e.getDefinition().equals(definition))
                .findFirst()
                .orElseThrow(RuntimeException::new)
                .getTerm();
    }

    public List<Card> getHardestCards() {
        int maxMistakes = data.stream().mapToInt(Card::getMistakeCount).max().orElse(0);
        if (maxMistakes == 0) {
            return List.of();
        }
        return data.stream()
                .filter(c -> c.getMistakeCount() == maxMistakes)
                .collect(Collectors.toList());
    }

    public boolean guess(Card card, String definitionGuess) {
        return data.stream()
                .filter(c -> c.getTerm().equals(card.getTerm()))
                .findFirst()
                .orElseThrow(RuntimeException::new)
                .guessTerm(definitionGuess);
    }

    public void incrementMistakeCount(Card card) {
        data.stream().filter(c -> c.getTerm().equals(card.getTerm()))
                .findFirst()
                .orElseThrow(RuntimeException::new)
                .incrementMistakeCount();
    }

    public void resetStatistics() {
        data.forEach(Card::resetMistakeCount);
    }
}
