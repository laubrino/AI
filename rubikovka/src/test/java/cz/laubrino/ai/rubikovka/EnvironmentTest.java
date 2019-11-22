package cz.laubrino.ai.rubikovka;

import org.junit.jupiter.api.Test;

import static cz.laubrino.ai.rubikovka.Action.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author tomas.laubr on 21.11.2019.
 */
class EnvironmentTest {

    @Test
    void testSteps() {
        Environment environment = new Environment();
        String initialState = environment.toString();

        environment.step(U);
        environment.step(F);
        environment.step(R);
        environment.step(D);
        environment.step(L);
        environment.step(B);

        environment.step(Bp);
        environment.step(Lp);
        environment.step(Dp);
        environment.step(Rp);
        environment.step(Fp);
        environment.step(Up);

        assertEquals(initialState, environment.toString());
    }

    @Test
    void testToString() {
        Environment environment = new Environment();
        System.out.println(environment.toString());

        environment.step(B);
        System.out.println(environment.toString());
        environment.step(Bp);

        environment.step(U);
        environment.step(F);
        environment.step(R);
        environment.step(D);
        environment.step(L);
        environment.step(B);
        System.out.println(environment.toString());
    }

    @Test
    void testSteps2() {
        Environment environment = new Environment();
        String initialState = environment.toString();

        Action[] moves = new Action[]{U, U, D,B,F,R,R,Fp,R,D,Bp,U,L,L,U};
        Action[] moves2 = new Action[]{Up, Lp,Lp,Up,B,Dp,Rp,F,Rp,Rp,Fp,Bp,Dp,Up,Up};

        for (Action a : moves) {
            environment.step(a);
        }

        System.out.println(environment.toString());

        for (Action a : moves2) {
            environment.step(a);
        }

        assertEquals(initialState, environment.toString());
    }

    @Test
    void testIsFinalStateAchieved() {
        Environment environment = new Environment();

        assertTrue(environment.isFinalStateAchieved());

        environment.step(U);
        assertFalse(environment.isFinalStateAchieved());
    }

    @Test
    void testShuffle() {
        Environment environment = new Environment();
        assertTrue(environment.isFinalStateAchieved());
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

}