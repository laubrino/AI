package cz.laubrino.ai.reversi;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

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
        Environment environment = new Environment();

        System.out.println(environment);

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
        Environment environment = new Environment();

        System.out.println(environment);

        StepResult stepResult;

        for (Action action : Arrays.asList(new Action(3,2, BLACK), new Action(2,4,WHITE))) {
            stepResult = environment.step(action);
            assertFalse(stepResult.isDone());
            assertEquals(StepResult.Reason.CONTINUE, stepResult.getReason());
            System.out.println(environment);
        }

    }
}