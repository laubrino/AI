package cz.laubrino.ai.reversi;

import java.util.HashSet;
import java.util.Set;

/**
 * @author tomas.laubr on 4.11.2019.
 */
public class Agent {
    private static final int[] DIRECTIONS = new int[] {-1, 0, 1};

    private final int boardSize;
    private final Policko policko;      // either white or black

    State state;

    public Agent(int boardSize, Policko policko) {
        this.boardSize = boardSize;
        this.policko = policko;
    }

    public void observeState(State state) {
        this.state = state;
    }

    /**
     * find all available actions (white AND also black)
     * @return
     */
    Set<Action> findAvailableMoves() {
        Set<Action> availableActions = new HashSet<>();

        for (int y=0;y<boardSize;y++) {
            for (int x=0;x<boardSize;x++) {
                if (state.state[x + y * boardSize] != Policko.EMPTY) {
                    continue;
                }

                for (int xDirection : DIRECTIONS) {
                    for (int yDirection : DIRECTIONS) {
                        if (xDirection == 0 && yDirection == 0) {
                            continue;
                        }

                        Policko firstColorFound = null;

                        for (int index = (x + xDirection) + (y + yDirection) * boardSize;
                             index < boardSize * boardSize && index >= 0;
                             index += xDirection + yDirection * boardSize) {
                            if (state.state[index] == Policko.EMPTY) {
                                break;      // no action available in this direction
                            }

                            if (firstColorFound == null) {
                                firstColorFound = state.state[index];
                                if (firstColorFound == policko) {
                                    break;
                                }
                                continue;
                            }

                            if (firstColorFound == state.state[index]) {
                                break;
                            }

                            availableActions.add(new Action(x, y, state.state[index]));
                            break;
                        }
                    }
                }
            }
        }

        return availableActions;
    }

}
