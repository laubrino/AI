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

    @Test
    void testToString() {
        System.out.println(new Environment());
    }

    @Test
    void testInvalidStep() {
        Assumptions.assumeTrue(Environment.BOARD_SIZE == 8);

        Environment environment = new Environment();

        Observer observer = stepResult -> {
            assertTrue(stepResult.isDone());
            assertEquals(StepResult.Status.ILLEGAL_ACTION, stepResult.getStatus());
            assertTrue(stepResult.getReward() < 0f);
        };
        environment.addObserver(WHITE, observer);

        for (int[] coordinate : new int[][]{{0,0}, {2,2}, {3,2}, {5,2}, {5,4}, {4,5}, {2,5}, {2,3}}) {
            environment.step(new Action(coordinate[0], coordinate[1], WHITE));
        }
    }

    @Test
    void testStep(){
        Assumptions.assumeTrue(Environment.BOARD_SIZE == 8);

        Environment environment = new Environment();

        Observer observer = stepResult -> {
            assertFalse(stepResult.isDone());
            assertFalse(stepResult.isDone());
            assertEquals(StepResult.Status.CONTINUE, stepResult.getStatus());
        };
        environment.addObserver(WHITE, observer);

        for (Action action : Arrays.asList(new Action(2,4, WHITE), new Action(2,5, BLACK), new Action(4,2,WHITE),
                new Action(2,3,BLACK), new Action(1,6,WHITE), new Action(2,6,BLACK), new Action(3,6,WHITE),
                new Action(0,7,BLACK), new Action(0,6,WHITE), new Action(2,7,BLACK), new Action(1,3,WHITE),
                new Action(5,4,BLACK), new Action(4,5,WHITE), new Action(0,5,BLACK), new Action(3,5,WHITE))) {
            environment.step(action);
        }

        assertEquals("[........|........|....o...|.oooo...|..ooox..|x.xoo...|xxxo....|x.x.....]", environment.getState().toString());

    }

    @Test
    void testStepTestOnly() {
        Environment environment = new Environment();

        String e1 = environment.toString();

        Observer observer = stepResult -> {
            assertFalse(stepResult.isDone());
            assertEquals(StepResult.Status.CONTINUE, stepResult.getStatus());
        };
        environment.addObserver(WHITE, observer);

        environment.doStep(new Action(2,4, WHITE), true);

        assertEquals(e1,environment.toString());
    }

    @Test
    void findAvailableActions() {
        Environment environment = new Environment();
        System.out.println(environment);

        Set<Action> availableActions = environment.getAvailableActions();
        System.out.println(availableActions.toString());
    }

    /**
     * Play shortest game
     */
    @Test
    void testIsGameOver() {
        Assumptions.assumeTrue(Environment.BOARD_SIZE == 8);

        Environment environment = new Environment();

        assertFalse(environment.checkGameOver());
        environment.step(new Action(3,2,BLACK));
        assertFalse(environment.checkGameOver());
        environment.step(new Action(2,2,WHITE));
        assertFalse(environment.checkGameOver());
        environment.step(new Action(1,2,BLACK));
        assertFalse(environment.checkGameOver());
        environment.step(new Action(3,1,WHITE));
        assertFalse(environment.checkGameOver());
        environment.step(new Action(4,0,BLACK));
        assertFalse(environment.checkGameOver());
        environment.step(new Action(3,5,WHITE));
        assertFalse(environment.checkGameOver());
        environment.step(new Action(3,6,BLACK));
        assertFalse(environment.checkGameOver());
        environment.step(new Action(4,2,WHITE));
        assertFalse(environment.checkGameOver());

        environment.step(new Action(5,3,BLACK));
        assertTrue(environment.checkGameOver());
        System.out.println(environment);
        System.out.println(environment.getState().toString());
    }

    @Test
    void testIsThereMove(){
        Environment environment = new Environment();

        assertTrue(environment.isThereMove(WHITE));
        assertTrue(environment.isThereMove(BLACK));

        new Games().playShortestGame(environment);

        assertFalse(environment.isThereMove(WHITE));
        assertFalse(environment.isThereMove(BLACK));
    }
}