package cz.laubrino.ai.framework;

import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author tomas.laubr on 26.11.2019.
 */
class QTableTest {
    enum Actions {
        UP, DOWN;
    }

    static class TestingState extends State {
        private final String state;

        TestingState(String state) {
            this.state = state;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TestingState that = (TestingState) o;
            return Objects.equals(state, that.state);
        }

        @Override
        public int hashCode() {
            return Objects.hash(state);
        }

        @Override
        public String toString() {
            return state;
        }
    }


    @Test
    void testSetAndGet() {
        QTable<Actions> qTable = new QTable<>(Actions.class);

        TestingState s1 = new TestingState("S1");

        assertEquals(0, qTable.get(s1, Actions.UP));
        assertEquals(0, qTable.get(s1, Actions.UP));
        assertEquals(0, qTable.get(s1, Actions.DOWN));

        qTable.set(s1, Actions.UP, (short)10);
        assertEquals(10, qTable.get(s1, Actions.UP));
        assertEquals(0, qTable.get(s1, Actions.DOWN));
    }

    @Test
    void testMax() {
        QTable<Actions> qTable = new QTable<>(Actions.class);

        TestingState s1 = new TestingState("S1");
        assertEquals(0, qTable.max(s1));

        qTable.set(s1, Actions.UP, (short)10);
        assertEquals(10, qTable.max(s1));

        qTable.set(s1, Actions.DOWN, (short)10);
        assertEquals(10, qTable.max(s1));

        qTable.set(s1, Actions.DOWN, (short)20);
        assertEquals(20, qTable.max(s1));
    }

    @Test
    void testMaxAction() {
        QTable<Actions> qTable = new QTable<>(Actions.class);

        TestingState s1 = new TestingState("S1");
        assertNotNull(qTable.maxAction(s1));

        qTable.set(s1, Actions.UP, (short)10);
        assertEquals(Actions.UP, qTable.maxAction(s1));

        qTable.set(s1, Actions.DOWN, (short)20);
        assertEquals(Actions.DOWN, qTable.maxAction(s1));
    }

    @Test
    void testMaxActionRandomness() {
        QTable<Actions> qTable = new QTable<>(Actions.class);

        TestingState s1 = new TestingState("S1");
        qTable.set(s1, Actions.DOWN, (short)20);
        qTable.set(s1, Actions.UP, (short)20);
        int upCounter = 0;
        int downCounter = 0;
        for (int i=0;i<100;i++) {
            if (qTable.maxAction(s1) == Actions.UP) {
                upCounter++;
            } else {
                downCounter++;
            }
        }

        assertNotEquals(0, upCounter);
        assertNotEquals(0, downCounter);
    }
}