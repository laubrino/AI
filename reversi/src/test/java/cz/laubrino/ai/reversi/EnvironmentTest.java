package cz.laubrino.ai.reversi;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Set;

import static cz.laubrino.ai.reversi.Policko.BLACK;
import static cz.laubrino.ai.reversi.Policko.WHITE;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author tomas.laubr on 1.11.2019.
 */
class EnvironmentTest {

    @org.junit.jupiter.api.Test
    void testToString() {
        System.out.println(new Environment());
    }

    @Test
    void testInvalidStep() {
        Assumptions.assumeTrue(Environment.BOARD_SIZE == 8);

        Environment environment = new Environment();
        StepResult stepResult;

        for (int[] coordinate : new int[][]{{0,0}, {2,2}, {3,2}, {5,2}, {5,4}, {4,5}, {2,5}, {2,3}}) {
            stepResult = environment.step(new Action(coordinate[0], coordinate[1], WHITE));
            assertTrue(stepResult.isDone());
            assertEquals(StepResult.Reason.INVALID_MOVE, stepResult.getReason());
            assertTrue(stepResult.getReward() < 0f);
        }
    }

    @Test
    void testStep(){
        Assumptions.assumeTrue(Environment.BOARD_SIZE == 8);

        Environment environment = new Environment();
        StepResult stepResult = null;

        for (Action action : Arrays.asList(new Action(2,4, WHITE), new Action(2,5, BLACK), new Action(4,2,WHITE),
                new Action(2,3,BLACK), new Action(1,6,WHITE), new Action(2,6,BLACK), new Action(3,6,WHITE),
                new Action(0,7,BLACK), new Action(0,6,WHITE), new Action(2,7,BLACK), new Action(1,3,WHITE),
                new Action(5,4,BLACK), new Action(4,5,WHITE), new Action(0,5,BLACK), new Action(3,5,WHITE))) {
            stepResult = environment.step(action);
            assertFalse(stepResult.isDone());
            assertEquals(StepResult.Reason.CONTINUE, stepResult.getReason());
        }

        assertEquals("[........|........|....o...|.oooo...|..ooox..|x.xoo...|xxxo....|x.x.....]", stepResult.getState().toString());

    }

    @Test
    void testStepTestOnly() {
        Environment environment = new Environment();

        String e1 = environment.toString();

        StepResult stepResult = environment.doStep(new Action(2,4, WHITE), true);
        assertFalse(stepResult.isDone());
        assertEquals(StepResult.Reason.CONTINUE, stepResult.getReason());

        assertEquals(e1,environment.toString());
    }

    @Test
    void findAvailableActions() {
        Environment environment = new Environment();
        System.out.println(environment);

        Set<Action> availableActions = environment.findAvailableActions();
        System.out.println(availableActions.toString());
    }

    /**
     * Play shortest game
     */
    @Test
    void testIsGameOver() {
        Assumptions.assumeTrue(Environment.BOARD_SIZE == 8);

        Environment environment = new Environment();

        assertFalse(environment.isGameOver());
        environment.step(new Action(3,2,BLACK));
        assertFalse(environment.isGameOver());
        environment.step(new Action(2,2,WHITE));
        assertFalse(environment.isGameOver());
        environment.step(new Action(1,2,BLACK));
        assertFalse(environment.isGameOver());
        environment.step(new Action(3,1,WHITE));
        assertFalse(environment.isGameOver());
        environment.step(new Action(4,0,BLACK));
        assertFalse(environment.isGameOver());
        environment.step(new Action(3,5,WHITE));
        assertFalse(environment.isGameOver());
        environment.step(new Action(3,6,BLACK));
        assertFalse(environment.isGameOver());
        environment.step(new Action(4,2,WHITE));
        assertFalse(environment.isGameOver());

        environment.step(new Action(5,3,BLACK));
        assertTrue(environment.isGameOver());
        System.out.println(environment);
        System.out.println(environment.getState().toString());
    }
}