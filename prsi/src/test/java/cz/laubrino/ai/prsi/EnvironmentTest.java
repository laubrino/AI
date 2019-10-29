package cz.laubrino.ai.prsi;

import cz.laubrino.ai.prsi.karty.Card;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * @author tomas.laubr on 17.10.2019.
 */
class EnvironmentTest {

    @Test
    void testReset() {
        QTable qTable = new QTable();

        Player karel = new Player("Karel", qTable);
        Player franta = new Player("Franta", qTable);
        Environment environment = new Environment(karel, karel, franta);

        environment.resetEnvironment(franta);
    }

    @Test
    void testNextPlayer() {
        QTable qTable = new QTable();

        Player karel = new Player("Karel", qTable);
        Player franta = new Player("Franta", qTable);
        Environment environment = new Environment(karel, karel, franta);

        assertEquals(karel, environment.getCurrentPlayer());
        environment.nextPlayer();
        assertEquals(franta, environment.getCurrentPlayer());
        environment.nextPlayer();
        assertEquals(karel, environment.getCurrentPlayer());
    }

    @Test
    void testStep() {
        QTable qTable = new QTable();

        Player karel = new Player("Karel", qTable);
        Player franta = new Player("Franta", qTable);
        Environment environment = new Environment(karel, karel, franta);

        EnumSet<Card> karelCards = environment.kartyVRukach.get(karel);
        EnumSet<Card> frantaCards = environment.kartyVRukach.get(franta);
        assertEquals(4, karelCards.size());
        assertEquals(4, frantaCards.size());
        assertEquals(karel, environment.getCurrentPlayer());

        environment.step(Action.LIZNI);
        assertEquals(5, karelCards.size());
        assertEquals(4, frantaCards.size());
        assertEquals(franta, environment.getCurrentPlayer());

        Action vynesKartu = Action.getActionByCard(frantaCards.iterator().next());
        StepResult stepResult = environment.step(vynesKartu);
        assertEquals(3, frantaCards.size());
        assertFalse(frantaCards.contains(vynesKartu.getCard()));
        assertEquals(vynesKartu.getCard(), environment.odhazovaciBalicek.peekTopCard());
        assertEquals(karel, environment.getCurrentPlayer());
    }

}