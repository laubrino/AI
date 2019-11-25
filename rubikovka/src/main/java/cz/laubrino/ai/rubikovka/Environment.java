package cz.laubrino.ai.rubikovka;

import java.util.Arrays;
import java.util.BitSet;
import java.util.EnumSet;
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
    /**
     * rubik cube 2x2x2, 3(4)bits/color, 24 surfaces => 12 bytes
     * |00.01|02.03|04.05|.....|22.23|    data in array
     */
    private byte[] kostka = new byte[12];
    private Random randoms = new Random();
    private static final Color[] INITIAL_SURFACES = new Color[] {O, O, O, O, G, G, W,W, B,B,Y,Y,G,G,W,W,B,B,Y,Y,R,R,R,R};
    private static final EnumSet<Action> AVAILABLE_ACTIONS = EnumSet.allOf(Action.class);

    public Environment() {
        reset();
    }

    public Environment(byte[] init) {
        kostka = Arrays.copyOf(init, init.length);
    }

    enum Color {
        W, O, G, B, R, Y;

        static Color getByIndex(int index) {    // same as values()[index], but faster, without new array creation
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

    boolean isFinalStateAchieved() {
        return isSameColor(0,1,2,3) && isSameColor(4,5,12,13) && isSameColor(6,7,14,15) && isSameColor(8,9,16,17)
        && isSameColor(10,11,18,19) && isSameColor(20,21,22,23);
    }

    private boolean isSameColor(int... positions) {
        Color color = get(positions[0]);

        for (int position : positions) {
            if (get(position) != color) {
                return false;
            }
        }

        return true;
    }

    /**
     * Reset but do not shuffle
     */
    void reset() {
        for (int i=0;i<24;i++) {
            set(i, INITIAL_SURFACES[i]);
        }
    }

    private void set(int surface, Color color) {
        int arrayIndex = surface >> 1;
        byte v = kostka[arrayIndex];

        if ((surface & 1) == 1) {
            v = (byte)(v & 0xf0);
            v = (byte) (v | color.ordinal());
        } else {
            v = (byte)(v & 0x0f);
            v = (byte) (v | (color.ordinal()<<4));
        }

        kostka[arrayIndex] = v;
    }

    private Color get(int surface) {
        int arrayIndex = surface >> 1;
        int colorIndex = kostka[arrayIndex];

        if ((surface & 1) == 1) {   //
            colorIndex = colorIndex & 0x0f;
        } else {
            colorIndex = (colorIndex & 0xf0) >> 4;
        }

        return Color.getByIndex(colorIndex);
    }

    /**
     * randomly move with numbers
     */
    void shuffle(int numberOfShuffleMoves) {
        for (int i=0;i<numberOfShuffleMoves;i++) {
            int randomActionIndex = randoms.nextInt(Action.VALUES.length);
            step(Action.VALUES[randomActionIndex]);
        }
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

    private static final int[][] U_SHIFTS = new int[][] {
            new int[]{7,15,14,6},
            new int[]{13,2,8,21},
            new int[]{5,3,16,20}
    };

    private static final int[][] F_SHIFTS = new int[][] {
            new int[]{20,21,23,22},
            new int[]{14,16,18,12},
            new int[]{15,17,19,13}
    };

    private static final int[][] R_SHIFTS = new int[][] {
            (new int[]{16,8,9,17}),
            (new int[]{15,3,10,23}),
            (new int[]{7,1,18,21})
    };

    private static final int[][] D_SHIFTS = new int[][] {
            (new int[]{22,17,1,4}),
            (new int[]{23,9,0,12}),
            (new int[]{11,19,18,10})
    };

    private static final int[][] L_SHIFTS = new int[][] {
            (new int[]{11,2,14,22}),
            (new int[]{19,0,6,20}),
            (new int[]{5,13,12,4})
    };

    private static final int[][] B_SHIFTS = new int[][] {
            (new int[]{8,6,4,10}),
            (new int[]{9,7,5,11}),
            (new int[]{3,2,0,1}),
    };

    private void makeTurn(int[][] specification, boolean prime) {
        for (int[] positions : specification) {
            if (prime) {
                shiftP(positions);
            } else {
                shift(positions);
            }
        }
    }

    EnumSet<Action> getAvailableActions() {
        return AVAILABLE_ACTIONS;
    }

    public ActionResult step(Action action) {
        switch (action) {
            case U: makeTurn(U_SHIFTS, false);
                break;
            case Up: makeTurn(U_SHIFTS, true);
                break;
            case F: makeTurn(F_SHIFTS, false);
                break;
            case Fp: makeTurn(F_SHIFTS, true);
                break;
            case R: makeTurn(R_SHIFTS, false);
                break;
            case Rp: makeTurn(R_SHIFTS, true);
                break;
            case D: makeTurn(D_SHIFTS, false);
                break;
            case Dp: makeTurn(D_SHIFTS, true);
                break;
            case L: makeTurn(L_SHIFTS, false);
                break;
            case Lp: makeTurn(L_SHIFTS, true);
                break;
            case B: makeTurn(B_SHIFTS, false);
                break;
            case Bp: makeTurn(B_SHIFTS, true);
                break;

            default: throw new RuntimeException("Not implemented " + action);
        }

        if (isFinalStateAchieved()) {
            return new ActionResult(getState(), 10000, true);
        } else {
            return new ActionResult(getState(), 0, false);
        }

    }


    //
    @Override
    public String toString() {
        String sb = "  " + get(0) + get(1) + "\n" +
                "  " + get(2) + get(3) + "\n" +
                get(4) + get(5) + get(6) + get(7) + get(8) + get(9) + get(10) + get(11) + "\n" +
                get(12) + get(13) + get(14) + get(15) + get(16) + get(17) + get(18) + get(19) + "\n" +
                "  " + get(20) + get(21) + "\n" +
                "  " + get(22) + get(23) + "\n";
        return sb;
    }

    State getState() {
        return new State(kostka);
    }
}
