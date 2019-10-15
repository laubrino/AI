package cz.laubrino.ai.prsi;

import cz.laubrino.ai.prsi.karty.Card;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author tomas.laubr on 9.10.2019.
 */
public class CardsInHand {
    private Set<Card> cards = new HashSet<>();

    public CardsInHand(Collection<Card> cards) {
        this.cards.addAll(cards);
    }

    /**
     *
     * @return all cards in hand
     */
    public Set<Card> listCards() {
        return cards;
    }

    /**
     *
     * @param card this card will be removed from hand
     */
    public void playCard(Card card)  {
        if (!cards.remove(card)) {
            throw new RuntimeException("Invalid card, not in hand");    // should not happen
        }
    }

    public void drawCard(Card card) {
        cards.add(card);
    }

    @Override
    public String toString() {
        return Arrays.deepToString(cards.toArray());
    }
}
