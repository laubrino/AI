package cz.laubrino.ai.piskvorky;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author tomas.laubr on 10.10.2019.
 */
class StateTest {

    @Test
    void testGetAvailableActions() {
        Board board = new Board(3);
        State state = new State(board,5,Field.X);

        assertEquals(9, state.getAvailableActions().size());

        board.put(1,2,Field.O);
        assertEquals(8, state.getAvailableActions().size());
        assertTrue(state.getAvailableActions().stream().noneMatch(action -> action.position.x == 1 && action.position.y == 2));
        assertFalse(state.getAvailableActions().stream().noneMatch(action -> action.position.x == 0 && action.position.y == 0));

        board.fillBoard(Field.O);
        assertTrue(state.getAvailableActions().isEmpty());

    }

    @Test
    void testToString() {
        Board board = new Board(3);
        State state = new State(board, 5, Field.X);

        System.out.println(state);

        board.put(1,2,Field.O);
        System.out.println(state);

        board.fillBoard(Field.O);
        System.out.println(state);
    }

}