package cz.laubrino.ai.framework;

import org.junit.jupiter.api.Test;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author tomas.laubr on 2.12.2019.
 */
class ProcessorTest {
    @Test
    void testShouldTest() {
        AgentConfiguration agentConfiguration = new AgentConfiguration(0f, 0f, 0f, 0f);
        Processor p = new Processor(agentConfiguration, null, 1_000_000, 1000, 0.001f, 100, EnumSet.class);

        assertFalse(p.shouldTest(999));
        assertTrue(p.shouldTest(1000));
        assertFalse(p.shouldTest(1001));

        assertFalse(p.shouldTest(1999));
        assertTrue(p.shouldTest(2000));
        assertFalse(p.shouldTest(2001));

        p = new Processor(agentConfiguration, null, 5_000_000, 1000, 0.001f, 100, EnumSet.class);

        assertFalse(p.shouldTest(4999));
        assertTrue(p.shouldTest(5000));
        assertFalse(p.shouldTest(5001));

        assertFalse(p.shouldTest(4999));
        assertTrue(p.shouldTest(5000));
        assertFalse(p.shouldTest(5001));

        p = new Processor(agentConfiguration, null, 5_000_000, 1000, 0.01f, 100, EnumSet.class);

        assertFalse(p.shouldTest(4999));
        assertFalse(p.shouldTest(5000));
        assertFalse(p.shouldTest(5001));

        assertFalse(p.shouldTest(4999));
        assertFalse(p.shouldTest(5000));
        assertFalse(p.shouldTest(5001));

        assertFalse(p.shouldTest(49999));
        assertTrue(p.shouldTest(50000));
        assertFalse(p.shouldTest(50001));

        assertFalse(p.shouldTest(49999));
        assertTrue(p.shouldTest(50000));
        assertFalse(p.shouldTest(50001));

    }

}