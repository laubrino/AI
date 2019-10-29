package cz.laubrino.ai.prsi;

import cz.laubrino.ai.prsi.karty.Card;

import java.util.*;

/**
 * @author tomas.laubr on 17.10.2019.
 */
public class OdhazovaciBalicek {
    List<Card> cards;

    public OdhazovaciBalicek(List<Card> cards) {
        this.cards = new ArrayList<>();
        this.cards.addAll(cards);
    }

    public OdhazovaciBalicek(Card card) {
        this.cards = new ArrayList<>();
        this.cards.add(card);
    }

    /**
     * Podivej se na vrchni kartu
     * @return
     */
    public Card peekTopCard() {
        return cards.get(cards.size()-1);
    }

    /**
     * Obrati odhazovaci balicek, necha jednu kartu nahore a udela ze zbytku talon
     * @return
     */
    public Talon toTalon() {
        List<Card> newTalonCards = new ArrayList<>();
        List<Card> newOdhazovaciBalicek = new ArrayList<>();

        newOdhazovaciBalicek.add(cards.remove(cards.size()-1));

        for (int i=0;i<cards.size();i++) {
            newTalonCards.add(cards.get(cards.size()-i-1));
        }

        cards = newOdhazovaciBalicek;

        return new Talon(newTalonCards);
    }

    /**
     * Odhod kartu na odhazovaci balicek
     * @param card
     * @return
     */
    public OdhazovaciBalicek putCard(Card card) {
        cards.add(card);
        if (!checkValid(cards)) {
            throw new IllegalStateException(card + ":" + Arrays.deepToString(cards.toArray()));
        }
        return this;
    }

    private static boolean checkValid(List<Card> cards) {
        Set<Card> uniqueCards = new HashSet<>(cards);
        return uniqueCards.size() == cards.size();
    }


    public List<Card> getCards() {
        return cards;
    }

    public int size() {
        return cards.size();
    }
}
