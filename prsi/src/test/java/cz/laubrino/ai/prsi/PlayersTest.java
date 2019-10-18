package cz.laubrino.ai.prsi;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author tomas.laubr on 17.10.2019.
 */
class PlayersTest {

    @Test
    void next() {
        QTable qTable = new QTable();

        Player karel = new Player("Karel", qTable);
        Player franta = new Player("Franta", qTable);
        Players players = new Players(karel, franta);

        assertEquals(karel, players.next());
        assertEquals(franta, players.next());
        assertEquals(karel, players.next());
    }
}