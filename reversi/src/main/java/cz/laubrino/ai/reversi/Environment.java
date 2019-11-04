package cz.laubrino.ai.reversi;

import java.util.*;

import static cz.laubrino.ai.reversi.Policko.BLACK;
import static cz.laubrino.ai.reversi.Policko.WHITE;
import static cz.laubrino.ai.reversi.StepResult.Status.*;

/**
 * @author tomas.laubr on 1.11.2019.
 */
public class Environment {
    public static int BOARD_SIZE = 8;
    private static String HEADING_LINE = " -0-1-2-3-4-5-6-7-8-";
    private static final int[] DIRECTIONS = new int[] {-1, 0, 1};

    private Policko[] board = new Policko[BOARD_SIZE*BOARD_SIZE];
    private Set<Policko> passed = EnumSet.noneOf(Policko.class);
    private boolean gameOver = false;
    private Map<Policko, Observer> observers = new EnumMap<>(Policko.class);

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
            put(3,3, WHITE);
            put(4,4, WHITE);
            put(4,3, BLACK);
            put(3,4, BLACK);
        }

        if (BOARD_SIZE == 6) {
            put(2,2, WHITE);
            put(3,3, WHITE);
            put(3,2, BLACK);
            put(2,3, BLACK);
        }

        passed.clear();
        gameOver = false;
    }

    /**
     * Set agents as observers of step results
     * @param policko
     * @param agent
     */
    public void addObserver(Policko policko, Observer observer) {
        observers.put(policko, observer);
    }

    void notifyObservers(Policko policko, StepResult stepResult) {
        Observer observer = observers.get(policko);
        if (observer != null) {
            observer.notify(stepResult);
        }
    }

    public void put(int x, int y, Policko p) {
        board[y*BOARD_SIZE+x] = p;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(HEADING_LINE, 0, BOARD_SIZE*2+2);
        for (int i=0;i<BOARD_SIZE; i++) {
            sb.append("\n");
            sb.append(i);
            for (int j=0;j<BOARD_SIZE;j++) {
                sb.append("|");
                sb.append(board[i*BOARD_SIZE + j]);
            }
            sb.append("|");
        }

        return sb.toString();
    }

    public void step(Action action) {
        doStep(action, false);
        return;
    }

    Map<Policko, Integer> calculateScore() {
        Map<Policko, Integer> score = new EnumMap<>(Policko.class);
        score.put(WHITE, 0);
        score.put(BLACK, 0);

        for (Policko policko : board) {
            if (policko != Policko.EMPTY) {
                score.put(policko, score.get(policko) + 1);
            }
        }

        if (gameOver) {     // add more points to a winner, if game is over
            int white = score.get(WHITE);
            int black = score.get(BLACK);

            if (white + black != BOARD_SIZE*BOARD_SIZE) {
                if (white == black) {
                    score.put(WHITE, BOARD_SIZE*BOARD_SIZE/2);
                    score.put(BLACK, BOARD_SIZE*BOARD_SIZE/2);
                } else {
                    Policko update = white < black ? BLACK : WHITE;

                    int morePoint = BOARD_SIZE*BOARD_SIZE - white - black;
                    score.put(update, score.get(update) + morePoint);
                }
            }
        }

        return score;
    }

    Optional<Policko> determineWinner(Map<Policko, Integer> score) {
        if (score.get(WHITE).equals(score.get(BLACK))) {
            return Optional.empty();
        }

        if (score.get(WHITE) > score.get(BLACK)) {
            return Optional.of(WHITE);
        }

        return Optional.of(BLACK);
    }

    /**
     *
     * @param action any action. If the action is invalid, return that in result
     * @param testOnly if {@code true}, don't write to board
     * @return
     */
    void doStep(Action action, boolean testOnly) {
        if (gameOver) {
            throw new AssertionError("Game is already over.");
        }

        if (action.isPassAction()) {
            if (isThereMove(action.getP())) {
                gameOver = true;
                notifyObservers(action.getP(), new StepResult(new State(board), -10f, true, ILLEGAL_ACTION));      // it's not permitted to draw if there is a move available
                return;
            } else {
                passed.add(action.getP());
            }

            if (passed.size() == 2) {       // both players passed
                gameOver = true;
                Map<Policko, Integer> score = calculateScore();
                Optional<Policko> optWinner = determineWinner(score);
                if (optWinner.isPresent()) {
                    notifyObservers(optWinner.get(), new StepResult(new State(board), 10f, true, WIN));
                    notifyObservers(optWinner.get() == BLACK ? WHITE : BLACK, new StepResult(new State(board), -10f, true, LOSE));
                } else {
                    notifyObservers(WHITE, new StepResult(new State(board), 5f, true, DRAW));
                    notifyObservers(BLACK, new StepResult(new State(board), 5f, true, DRAW));
                }
                return ;
            } else {
                notifyObservers(BLACK, new StepResult(new State(board), 0f, false, CONTINUE));
                notifyObservers(WHITE, new StepResult(new State(board), 0f, false, CONTINUE));
                return;
            }
        }

        if (action.getP() == Policko.EMPTY) {
            throw new AssertionError(action);
        }

        if (!checkAndReverse(action,testOnly)) {
            notifyObservers(action.getP(), new StepResult(new State(board), -10f, true, ILLEGAL_ACTION));        // illegal move, place is not empty
            return;
        }

        if (!testOnly) {
            board[action.getX() + action.getY()*BOARD_SIZE] = action.getP();
        }

        passed.remove(action.getP());

        if (checkGameOver()) {
            gameOver = true;
            // TODO: notify winner and looser
        } else {
            notifyObservers(action.getP(), new StepResult(new State(board), 0f, false, CONTINUE));
        }

    }

    public State getState() {
        return new State(board);
    }

    public boolean isGameOver() {
        return gameOver;
    }


    /**
     * should be private
     * @return
     */
    boolean checkGameOver() {
        // check for both colors first
        boolean whiteFound = false;
        boolean blackFound = false;

        for (Policko policko : board) {
            if (policko == WHITE) {
                whiteFound = true;
            } else if (policko == BLACK) {
                blackFound = true;
            }
            if (whiteFound && blackFound) {
                break;
            }
        }

        if (!whiteFound || !blackFound) {
            return true;
        }

        Set<Action> availableActions = getAvailableActions();

        return availableActions.stream().allMatch(Action::isPassAction);
    }

    /**
     * Is there a move for a player?
     * @param policko
     * @return
     */
    boolean isThereMove(Policko policko) {
        for (int y=0;y<BOARD_SIZE;y++) {
            for (int x=0;x<BOARD_SIZE;x++) {
                if (board[x + y * BOARD_SIZE] != Policko.EMPTY) {
                    continue;
                }

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
                                if(firstColorFound == policko) {    // found other's player move, try different direction
                                    break;
                                } else {
                                    continue;
                                }
                            }

                            if (firstColorFound == board[index]) {
                                break;
                            }

                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     * find all available actions (white AND also black) including PASS action
     * @return
     */
    Set<Action> getAvailableActions() {
        Set<Action> availableActions = new HashSet<>();

        for (int y=0;y<BOARD_SIZE;y++) {
            for (int x=0;x<BOARD_SIZE;x++) {
                if (board[x + y * BOARD_SIZE] != Policko.EMPTY) {
                    continue;
                }

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
                            break;
                        }
                    }
                }
            }
        }

        if (availableActions.stream().noneMatch(action -> action.getP() == WHITE)) {
            availableActions.add(Action.getPassAction(WHITE));
        }
        if (availableActions.stream().noneMatch(action -> action.getP() == BLACK)) {
            availableActions.add(Action.getPassAction(BLACK));
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
        Policko opposite = action.getP() == WHITE ? BLACK : WHITE;

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
