package cz.laubrino.ai.prsi;

import cz.laubrino.ai.prsi.karty.Card;

import java.util.*;

/**
 * Dobiraci balicek
 * @author tomas.laubr on 9.10.2019.
 */
public class Talon {
    private List<Card> cards;

    public Talon(List<Card> cards) {
        if (!checkTalonValid(cards)) {
            throw new AssertionError("Talon not valid: " + Arrays.deepToString(cards.toArray()));
        }

        this.cards = new ArrayList<>(cards.size());
        this.cards.addAll(cards);
    }

    /**
     * Lizni si posledni kartu nahore
     * @return
     */
    public Card takeCard() {
        if (cards.isEmpty()) {
            throw new RuntimeException("Empty, no more cards");
        }

        return cards.remove(cards.size() - 1);
    }

    private static boolean checkTalonValid(List<Card> cards) {
        Set<Card> uniqueCards = new HashSet<>(cards);
        return uniqueCards.size() == cards.size();
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    @Override
    public String toString() {
        return Arrays.deepToString(cards.toArray());
    }

    public List<Card> getCards() {
        return cards;
    }

    public int size() {
        return cards.size();
    }
}
