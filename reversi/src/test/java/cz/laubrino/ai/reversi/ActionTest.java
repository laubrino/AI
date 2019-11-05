package cz.laubrino.ai.reversi;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author tomas.laubr on 5.11.2019.
 */
class ActionTest {
    @Test
    void testActionFromString() {
        assertEquals(new Action(2,3,Policko.BLACK), new Action("[2,3]" + Policko.BLACK.toString()));
        assertEquals(Action.getPassAction(Policko.WHITE), new Action("[-1,-1]" + Policko.WHITE.toString()));
    }

}