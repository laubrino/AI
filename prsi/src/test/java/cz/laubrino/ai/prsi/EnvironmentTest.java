package cz.laubrino.ai.prsi;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

}