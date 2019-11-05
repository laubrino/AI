package cz.laubrino.ai.reversi;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author tomas.laubr on 5.11.2019.
 */
class ActionTest {
    @Test
    void testActionFromString() {
        assertEquals(Action.get(2,3,Policko.BLACK), Action.get("[2,3]" + Policko.BLACK.toString()));
        assertEquals(Action.getPassAction(Policko.WHITE), Action.get("[-1,-1]" + Policko.WHITE.toString()));
    }

    @Test
    void testGet() {
        Action action = Action.get(3, 4, Policko.WHITE);
        assertEquals(3, action.getX());
        assertEquals(4, action.getY());
        assertEquals(Policko.WHITE, action.getP());

        action = Action.get(-1, -1, Policko.BLACK);
        assertEquals(Action.getPassAction(Policko.BLACK), action);
    }
}