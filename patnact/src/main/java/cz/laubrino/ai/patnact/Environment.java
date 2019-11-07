    package cz.laubrino.ai.patnact;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

/**
 * @author tomas.laubr on 24.10.2019.
 */
public class Environment {
    public static final int BOARD_SIZE = 3;
    byte[] board = new byte[BOARD_SIZE*BOARD_SIZE];
    private Random randoms = new Random();

    public Environment() {
        reset();
    }

    /**
     * Reset but do not shuffle
     */
    void reset() {
        for (int i=0;i<BOARD_SIZE*BOARD_SIZE-1;i++) {
            board[i] = (byte)(i+1);
        }

        board[BOARD_SIZE*BOARD_SIZE-1] = 0;     // the space
    }

    /**
     * randomly move with numbers
     */
    void shuffle(int numberOfShuffleMoves) {
        for (int i=0;i<numberOfShuffleMoves;i++) {
            step(Action.values()[randoms.nextInt(4)]);
        }
    }

    boolean isFinalStateAchieved() {
        for (int i=0;i<BOARD_SIZE*BOARD_SIZE-1;i++) {
            if (board[i] != (byte)(i+1)) {
                return false;
            }
        }

        return board[BOARD_SIZE*BOARD_SIZE-1] == 0;
    }

    EnumSet<Action> getAvailableActions() {
        EnumSet<Action> availableActions = EnumSet.noneOf(Action.class);

        int i;
        for (i=0;i<BOARD_SIZE*BOARD_SIZE;i++) {
            if (board[i] == 0) {     // find the space
                break;
            }
        }
        if ((i%BOARD_SIZE) > 0) {
            availableActions.add(Action.MOVE_RIGHT);
        }

        if ((i%BOARD_SIZE) < BOARD_SIZE-1) {
            availableActions.add(Action.MOVE_LEFT);
        }

        if (i/BOARD_SIZE > 0) {
            availableActions.add(Action.MOVE_DOWN);
        }

        if (i/BOARD_SIZE < BOARD_SIZE-1) {
            availableActions.add(Action.MOVE_UP);
        }

        return availableActions;
    }

    /**
     * Take no action if invalid move
     * @param action
     */
    ActionResult step(Action action) {
        for (int i=0;i<BOARD_SIZE*BOARD_SIZE;i++) {
            if (board[i] == 0) {     // find the space
                int j;
                switch (action) {
                    case MOVE_UP:
                        j = i+BOARD_SIZE;
                        if (j>BOARD_SIZE*BOARD_SIZE-1) {
                            return new ActionResult(getState(), -10f, true);      // invalid move
                        }
                        break;
                    case MOVE_DOWN:
                        j = i-BOARD_SIZE;
                        if (j<0) {
                            return new ActionResult(getState(), -10f, true);
                        }
                        break;
                    case MOVE_LEFT:
                        j = i+1;
                        if (j/BOARD_SIZE != i/BOARD_SIZE || j>BOARD_SIZE*BOARD_SIZE-1) {
                            return new ActionResult(getState(), -10f, true);
                        }
                        break;
                    case MOVE_RIGHT:
                        j = i-1;
                        if (j/BOARD_SIZE != i/BOARD_SIZE || j<0) {
                            return new ActionResult(getState(), -10f, true);
                        }
                        break;
                    default:
                        throw new RuntimeException("WTF? " + action);
                }

                board[i] = board[j];
                board[j] = 0;
                break;
            }
        }

        if (isFinalStateAchieved()) {
            return new ActionResult(getState(), 10f, true);
        } else {
            return new ActionResult(getState(), 0f, false);
        }
    }

    @Override
    public String toString() {
        int val;
        StringBuilder sb = new StringBuilder();
        sb.append("-------------");

        for (int i=0;i<BOARD_SIZE*BOARD_SIZE;i++) {
            if ((i%BOARD_SIZE) == 0) {
                sb.append("\n|");
            }

            val = board[i];
            if (val == 0) {
                sb.append("  ");
            } else {
                sb.append(String.format("%2d", board[i]));
            }
            sb.append("|");
        }

        sb.append("\n-------------");

        return sb.toString();
    }

    State getState() {
        byte[] bytes = new byte[BOARD_SIZE*BOARD_SIZE];
        System.arraycopy(board, 0, bytes, 0, BOARD_SIZE*BOARD_SIZE);
        return new State(bytes);
    }
}
