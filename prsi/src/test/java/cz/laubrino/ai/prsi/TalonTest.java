package cz.laubrino.ai.prsi;

import cz.laubrino.ai.prsi.karty.Card;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author tomas.laubr on 9.10.2019.
 */
class TalonTest {

    @Test
    void testTakeCardFromEmptyTalon() throws Exception {
        Talon t = new Talon(new ArrayList<>());

        assertThrows(RuntimeException.class, t::takeCard);
    }

    @Test
    void testTakeTheOnlyCard() throws Exception {
        Talon t = new Talon(Arrays.asList(Card.ZELENA_SEDMA));

        Card card = t.takeCard();

        assertEquals(Card.ZELENA_SEDMA, card);

        assertThrows(RuntimeException.class, t::takeCard);
    }

    @Test
    void testTakeCard() throws Exception{
        Talon t = new Talon(Arrays.asList(Card.ZELENA_SEDMA, Card.CERVENA_DESITKA, Card.KULOVA_OSMA));

        assertEquals(Card.KULOVA_OSMA, t.takeCard());
        assertEquals(Card.CERVENA_DESITKA, t.takeCard());
        assertEquals(Card.ZELENA_SEDMA, t.takeCard());

        assertThrows(RuntimeException.class, t::takeCard);
    }

    @Test
    void testInvalidTalon() {
        assertThrows(AssertionError.class, () -> new Talon(Arrays.asList(Card.ZELENA_SEDMA, Card.ZELENA_SEDMA)));
    }

    @Test
    void testToString() throws Exception {
        Talon t = new Talon(Arrays.asList(Card.ZELENA_SEDMA, Card.CERVENA_DESITKA, Card.KULOVA_OSMA));
        System.out.println(t);
    }


    @Test
    void testIsEmpty() throws Exception {
        assertFalse(new Talon(Arrays.asList(Card.ZELENA_SEDMA, Card.CERVENA_DESITKA, Card.KULOVA_OSMA)).isEmpty());
        assertTrue(new Talon(new ArrayList<>()).isEmpty());
    }
}