package cz.laubrino.ai.piskvorky;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author tomas.laubr on 10.10.2019.
 */
class BoardTest {

    @Test
    void testGetFieldPosition(){
        Board b = new Board(3);

        b.put(1,2, Field.O);

        assertTrue(b.getFieldPositions(Field.X).isEmpty());

        List<Position> kolecka = b.getFieldPositions(Field.O);
        assertEquals(1, kolecka.size());
        assertEquals(new Position(1,2), kolecka.get(0));

        List<Position> fieldPositions = b.getFieldPositions(Field.NIC);
        assertEquals(8, fieldPositions.size());
    }

    @Test
    void testToString() {
        Board b = new Board(3);

        System.out.println(b);

        b = new Board(5);

        System.out.println(b);
    }

    @Test
    void testFillBoard() {
        Board b = new Board(3);
        b.fillBoard(Field.X);
        System.out.println(b);

        b = new Board(5);
        b.fillBoard(Field.X);
        System.out.println(b);

        b = new Board(5);
        b.fillBoard(Field.X);
        System.out.println(b);
    }

    @Test
    void testPut() {
        Board b = new Board(3);
        b.fillBoard(Field.X);
        b.put(0,2, Field.O);
        System.out.println(b);

        b = new Board(5);
        b.fillBoard(Field.X);
        b.put(4,2, Field.O);
        System.out.println(b);

        b = new Board(5);
        b.fillBoard(Field.X);
        b.put(2,4, Field.O);
        System.out.println(b);
    }

}