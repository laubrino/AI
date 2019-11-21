package cz.laubrino.ai.rubikovka;

import java.util.BitSet;
import java.util.EnumSet;
import java.util.Random;

import static cz.laubrino.ai.rubikovka.Environment.Colors.*;


/**
 * @author tomas.laubr on 24.10.2019.
 */
public class Environment {
    private BitSet kostka = new BitSet(24*3);            // rubik cube 2x2x2, 3bits/color, 24 surfaces
    private Random randoms = new Random();
    private static final Colors[] INITIAL_SURFACES = new Colors[] {O, O, O, O, G, G, W,W, B,B,Y,Y,G,G,W,W,B,B,Y,Y,R,R,R,R};

    public Environment() {
        reset();
    }

    enum Colors {
        W, O, G, B, R, Y;

        static Colors getByIndex(int index) {
            switch (index) {
                case 0: return W;
                case 1: return O;
                case 2: return G;
                case 3: return B;
                case 4: return R;
                case 5: return Y;
                default: throw new RuntimeException("Unknown color index " + index);
            }
        }
    }

    /**
     * Reset but do not shuffle
     */
    void reset() {
        kostka.clear();
        for (int i=0;i<24;i++) {
            set(i, INITIAL_SURFACES[i]);
        }
    }

    void set(int surface, Colors color) {
        kostka.set(surface*3, (color.ordinal()&0b100) != 0);
        kostka.set(surface*3+1, (color.ordinal()&0b10) != 0);
        kostka.set(surface*3+2, (color.ordinal()&0b1) != 0);
    }

    Colors get(int surface) {
        int colorIndex = (kostka.get(surface * 3) ? 4 : 0) + (kostka.get(surface * 3 + 1) ? 2 : 0) + (kostka.get(surface * 3 + 2) ? 1 : 0);
        return Colors.getByIndex(colorIndex);
    }

    /**
     * randomly move with numbers
     */
    void shuffle(int numberOfShuffleMoves) {
    }

    boolean isFinalStateAchieved() {
        return false;
    }
//
//    EnumSet<Action> getAvailableActions() {
//        return null;
//    }
//
//    /**
//     * Take no action if invalid move
//     * @param action
//     */
//    ActionResult step(Action action) {
//        for (int i=0;i<BOARD_SIZE*BOARD_SIZE;i++) {
//            if (board[i] == 0) {     // find the space
//                int j;
//                switch (action) {
//                    case MOVE_UP:
//                        j = i+BOARD_SIZE;
//                        if (j>BOARD_SIZE*BOARD_SIZE-1) {
//                            return new ActionResult(getState(), -10f, true);      // invalid move
//                        }
//                        break;
//                    case MOVE_DOWN:
//                        j = i-BOARD_SIZE;
//                        if (j<0) {
//                            return new ActionResult(getState(), -10f, true);
//                        }
//                        break;
//                    case MOVE_LEFT:
//                        j = i+1;
//                        if (j/BOARD_SIZE != i/BOARD_SIZE || j>BOARD_SIZE*BOARD_SIZE-1) {
//                            return new ActionResult(getState(), -10f, true);
//                        }
//                        break;
//                    case MOVE_RIGHT:
//                        j = i-1;
//                        if (j/BOARD_SIZE != i/BOARD_SIZE || j<0) {
//                            return new ActionResult(getState(), -10f, true);
//                        }
//                        break;
//                    default:
//                        throw new RuntimeException("WTF? " + action);
//                }
//
//                board[i] = board[j];
//                board[j] = 0;
//                break;
//            }
//        }
//
//        if (isFinalStateAchieved()) {
//            return new ActionResult(getState(), 10f, true);
//        } else {
//            return new ActionResult(getState(), 0f, false);
//        }
//    }
//
    @Override
    public String toString() {
        int val;
        StringBuilder sb = new StringBuilder();
        sb.append("  ").append(get(0)).append(get(1)).append("\n");
        sb.append("  ").append(get(2)).append(get(3)).append("\n");
        sb.append(get(4)).append(get(5)).append(get(6)).append(get(7)).append(get(8)).append(get(9)).append(get(10)).append(get(11)).append("\n");
        sb.append(get(12)).append(get(13)).append(get(14)).append(get(15)).append(get(16)).append(get(17)).append(get(18)).append(get(19)).append("\n");
        sb.append("  ").append(get(20)).append(get(21)).append("\n");
        sb.append("  ").append(get(22)).append(get(23)).append("\n");
        return sb.toString();
    }
//
//    State getState() {
//        byte[] bytes = new byte[BOARD_SIZE*BOARD_SIZE];
//        System.arraycopy(board, 0, bytes, 0, BOARD_SIZE*BOARD_SIZE);
//        return new State(bytes);
//    }
}
