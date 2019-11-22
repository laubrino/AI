package cz.laubrino.ai.rubikovka;

import java.util.BitSet;
import java.util.Random;

import static cz.laubrino.ai.rubikovka.Environment.Color.*;


/**
 *      OO
 *      OO
 *   GG|WW|BB|YY
 *   GG|WW|BB|YY
 *      RR
 *      RR
 *
 *  White is upper face, red is front, blue is right face.
 *
 * Indexes of surfaces:
 *          0  1
 *          2  3
 *    4  5| 6  7| 8  9|10 11
 *   12 13|14 15|16 17|18 19
 *         20 21
 *         22 23
 *
 *
 * @author tomas.laubr on 24.10.2019.
 */
public class Environment {
    private BitSet kostka = new BitSet(24*3);            // rubik cube 2x2x2, 3bits/color, 24 surfaces
    private Random randoms = new Random();
    private static final Color[] INITIAL_SURFACES = new Color[] {O, O, O, O, G, G, W,W, B,B,Y,Y,G,G,W,W,B,B,Y,Y,R,R,R,R};

    public Environment() {
        reset();
    }

    enum Color {
        W, O, G, B, R, Y;

        static Color getByIndex(int index) {
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

    void set(int surface, Color color) {
        kostka.set(surface*3, (color.ordinal()&0b100) != 0);
        kostka.set(surface*3+1, (color.ordinal()&0b10) != 0);
        kostka.set(surface*3+2, (color.ordinal()&0b1) != 0);
    }

    Color get(int surface) {
        int colorIndex = (kostka.get(surface * 3) ? 4 : 0) + (kostka.get(surface * 3 + 1) ? 2 : 0) + (kostka.get(surface * 3 + 2) ? 1 : 0);
        return Color.getByIndex(colorIndex);
    }

    /**
     * randomly move with numbers
     */
    void shuffle(int numberOfShuffleMoves) {
    }

    boolean isFinalStateAchieved() {
        return false;
    }

    private void turnU() {
        Color tmp = get(6);
        set(6, get(14));
        set(14, get(15));
        set(15, get(7));
        set(7, tmp);

        tmp = get(5);
        Color tmp2 = get(13);

        set(5, get(20));
        set(13, get(21));
        set(20, get(16));
        set(21, get(8));
        set(16, get(3));
        set(8, get(2));
        set(3, tmp);
        set(2, tmp2);
    }

    /**
     * Circular shifts all positions to right. (The most right position will appear on the most left)
     * @param positions
     */
    private void shift(int[] positions) {
        Color tmp = get(positions[positions.length-1]);
        for (int i=positions.length - 2; i>=0; i--) {
            set(positions[i+1], get(positions[i]));
        }
        set(positions[0], tmp);
    }

    /**
     * Oposit direction shift (prime)
     * @see #shift(int[])
     * @param positions
     */
    private void shiftP(int[] positions) {
        Color tmp = get(positions[0]);
        for (int i=0; i<positions.length - 1; i++) {
            set(positions[i], get(positions[i+1]));
        }
        set(positions[positions.length-1], tmp);
    }

    private void turnUp() {
        shift(new int[]{6,14,15,7});
        shift(new int[]{21,8,2,13});
        shift(new int[]{20,16,3,5});
    }

    private void turnF() {
        shift(new int[]{20,21,23,22});
        shift(new int[]{14,16,18,12});
        shift(new int[]{15,17,19,13});
    }

    private void turnFp() {
        shiftP(new int[]{20,21,23,22});
        shiftP(new int[]{14,16,18,12});
        shiftP(new int[]{15,17,19,13});
    }

    private void turnR() {
        shift(new int[]{16,8,9,17});
        shift(new int[]{15,3,10,23});
        shift(new int[]{7,1,18,21});
    }
    private void turnRp() {
        shiftP(new int[]{16,8,9,17});
        shiftP(new int[]{15,3,10,23});
        shiftP(new int[]{7,1,18,21});
    }

    private void turnD() {
        shift(new int[]{22,17,1,4});
        shift(new int[]{23,9,0,12});
        shift(new int[]{11,19,18,10});
    }
    private void turnDp() {
        shiftP(new int[]{22,17,1,4});
        shiftP(new int[]{23,9,0,12});
        shiftP(new int[]{11,19,18,10});
    }

    private void turnL() {
        shift(new int[]{11,2,14,22});
        shift(new int[]{19,0,6,20});
        shift(new int[]{5,13,12,4});
    }
    private void turnLp() {
        shiftP(new int[]{11,2,14,22});
        shiftP(new int[]{19,0,6,20});
        shiftP(new int[]{5,13,12,4});
    }
    private void turnB() {
        shift(new int[]{8,6,4,10});
        shift(new int[]{9,7,5,11});
        shift(new int[]{3,2,0,1});
    }
    private void turnBp() {
        shiftP(new int[]{8,6,4,10});
        shiftP(new int[]{9,7,5,11});
        shiftP(new int[]{3,2,0,1});
    }


    public void step(Action action) {
        switch (action) {
            case U: turnU();
                break;
            case Up: turnUp();
                break;
            case F: turnF();
                break;
            case Fp: turnFp();
                break;
            case R: turnR();
                break;
            case Rp: turnRp();
                break;
            case D: turnD();
                break;
            case Dp: turnDp();
                break;
            case L: turnL();
                break;
            case Lp: turnLp();
                break;
            case B: turnB();
                break;
            case Bp: turnBp();
                break;

            default: throw new RuntimeException("Not implemented " + action);
        }
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
