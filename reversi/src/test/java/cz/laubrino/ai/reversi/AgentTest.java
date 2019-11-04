package cz.laubrino.ai.reversi;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author tomas.laubr on 4.11.2019.
 */
class AgentTest {

    @Test
    void testFindAvailableMoves() {
        Environment environment = new Environment();
        State state = environment.getState();

        Agent white = new Agent(Environment.BOARD_SIZE, Policko.WHITE);
        white.observeState(state);
        Set<Action> availableMoves = white.findAvailableMoves();

        assertEquals(4, availableMoves.size());
        assertTrue(availableMoves.stream().allMatch(action -> action.getP() == Policko.WHITE));
        assertTrue(availableMoves.stream().anyMatch(action -> action.getX() == 4 && action.getY() == 2));
        assertTrue(availableMoves.stream().anyMatch(action -> action.getX() == 5 && action.getY() == 3));
        assertTrue(availableMoves.stream().anyMatch(action -> action.getX() == 2 && action.getY() == 4));
        assertTrue(availableMoves.stream().anyMatch(action -> action.getX() == 3 && action.getY() == 5));
    }
}