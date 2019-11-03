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

    @Test
    void testFromString(){
        Games games = new Games();
        Environment environment = new Environment();

        State state1 = new State(environment.getBoard());
        assertEquals(state1, new State(state1.toString()));

        environment.reset();
        games.playShortestGame(environment);
        state1 = new State(environment.getBoard());
        assertEquals(state1, new State(state1.toString()));

        environment.reset();
        games.playRandomGame(environment,20);
        System.out.println(environment);
        state1 = new State(environment.getBoard());
        assertEquals(state1, new State(state1.toString()));

    }
}