package cz.laubrino.ai.patnact;

import cz.laubrino.ai.framework.ActionResult;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author tomas.laubr on 24.10.2019.
 */
class PatnactEnvironmentTest {

    @org.junit.jupiter.api.Test
    void testToString() {
        PatnactEnvironment patnactEnvironment = new PatnactEnvironment();

        System.out.println(patnactEnvironment);
        System.out.println(patnactEnvironment.getState());

        patnactEnvironment.shuffle(1000);

        System.out.println(patnactEnvironment);
        System.out.println(patnactEnvironment.getState());
        System.out.println(patnactEnvironment.getAvailableActions());

    }

    @Test
    void testIsFinalStateAchieved() {
        PatnactEnvironment patnactEnvironment = new PatnactEnvironment();

        assertTrue(patnactEnvironment.isFinalStateAchieved());

        patnactEnvironment.shuffle(1000);
        assertFalse(patnactEnvironment.isFinalStateAchieved());
    }

    @Test
    void testMoves1() {
        Assumptions.assumeTrue(PatnactEnvironment.BOARD_SIZE >= 3);

        PatnactEnvironment patnactEnvironment = new PatnactEnvironment();
        System.out.println(patnactEnvironment);
        System.out.println(patnactEnvironment.getState());

        patnactEnvironment.step(Action.MOVE_DOWN);
        System.out.println(patnactEnvironment);
        System.out.println(patnactEnvironment.getState());

        EnumSet<Action> availableActions = EnumSet.copyOf(Arrays.asList(patnactEnvironment.getAvailableActions()));
        assertEquals(3, availableActions.size());
        assertFalse(availableActions.contains(Action.MOVE_LEFT));

        ActionResult actionResult = patnactEnvironment.step(Action.MOVE_LEFT);
        assertTrue(actionResult.getReward() < 0);   // invalid move
        System.out.println(patnactEnvironment);
        System.out.println(patnactEnvironment.getState());

        actionResult = patnactEnvironment.step(Action.MOVE_UP);
        assertTrue(actionResult.getReward() > 0);
        assertTrue(actionResult.isDone());
        System.out.println(patnactEnvironment);
        System.out.println(patnactEnvironment.getState());

        availableActions = EnumSet.copyOf(Arrays.asList(patnactEnvironment.getAvailableActions()));
        assertEquals(2, availableActions.size());
        assertTrue(availableActions.contains(Action.MOVE_DOWN));
        assertTrue(availableActions.contains(Action.MOVE_RIGHT));

    }

    @Test
    void testMoves2() {
        PatnactEnvironment patnactEnvironment = new PatnactEnvironment();
        System.out.println(patnactEnvironment);
        System.out.println(patnactEnvironment.getState());
        State state = patnactEnvironment.getState();

        patnactEnvironment.step(Action.MOVE_UP);
        assertEquals(state, patnactEnvironment.getState());
        patnactEnvironment.step(Action.MOVE_LEFT);
        assertEquals(state, patnactEnvironment.getState());

        // 1
        patnactEnvironment.step(Action.MOVE_DOWN);
        patnactEnvironment.step(Action.MOVE_DOWN);
        patnactEnvironment.step(Action.MOVE_DOWN);
        System.out.println(patnactEnvironment);
        System.out.println(patnactEnvironment.getState());
        state = patnactEnvironment.getState();

        patnactEnvironment.step(Action.MOVE_DOWN);
        assertEquals(state, patnactEnvironment.getState());
        patnactEnvironment.step(Action.MOVE_LEFT);
        assertEquals(state, patnactEnvironment.getState());

        // 2
        patnactEnvironment.step(Action.MOVE_RIGHT);
        patnactEnvironment.step(Action.MOVE_RIGHT);
        patnactEnvironment.step(Action.MOVE_RIGHT);
        System.out.println(patnactEnvironment);
        System.out.println(patnactEnvironment.getState());
        state = patnactEnvironment.getState();

        patnactEnvironment.step(Action.MOVE_DOWN);
        assertEquals(state, patnactEnvironment.getState());
        patnactEnvironment.step(Action.MOVE_RIGHT);
        assertEquals(state, patnactEnvironment.getState());


        // 3
        patnactEnvironment.step(Action.MOVE_UP);
        patnactEnvironment.step(Action.MOVE_UP);
        patnactEnvironment.step(Action.MOVE_UP);
        System.out.println(patnactEnvironment);
        System.out.println(patnactEnvironment.getState());
        state = patnactEnvironment.getState();

        patnactEnvironment.step(Action.MOVE_UP);
        assertEquals(state, patnactEnvironment.getState());
        patnactEnvironment.step(Action.MOVE_RIGHT);
        assertEquals(state, patnactEnvironment.getState());

        // 4
        patnactEnvironment.step(Action.MOVE_LEFT);
        patnactEnvironment.step(Action.MOVE_LEFT);
        patnactEnvironment.step(Action.MOVE_LEFT);
        System.out.println(patnactEnvironment);
        System.out.println(patnactEnvironment.getState());
        state = patnactEnvironment.getState();

        patnactEnvironment.step(Action.MOVE_UP);
        assertEquals(state, patnactEnvironment.getState());
        patnactEnvironment.step(Action.MOVE_LEFT);
        assertEquals(state, patnactEnvironment.getState());

    }

}