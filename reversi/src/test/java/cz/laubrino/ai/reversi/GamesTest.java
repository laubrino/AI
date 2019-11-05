package cz.laubrino.ai.reversi;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GamesTest {

    @Test
    void testPlayShortestGame8x8() {
        Assumptions.assumeTrue(Environment.BOARD_SIZE == 8);

        Environment environment = new Environment();
        Games games = new Games();
        games.playShortestGame(environment);
        assertEquals("[....x...|...x....|.xxxx...|...xxx..|...xx...|...x....|...x....|........]",
                environment.getState().toString());
    }

    @Test
    void testPlayShortestGame6x6() {
        Assumptions.assumeTrue(Environment.BOARD_SIZE == 6);

        Environment environment = new Environment();
        Games games = new Games();
        games.playShortestGame(environment);
        assertEquals("[......|...o..|..oo..|.ooo..|......|......]",
                environment.getState().toString());
    }


    @Test
    void testPlayRandomGame() {
        Environment environment = new Environment();
        Games games = new Games();
        System.out.println(environment.getState());

        for (int i=0;i<100;i++) {
            environment.reset();
            games.playRandomGame(environment, i);
            System.out.println(environment.toString());
            System.out.println(environment.calculateScore().toString());
            System.out.println("\n");
        }
    }
}