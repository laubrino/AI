package cz.laubrino.ai.reversi;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author tomas.laubr on 1.11.2019.
 */
class StateTest {

    @Test
    void testToString() {
        Environment environment = new Environment();
        State state = new State(environment.getBoard());
        System.out.println(state);
    }
}