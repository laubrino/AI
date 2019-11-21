package cz.laubrino.ai.rubikovka;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author tomas.laubr on 21.11.2019.
 */
class EnvironmentTest {

    @Test
    void testToString() {
        Environment environment = new Environment();
        System.out.println(environment.toString());
    }
}