package cz.laubrino.ai.prsi;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author tomas.laubr on 17.10.2019.
 */
class EnvironmentTest {

    @Test
    void reset() {
        QTable qTable = new QTable();

        Player karel = new Player("Karel", qTable);
        Player franta = new Player("Franta", qTable);
        Environment environment = new Environment(new Players(karel, franta), karel);

        environment.reset(franta);
    }
}