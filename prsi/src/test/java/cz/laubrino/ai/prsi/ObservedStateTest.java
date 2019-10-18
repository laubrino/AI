package cz.laubrino.ai.prsi;

import cz.laubrino.ai.prsi.karty.Card;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author tomas.laubr on 17.10.2019.
 */
class ObservedStateTest {

    @Test
    void testToString() {
        OdhazovaciBalicek odhazovaciBalicek = new OdhazovaciBalicek(Arrays.asList(Card.CERVENE_ESO, Card.ZALUDOVA_DESITKA, Card.ZELENA_SEDMA));
        Set<Card> kartyVRuce = new TreeSet<>(Arrays.asList(Card.KULOVA_OSMA, Card.CERVENA_DESITKA));
        ObservedState observedState = new ObservedState(odhazovaciBalicek, kartyVRuce, Arrays.asList(3, 5));
        System.out.println(observedState.toString());

        observedState = new ObservedState(odhazovaciBalicek, kartyVRuce, Arrays.asList(4));
        System.out.println(observedState.toString());
    }
}