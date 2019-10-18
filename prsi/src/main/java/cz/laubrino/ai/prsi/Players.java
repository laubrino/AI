package cz.laubrino.ai.prsi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author tomas.laubr on 17.10.2019.
 */
public class Players {
    List<Player> players;
    int currentPlayer = 0;

    public Players(Player... players) {
        this.players = new ArrayList<>(Arrays.asList(players));
    }

    /**
     * get next player on turn
     * @return
     */
    Player next() {
        currentPlayer = (currentPlayer + 1) % players.size();
        return players.get(currentPlayer);
    }

    List<Player> getPlayers() {
        return players;
    }

    int getNumberOfPlayers() {
        return players.size();
    }
}
