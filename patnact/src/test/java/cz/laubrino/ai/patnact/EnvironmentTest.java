package cz.laubrino.ai.patnact;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author tomas.laubr on 24.10.2019.
 */
class EnvironmentTest {

    @org.junit.jupiter.api.Test
    void testToString() {
        Environment environment = new Environment();

        System.out.println(environment);

        environment.shuffle();

        System.out.println(environment);
        System.out.println(environment.hashString());
        System.out.println(environment.getAvailableActions());

    }

    @Test
    void testIsFinalStateAchieved() {
        Environment environment = new Environment();

        assertTrue(environment.isFinalStateAchieved());

        environment.shuffle();
        assertFalse(environment.isFinalStateAchieved());
    }
}