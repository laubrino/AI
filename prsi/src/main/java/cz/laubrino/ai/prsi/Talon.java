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
        checkTalonValid(cards);

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

    private static void checkTalonValid(List<Card> cards) {
        Set<Card> uniqueCards = new HashSet<>(cards);
        if (uniqueCards.size() != cards.size()) {
            throw new RuntimeException("Talon not valid, multiple cards");
        }
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    @Override
    public String toString() {
        return Arrays.deepToString(cards.toArray());
    }
}
