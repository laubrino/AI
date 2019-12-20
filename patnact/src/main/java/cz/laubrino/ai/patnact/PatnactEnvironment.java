    package cz.laubrino.ai.patnact;

import cz.laubrino.ai.framework.ActionResult;
import cz.laubrino.ai.framework.Environment;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

/**
 * @author tomas.laubr on 24.10.2019.
 */
public class PatnactEnvironment implements Environment<Action> {
    public static final int BOARD_SIZE = 4;                 // 3 or 4, no more
    byte[] board = new byte[(BOARD_SIZE*BOARD_SIZE+1)/2];
    private Random randoms = new Random();

    public PatnactEnvironment() {
        for (int i=0;i<BOARD_SIZE*BOARD_SIZE-1;i++) {
            setBoard(i, (byte)(i+1));
        }

        setBoard(BOARD_SIZE*BOARD_SIZE-1, 0);     // the space
    }

    /**
     * Reset but do not shuffle
     */
    public void reset() {
        shuffle(1000 / (randoms.nextInt(100)+1));
    }

    private void setBoard(int index, int value) {
        int arrayIndex = index/2;
        if ((index & 1) == 1) {
            value = value << 4;
            board[arrayIndex] = (byte)(board[arrayIndex] & 0x0F | value);
        } else {
            board[arrayIndex] = (byte)(board[arrayIndex] & 0xF0 | value);
        }
    }

    private byte getBoard(int index) {
        int arrayIndex = index/2;
        if ((index & 1) == 1) {
            return (byte)((board[arrayIndex] & 0xF0) >> 4);
        } else {
            return (byte)(board[arrayIndex] & 0x0F);
        }
    }

    /**
     * randomly move with numbers
     */
    void shuffle(int numberOfShuffleMoves) {
        for (int i=0;i<numberOfShuffleMoves;i++) {
            step(Action.VALUES[randoms.nextInt(4)]);
        }
    }

    @Override
    public boolean isFinalStateAchieved() {
        for (int i=0;i<BOARD_SIZE*BOARD_SIZE-1;i++) {
            if (getBoard(i) != (byte)(i+1)) {
                return false;
            }
        }

        return getBoard(BOARD_SIZE*BOARD_SIZE-1) == 0;
    }

    public Action[] getAvailableActions() {
        return Action.VALUES;               // always return all actions, no matter what state. Agent will learn valid actions
    }

    /**
     * Take no action if invalid move
     * @param action
     */
    public ActionResult step(Action action) {
        for (int i=0;i<BOARD_SIZE*BOARD_SIZE;i++) {
            if (getBoard(i) == 0) {     // find the space
                int j;
                switch (action) {
                    case MOVE_UP:
                        j = i+BOARD_SIZE;
                        if (j>BOARD_SIZE*BOARD_SIZE-1) {
                            return new ActionResult(getState(), -1000f, ActionResult.Type.FAIL);      // invalid move
                        }
                        break;
                    case MOVE_DOWN:
                        j = i-BOARD_SIZE;
                        if (j<0) {
                            return new ActionResult(getState(), -1000f, ActionResult.Type.FAIL);
                        }
                        break;
                    case MOVE_LEFT:
                        j = i+1;
                        if (j/BOARD_SIZE != i/BOARD_SIZE || j>BOARD_SIZE*BOARD_SIZE-1) {
                            return new ActionResult(getState(), -1000f, ActionResult.Type.FAIL);
                        }
                        break;
                    case MOVE_RIGHT:
                        j = i-1;
                        if (j/BOARD_SIZE != i/BOARD_SIZE || j<0) {
                            return new ActionResult(getState(), -1000f, ActionResult.Type.FAIL);
                        }
                        break;
                    default:
                        throw new RuntimeException("WTF? " + action);
                }

                setBoard(i, getBoard(j));
                setBoard(j, 0);
                break;
            }
        }

        if (isFinalStateAchieved()) {
            return new ActionResult(getState(), 1000f, ActionResult.Type.OK);
        } else {
            return new ActionResult(getState(), 0f, ActionResult.Type.CONTINUE);
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

            val = getBoard(i);
            if (val == 0) {
                sb.append("  ");
            } else {
                sb.append(String.format("%2d", getBoard(i)));
            }
            sb.append("|");
        }

        sb.append("\n-------------");

        return sb.toString();
    }

    public State getState() {
        byte[] bytes = new byte[board.length];
        System.arraycopy(board, 0, bytes, 0, board.length);
        return new State(bytes);
    }
}
