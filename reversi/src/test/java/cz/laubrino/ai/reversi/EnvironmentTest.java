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
            environment.step(Action.get(coordinate[0], coordinate[1], WHITE));
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

        for (Action action : Arrays.asList(Action.get(2,4, WHITE), Action.get(2,5, BLACK), Action.get(4,2,WHITE),
                Action.get(2,3,BLACK), Action.get(1,6,WHITE), Action.get(2,6,BLACK), Action.get(3,6,WHITE),
                Action.get(0,7,BLACK), Action.get(0,6,WHITE), Action.get(2,7,BLACK), Action.get(1,3,WHITE),
                Action.get(5,4,BLACK), Action.get(4,5,WHITE), Action.get(0,5,BLACK), Action.get(3,5,WHITE))) {
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

        environment.doStep(Action.get(2,4, WHITE), true);

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
        environment.step(Action.get(3,2,BLACK));
        assertFalse(environment.checkGameOver());
        environment.step(Action.get(2,2,WHITE));
        assertFalse(environment.checkGameOver());
        environment.step(Action.get(1,2,BLACK));
        assertFalse(environment.checkGameOver());
        environment.step(Action.get(3,1,WHITE));
        assertFalse(environment.checkGameOver());
        environment.step(Action.get(4,0,BLACK));
        assertFalse(environment.checkGameOver());
        environment.step(Action.get(3,5,WHITE));
        assertFalse(environment.checkGameOver());
        environment.step(Action.get(3,6,BLACK));
        assertFalse(environment.checkGameOver());
        environment.step(Action.get(4,2,WHITE));
        assertFalse(environment.checkGameOver());

        environment.step(Action.get(5,3,BLACK));
        assertTrue(environment.checkGameOver());
        System.out.println(environment);
        System.out.println(environment.getState().toString());
    }

    @Test
    void testGetAvailableActions() {
        Assumptions.assumeTrue(Environment.BOARD_SIZE == 8);

        String[] actions = new String[]{"[5,4]x", "[3,5]o", "[2,5]x","[1,5]o","[2,3]x","[5,3]o","[5,2]x","[2,2]o","[1,6]x","[1,7]o",
                "[3,2]x","[6,2]o","[0,5]x","[2,4]o","[6,3]x","[7,3]o","[1,4]x","[4,1]o","[0,7]x","[5,5]o","[1,3]x","[0,4]o",
                "[7,4]x","[0,6]o","[7,5]x","[3,6]o","[2,7]x","[6,6]o","[4,5]x","[7,6]o","[6,4]x","[5,6]o","[4,2]x","[6,5]o","[0,2]x",
                "[1,2]o","[0,3]x","[7,1]o","[3,1]x","[2,1]o","[1,1]x","[0,1]o","[2,0]x","[2,6]o","[5,1]x","[5,0]o","[6,0]x",
                "[3,7]o","[7,0]x","[3,0]o","[4,0]x","[1,0]o","[6,1]x","[7,2]o","[0,0]x"};

        Environment environment = new Environment();
        for (String action : actions) {
            environment.step(Action.get(action));
        }

        Set<Action> availableActions = environment.getAvailableActions();
        assertEquals(6, availableActions.size());
        assertTrue(availableActions.stream().filter(Action::isPassAction).anyMatch(action -> action.getP() == WHITE));
    }

    @Test
    void testGetAvailableActions2() {
        Environment environment = new Environment();
        Games games = new Games();
        games.playShortestGame(environment);

        Set<Action> availableActions = environment.getAvailableActions();
        assertEquals(2, availableActions.size());
        assertTrue(availableActions.stream().allMatch(Action::isPassAction));
   }

   @Test
    void testPassActionStep(){
       Environment environment = new Environment();

       assertFalse(environment.isGameOver());

       environment.step(Action.getPassAction(BLACK));
       assertTrue(environment.isGameOver());
   }
}