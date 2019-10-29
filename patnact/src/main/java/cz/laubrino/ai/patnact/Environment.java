    package cz.laubrino.ai.patnact;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

/**
 * @author tomas.laubr on 24.10.2019.
 */
public class Environment {
    int[][] board = new int[4][4];

    public Environment() {
        reset();
    }

    /**
     * Reset but do not shuffle
     */
    void reset() {
        board[3][3] = 0;
        for (int i=0;i<15;i++) {
            board[i%4][i/4] = i+1;
        }
    }

    /**
     * randomly move with numbers
     */
    void shuffle() {
        List<Action> actions = Arrays.asList(Action.values());

        for (int i=0;i<1000;i++) {
            Action action = actions.stream().skip(new Random().nextInt(4)).findAny().get();
            step(action);
        }
    }

    boolean isFinalStateAchieved() {
        for (int i=0;i<15;i++) {
            if (board[i%4][i/4] != i+1) {
                return false;
            }
        }

        return board[3][3] == 0;
    }

    EnumSet<Action> getAvailableActions() {
        EnumSet<Action> availableActions = EnumSet.noneOf(Action.class);

        int i=0;
        for (i=0;i<16;i++) {
            if (board[i % 4][i / 4] == 0) {     // find the space
                break;
            }
        }
        if (i%4 > 0) {
            availableActions.add(Action.MOVE_RIGHT);
        }

        if (i%4 < 3) {
            availableActions.add(Action.MOVE_LEFT);
        }

        if (i/4 > 0) {
            availableActions.add(Action.MOVE_DOWN);
        }

        if (i/4 < 3) {
            availableActions.add(Action.MOVE_UP);
        }

        return availableActions;
    }

    /**
     * Take no action if invalid move
     * @param action
     */
    void step(Action action) {
        for (int i=0;i<16;i++) {
            if (board[i%4][i/4] == 0) {     // find the space
                int j = -100;
                switch (action) {
                    case MOVE_UP:
                        j = i+4;
                        if (j>15) {
                            return;
                        }
                        break;
                    case MOVE_DOWN:
                        j = i-4;
                        if (j<0) {
                            return;
                        }
                        break;
                    case MOVE_LEFT:
                        j = i+1;
                        if (j/4 != i/4 || j>15) {
                            return;
                        }
                        break;
                    case MOVE_RIGHT:
                        j = i-1;
                        if (j/4 != i/4 || j<0) {
                            return;
                        }
                        break;
                }

                board[i%4][i/4] = board[j%4][j/4];
                board[j%4][j/4] = 0;
                return;
            }
        }
    }

    @Override
    public String toString() {
        int val;
        StringBuilder sb = new StringBuilder();
        sb.append("-------------");

        for (int i=0;i<16;i++) {
            if (i%4 == 0) {
                sb.append("\n|");
            }

            val = board[i%4][i/4];
            if (val == 0) {
                sb.append("  ");
            } else {
                sb.append(String.format("%2d", board[i%4][i/4]));
            }
            sb.append("|");
        }

        sb.append("\n-------------");

        return sb.toString();
    }

    /**
     * String representation of board
     * @return
     */
    public String hashString() {
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<16;i++) {
            sb.append(board[i%4][i/4]);
            sb.append("|");
        }
        return sb.toString();
    }
}
