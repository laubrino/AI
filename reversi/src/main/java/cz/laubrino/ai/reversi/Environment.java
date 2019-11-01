package cz.laubrino.ai.reversi;

import static cz.laubrino.ai.reversi.StepResult.Reason.*;

/**
 * @author tomas.laubr on 1.11.2019.
 */
public class Environment {
    public static int BOARD_SIZE = 8;
    private static String HEADING_LINE = " -0-1-2-3-4-5-6-7-8-";
    private static String FOOTER_LINE = " --------------------";
    private static final int[] DIRECTIONS = new int[] {-1, 0, 1};

    private Policko[] board = new Policko[BOARD_SIZE*BOARD_SIZE];

    public Environment() {
        reset();
    }

    Policko[] getBoard() {
        return board;
    }

    public void reset() {
        for (int i=0;i<BOARD_SIZE*BOARD_SIZE;i++) {
            board[i] = Policko.EMPTY;
        }

        if (BOARD_SIZE == 8) {
            put(3,3,Policko.WHITE);
            put(4,4,Policko.WHITE);
            put(4,3,Policko.BLACK);
            put(3,4,Policko.BLACK);
        }

        if (BOARD_SIZE == 6) {
            put(2,2,Policko.WHITE);
            put(3,3,Policko.WHITE);
            put(3,2,Policko.BLACK);
            put(2,3,Policko.BLACK);
        }

    }

    public void put(int x, int y, Policko p) {
        board[y*BOARD_SIZE+x] = p;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(HEADING_LINE, 0, BOARD_SIZE*2+2);
        sb.append("\n");
        for (int i=0;i<BOARD_SIZE; i++) {
            sb.append(i);
            for (int j=0;j<BOARD_SIZE;j++) {
                sb.append("|");
                sb.append(board[i*BOARD_SIZE + j]);
            }
            sb.append("|");
            sb.append("\n");
        }
//        sb.append(FOOTER_LINE, 0, BOARD_SIZE*2+2);

        return sb.toString();
    }

    public StepResult step(Action action) {
        return doStep(action, false);
    }


    public StepResult doStep(Action action, boolean testOnly) {
        if (action.getP() == Policko.EMPTY) {
            throw new AssertionError(action);
        }

        if (!checkAndReverse(action,testOnly)) {
            return new StepResult(new State(board), -10f, true, INVALID_MOVE);        // illegal move, place is not empty
        }

        if (!testOnly) {
            board[action.getX() + action.getY()*BOARD_SIZE] = action.getP();
        }

        return new StepResult(new State(board), 0f, false, CONTINUE);

    }

    /**
     *
     * @param action
     * @param testOnly don't reverse, just check step is valid
     * @return
     */
    boolean checkAndReverse(Action action, boolean testOnly) {
        if (board[action.getX() + action.getY()*BOARD_SIZE] != Policko.EMPTY) {
            return false;
        }

        boolean invalidMove = true;
        Policko opposite = action.getP() == Policko.WHITE ? Policko.BLACK : Policko.WHITE;

        for (int xDirection : DIRECTIONS) {
            for (int yDirection : DIRECTIONS) {
                if (xDirection == 0 && yDirection == 0) {
                    continue;
                }

                boolean foundOpposite = false;

                for (int index = (action.getX() + xDirection) + (action.getY() + yDirection) * BOARD_SIZE; index < BOARD_SIZE*BOARD_SIZE && index >= 0; index+=xDirection+yDirection*BOARD_SIZE) {
                    if (board[index] == Policko.EMPTY) {
                        break;
                    }

                    if (board[index] == action.getP()) {
                        if (foundOpposite) {
                            flip(xDirection,yDirection,action);
                            invalidMove = false;
                        }
                        break;
                    }

                    if (board[index] == opposite) {
                        foundOpposite = true;
                    }
                }
            }
        }

        return !invalidMove;
    }

    /**
     * Only for valid moves.
     * @param xDirection direction
     * @param yDirection direction
     * @param action
     */
    void flip(int xDirection, int yDirection, Action action) {
        int x = action.getX() + xDirection;
        int y = action.getY() + yDirection;

        while ((x+y*BOARD_SIZE >= 0) && (x+y*BOARD_SIZE < BOARD_SIZE*BOARD_SIZE) && board[x+y*BOARD_SIZE] != action.getP()) {
            board[x+y*BOARD_SIZE] = action.getP();
            x += xDirection;
            y += yDirection;
        }
    }
}
