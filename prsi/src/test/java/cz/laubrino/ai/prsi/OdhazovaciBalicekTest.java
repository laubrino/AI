package cz.laubrino.ai.prsi;

import cz.laubrino.ai.prsi.karty.Card;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author tomas.laubr on 17.10.2019.
 */
class OdhazovaciBalicekTest {

    @Test
    void peekTopCard() {
        OdhazovaciBalicek odhazovaciBalicek = new OdhazovaciBalicek(Card.ZALUDOVA_DESITKA);

        assertEquals(Card.ZALUDOVA_DESITKA, odhazovaciBalicek.peekTopCard());
        assertEquals(Card.ZALUDOVA_DESITKA, odhazovaciBalicek.peekTopCard());

        odhazovaciBalicek = new OdhazovaciBalicek(Arrays.asList(Card.ZALUDOVA_DESITKA, Card.CERVENE_ESO));

        assertEquals(Card.CERVENE_ESO, odhazovaciBalicek.peekTopCard());
    }

    @Test
    void toTalon() {
        OdhazovaciBalicek odhazovaciBalicek = new OdhazovaciBalicek(Card.ZALUDOVA_DESITKA);
        Talon talon = odhazovaciBalicek.toTalon();

        assertTrue(talon.isEmpty());
        assertEquals(Card.ZALUDOVA_DESITKA, odhazovaciBalicek.peekTopCard());

        odhazovaciBalicek = new OdhazovaciBalicek(Arrays.asList(Card.ZALUDOVA_DESITKA, Card.CERVENE_ESO));
        talon = odhazovaciBalicek.toTalon();

        assertEquals(Card.CERVENE_ESO, odhazovaciBalicek.peekTopCard());
        assertEquals(1, odhazovaciBalicek.getCards().size());
        assertEquals(1, talon.getCards().size());
        assertEquals(Card.ZALUDOVA_DESITKA, talon.takeCard());

        odhazovaciBalicek = new OdhazovaciBalicek(Arrays.asList(Card.ZALUDOVA_DESITKA, Card.CERVENE_ESO, Card.ZELENA_OSMA));
        talon = odhazovaciBalicek.toTalon();

        assertEquals(Card.ZELENA_OSMA, odhazovaciBalicek.peekTopCard());
        assertEquals(1, odhazovaciBalicek.getCards().size());
        assertEquals(2, talon.getCards().size());
        assertEquals(Card.ZALUDOVA_DESITKA, talon.takeCard());
        assertEquals(Card.CERVENE_ESO, talon.takeCard());
    }

    @Test
    void putCard() {
        OdhazovaciBalicek odhazovaciBalicek = new OdhazovaciBalicek(Card.ZALUDOVA_DESITKA);
        odhazovaciBalicek.putCard(Card.ZELENA_DESITKA);

        assertEquals(Card.ZELENA_DESITKA, odhazovaciBalicek.peekTopCard());
        assertEquals(2, odhazovaciBalicek.getCards().size());
    }

}