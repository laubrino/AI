package cz.laubrino.ai.rubikovka;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Test;

import static cz.laubrino.ai.rubikovka.Action.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author tomas.laubr on 21.11.2019.
 */
class RubikEnvironmentTest {

    @Test
    void testSteps() {
        RubikEnvironment environment = new RubikEnvironment();
        environment.reset();
        String initialState = environment.toString();

        environment.step(F);
        environment.step(R);
        environment.step(D);

        assertNotEquals(initialState, environment.toString());

        environment.step(D);environment.step(D);environment.step(D);
        environment.step(R);environment.step(R);environment.step(R);
        environment.step(F);environment.step(F);environment.step(F);

        assertEquals(initialState, environment.toString());
    }

    @Test
    void testToString() {
        RubikEnvironment environment = new RubikEnvironment();
        environment.reset();
        System.out.println(environment.toString());

        environment.step(F);
        System.out.println(environment.toString());
        environment.step(F);environment.step(F);environment.step(F);

        environment.step(F);
        environment.step(R);
        environment.step(D);
        System.out.println(environment.toString());
    }

    @Test
    void testIsFinalStateAchieved() {
        RubikEnvironment environment = new RubikEnvironment();
        environment.reset();
        assertFalse(environment.isFinalStateAchieved());

        environment.resetNoShuffle();
        assertTrue(environment.isFinalStateAchieved());

        environment.step(F);
        assertFalse(environment.isFinalStateAchieved());
    }

    @Test
    void testShuffle() {
        RubikEnvironment environment = new RubikEnvironment();
        environment.reset();
        assertFalse(environment.isFinalStateAchieved());
        String s = environment.toString();

        environment.shuffle(1000);
        String s1 = environment.toString();
        System.out.println(s1);
        environment.shuffle(1000);
        String s2 = environment.toString();
        System.out.println(s2);

        assertFalse(environment.isFinalStateAchieved());
        assertNotEquals(s, s2);
        assertNotEquals(s1, s2);
    }

    @Test
    void testResetNoShufle() {
        RubikEnvironment environment = new RubikEnvironment();
        environment.reset();
        assertFalse(environment.isFinalStateAchieved());

        environment.resetNoShuffle();
        assertTrue(environment.isFinalStateAchieved());
    }

}