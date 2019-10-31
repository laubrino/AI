package cz.laubrino.ai.patnact;

import org.junit.jupiter.api.Test;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author tomas.laubr on 24.10.2019.
 */
class EnvironmentTest {

    @org.junit.jupiter.api.Test
    void testToString() {
        Environment environment = new Environment();

        System.out.println(environment);
        System.out.println(environment.getState());

        environment.shuffle(1000);

        System.out.println(environment);
        System.out.println(environment.getState());
        System.out.println(environment.getAvailableActions());

    }

    @Test
    void testIsFinalStateAchieved() {
        Environment environment = new Environment();

        assertTrue(environment.isFinalStateAchieved());

        environment.shuffle(1000);
        assertFalse(environment.isFinalStateAchieved());
    }

    @Test
    void testMoves1() {
        Environment environment = new Environment();
        System.out.println(environment);
        System.out.println(environment.getState());

        environment.step(Action.MOVE_DOWN);
        System.out.println(environment);
        System.out.println(environment.getState());

        EnumSet<Action> availableActions = environment.getAvailableActions();
        assertEquals(3, availableActions.size());
        assertFalse(availableActions.contains(Action.MOVE_LEFT));

        ActionResult actionResult = environment.step(Action.MOVE_LEFT);
        assertTrue(actionResult.getReward() < 0);   // invalid move
        System.out.println(environment);
        System.out.println(environment.getState());

        actionResult = environment.step(Action.MOVE_UP);
        assertTrue(actionResult.getReward() > 0);
        assertTrue(actionResult.isDone());
        System.out.println(environment);
        System.out.println(environment.getState());

        availableActions = environment.getAvailableActions();
        assertEquals(2, availableActions.size());
        assertTrue(availableActions.contains(Action.MOVE_DOWN));
        assertTrue(availableActions.contains(Action.MOVE_RIGHT));

    }

    @Test
    void testMoves2() {
        Environment environment = new Environment();
        System.out.println(environment);
        System.out.println(environment.getState());
        State state = environment.getState();

        environment.step(Action.MOVE_UP);
        assertEquals(state, environment.getState());
        environment.step(Action.MOVE_LEFT);
        assertEquals(state, environment.getState());

        // 1
        environment.step(Action.MOVE_DOWN);
        environment.step(Action.MOVE_DOWN);
        environment.step(Action.MOVE_DOWN);
        System.out.println(environment);
        System.out.println(environment.getState());
        state = environment.getState();

        environment.step(Action.MOVE_DOWN);
        assertEquals(state, environment.getState());
        environment.step(Action.MOVE_LEFT);
        assertEquals(state, environment.getState());

        // 2
        environment.step(Action.MOVE_RIGHT);
        environment.step(Action.MOVE_RIGHT);
        environment.step(Action.MOVE_RIGHT);
        System.out.println(environment);
        System.out.println(environment.getState());
        state = environment.getState();

        environment.step(Action.MOVE_DOWN);
        assertEquals(state, environment.getState());
        environment.step(Action.MOVE_RIGHT);
        assertEquals(state, environment.getState());


        // 3
        environment.step(Action.MOVE_UP);
        environment.step(Action.MOVE_UP);
        environment.step(Action.MOVE_UP);
        System.out.println(environment);
        System.out.println(environment.getState());
        state = environment.getState();

        environment.step(Action.MOVE_UP);
        assertEquals(state, environment.getState());
        environment.step(Action.MOVE_RIGHT);
        assertEquals(state, environment.getState());

        // 4
        environment.step(Action.MOVE_LEFT);
        environment.step(Action.MOVE_LEFT);
        environment.step(Action.MOVE_LEFT);
        System.out.println(environment);
        System.out.println(environment.getState());
        state = environment.getState();

        environment.step(Action.MOVE_UP);
        assertEquals(state, environment.getState());
        environment.step(Action.MOVE_LEFT);
        assertEquals(state, environment.getState());

    }

}