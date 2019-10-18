package cz.laubrino.ai.piskvorky;

import com.google.common.cache.CacheBuilder;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static cz.laubrino.ai.piskvorky.Field.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author tomas.laubr on 10.10.2019.
 */
class ProcessorTest {

    @Test
    void isPiskvorek() {
        Board b = new Board(5);
        State s3 = new State(b, 3, X);
        State s6 = new State(b, 6, X);
        State s4 = new State(b, 4, X);

        assertFalse(Processor.isPiskvorek(s3).isPresent());
        assertFalse(Processor.isPiskvorek(s6).isPresent());

        b.fillBoard(NIC);
        b.put(1,1, O);
        b.put(2,2, O);
        b.put(3,3, O);
        assertFalse(Processor.isPiskvorek(s4).isPresent());
        assertEquals(Optional.of(O), Processor.isPiskvorek(s3));

        b.fillBoard(NIC);
        b.put(3,1, O);
        b.put(2,2, O);
        b.put(1,3, O);
        assertFalse(Processor.isPiskvorek(s4).isPresent());
        assertEquals(Optional.of(O), Processor.isPiskvorek(s3));

        b.fillBoard(NIC);
        b.put(1,1, O);
        b.put(2,2, X);
        b.put(3,3, O);
        assertFalse(Processor.isPiskvorek(s4).isPresent());
        assertFalse(Processor.isPiskvorek(s3).isPresent());

        b.fillBoard(NIC);
        b.put(1,1, O);
        b.put(1,2, O);
        b.put(1,3, O);
        assertFalse(Processor.isPiskvorek(s4).isPresent());
        assertEquals(Optional.of(O), Processor.isPiskvorek(s3));

        b.fillBoard(NIC);
        b.put(1,1, O);
        b.put(2,1, O);
        b.put(3,1, O);
        assertFalse(Processor.isPiskvorek(s4).isPresent());
        assertEquals(Optional.of(O), Processor.isPiskvorek(s3));
    }

    @Test
    void testChooseAction() {
        Board b = new Board(3);
        b.put(1,1, O);
        State s = new State(b, 3, X);

        Processor p = new Processor();
        Optional<Action> action = p.chooseAction(s, CacheBuilder.newBuilder().build(), 0);
        assertTrue(action.isPresent());
        System.out.println(action.get());
    }
}