package cz.laubrino.ai.reversi;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

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
    private Set<Policko> passed = EnumSet.noneOf(Policko.class);

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

        passed.clear();
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


    /**
     *
     * @param action any action. If the action is invalid, return that in result
     * @param testOnly
     * @return
     */
    StepResult doStep(Action action, boolean testOnly) {
        if (action.isPassAction()) {
            if (passed.contains(action.getP())) {
                throw new RuntimeException();       // TODO: really? Can I passed several times? Chech possible moves first
            } else {
                passed.add(action.getP());
            }

            if (passed.size() == 2) {
                return new StepResult(new State(board), 0f, true, WIN);     // TODO: both players passed, deal with it
            } else {
                return new StepResult(new State(board), 0f, false, CONTINUE);
            }
        }

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

    public State getState() {
        return new State(board);
    }

    boolean isGameOver() {
        // check for both colors first
        boolean whiteFound = false;
        boolean blackFound = false;

        for (Policko policko : board) {
            if (policko == Policko.WHITE) {
                whiteFound = true;
            } else if (policko == Policko.BLACK) {
                blackFound = true;
            }
            if (whiteFound && blackFound) {
                break;
            }
        }

        if (!whiteFound || !blackFound) {
            return true;
        }

        Set<Action> availableActions = findAvailableActions();

        return availableActions.size() == 0;
    }

    /**
     * find all available actions (white AND also black)
     * @return
     */
    Set<Action> findAvailableActions() {
        Set<Action> availableActions = new HashSet<>();

        for (int y=0;y<BOARD_SIZE;y++) {
            for (int x=0;x<BOARD_SIZE;x++) {
                if (board[x + y * BOARD_SIZE] != Policko.EMPTY) {
                    continue;
                }

                directions:
                for (int xDirection : DIRECTIONS) {
                    for (int yDirection : DIRECTIONS) {
                        if (xDirection == 0 && yDirection == 0) {
                            continue;
                        }

                        Policko firstColorFound = null;

                        for (int index = (x + xDirection) + (y + yDirection) * BOARD_SIZE;
                             index < BOARD_SIZE * BOARD_SIZE && index >= 0;
                             index += xDirection + yDirection * BOARD_SIZE) {
                            if (board[index] == Policko.EMPTY) {
                                break;      // no action available in this direction
                            }

                            if (firstColorFound == null) {
                                firstColorFound = board[index];
                                continue;
                            }

                            if (firstColorFound == board[index]) {
                                break;
                            }

                            availableActions.add(new Action(x, y, board[index]));
                            break;      // TODO: consider break to "directions:"
                        }
                    }
                }
            }
        }

        return availableActions;
    }

    /**
     * Reverse
     * @param action
     * @param testOnly don't reverse, just check step is valid
     * @return
     */
    private boolean checkAndReverse(Action action, boolean testOnly) {
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
                            if (!testOnly) {
                                flip(xDirection,yDirection,action);
                            }
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
    private void flip(int xDirection, int yDirection, Action action) {
        int x = action.getX() + xDirection;
        int y = action.getY() + yDirection;

        while ((x+y*BOARD_SIZE >= 0) && (x+y*BOARD_SIZE < BOARD_SIZE*BOARD_SIZE) && board[x+y*BOARD_SIZE] != action.getP()) {
            board[x+y*BOARD_SIZE] = action.getP();
            x += xDirection;
            y += yDirection;
        }
    }
}
