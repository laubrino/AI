package cz.laubrino.ai.prsi;

import cz.laubrino.ai.prsi.karty.Card;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author tomas.laubr on 9.10.2019.
 */
class CardsInHandTest {

    @Test
    void testListCards() {
        CardsInHand cardsInHand = new CardsInHand(Arrays.asList(Card.ZELENA_DESITKA, Card.KULOVA_DEVITKA));
        Set<Card> cards = cardsInHand.listCards();
        assertEquals(2, cards.size());
        assertTrue(cards.contains(Card.ZELENA_DESITKA));
        assertTrue(cards.contains(Card.KULOVA_DEVITKA));
    }

    @Test
    void testPlayCard() {
        CardsInHand cardsInHand = new CardsInHand(Arrays.asList(Card.ZELENA_DESITKA, Card.KULOVA_DEVITKA));
        cardsInHand.playCard(Card.ZELENA_DESITKA);

        Set<Card> cards = cardsInHand.listCards();
        assertEquals(1, cards.size());
        assertTrue(cards.contains(Card.KULOVA_DEVITKA));
    }

    @Test
    void testPlayCardNotInHand() {
        CardsInHand cardsInHand = new CardsInHand(Arrays.asList(Card.ZELENA_DESITKA, Card.KULOVA_DEVITKA));
        assertThrows(RuntimeException.class, () -> cardsInHand.playCard(Card.CERVENY_KRAL));
    }

    @Test
    void drawCard() {
        CardsInHand cardsInHand = new CardsInHand(Arrays.asList(Card.ZELENA_DESITKA, Card.KULOVA_DEVITKA));
        cardsInHand.drawCard(Card.CERVENE_ESO);

        Set<Card> cards = cardsInHand.listCards();
        assertEquals(3, cards.size());
        assertTrue(cards.contains(Card.ZELENA_DESITKA));
        assertTrue(cards.contains(Card.KULOVA_DEVITKA));
        assertTrue(cards.contains(Card.CERVENE_ESO));
    }

    @Test
    void testToString() {
        CardsInHand cardsInHand = new CardsInHand(Arrays.asList(Card.ZELENA_DESITKA, Card.KULOVA_DEVITKA, Card.CERVENE_ESO));

        System.out.println(cardsInHand);
    }
}