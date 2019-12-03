package cz.laubrino.ai.framework.observers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author tomas.laubr on 3.12.2019.
 */
class AveragingTest {

    @Test
    void getAverageAndMarkReset() {
        Averaging instance = new Averaging();

        assertEquals(0, (int)instance.getAverageAndMarkReset());

        instance.add(1);
        instance.add(3);

        assertEquals(2, (int)instance.getAverageAndMarkReset());

        instance.add(100);
        instance.add(300);

        assertEquals(200, (int)instance.getAverageAndMarkReset());
    }

}