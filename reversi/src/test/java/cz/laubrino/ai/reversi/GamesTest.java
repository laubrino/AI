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
}