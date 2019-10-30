package cz.laubrino.ai.patnact;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author tomas.laubr on 29.10.2019.
 */
class QTableTest {

    @Test
    void testSetAndGet() {
        QTable qTable = new QTable();
        qTable.set(new State("state1"), Action.MOVE_DOWN, 1f);
        assertEquals(1f, qTable.get(new State("state1"), Action.MOVE_DOWN));

        assertEquals(0f, qTable.get(new State("state2"), Action.MOVE_RIGHT));
    }

    @Test
    void testPrint() {
        QTable qTable = new QTable();
        qTable.set(new State("state1"), Action.MOVE_DOWN, 1f);
        qTable.set(new State("state2"), Action.MOVE_LEFT, 2f);

        qTable.print(System.out);
    }

    @Test
    void testMax() {
        QTable qTable = new QTable();
        assertEquals(0f, qTable.max(new State("state1")));

        qTable.set(new State("state1"), Action.MOVE_DOWN, 1f);
        assertEquals(1f, qTable.max(new State("state1")));

        qTable.set(new State("state1"), Action.MOVE_DOWN, 2f);
        assertEquals(2f, qTable.max(new State("state1")));

        qTable.set(new State("state1"), Action.MOVE_LEFT, 3f);
        assertEquals(3f, qTable.max(new State("state1")));

    }

    @Test
    void testMaxAction() {
        QTable qTable = new QTable();

        qTable.set(new State("state1"), Action.MOVE_DOWN, 1f);
        assertEquals(Action.MOVE_DOWN, qTable.maxAction(new State("state1")));

        qTable.set(new State("state1"), Action.MOVE_DOWN, 2f);
        assertEquals(Action.MOVE_DOWN, qTable.maxAction(new State("state1")));

        qTable.set(new State("state1"), Action.MOVE_LEFT, 3f);
        assertEquals(Action.MOVE_LEFT, qTable.maxAction(new State("state1")));

    }

}