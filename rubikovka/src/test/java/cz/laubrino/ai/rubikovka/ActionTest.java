package cz.laubrino.ai.rubikovka;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author tomas.laubr on 22.11.2019.
 */
class ActionTest {
    @Test
    void testValues() {
//        assertEquals(12, Action.VALUES.length);
        assertEquals(3, Action.VALUES.length);
    }

    @Test
    void neco() {
        short x = 324;


        short y = (short)(x * 0.9);

        System.out.println(y);
    }

}