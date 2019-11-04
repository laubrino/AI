package cz.laubrino.ai.reversi;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GamesTest {

    @Test
    void testPlayShortestGame() {
        Environment environment = new Environment();
        Games games = new Games();
        games.playShortestGame(environment);
        assertEquals("[....x...|...x....|.xxxx...|...xxx..|...xx...|...x....|...x....|........]",
                environment.getState().toString());
    }

    @Test
    void testPlayRandomGame() {
        Environment environment = new Environment();
        Games games = new Games();

        for (int i=0;i<100;i++) {
            environment.reset();
            games.playRandomGame(environment, i);
            System.out.println(environment.toString());
            System.out.println(environment.calculateScore().toString());
            System.out.println("\n");
        }
    }
}