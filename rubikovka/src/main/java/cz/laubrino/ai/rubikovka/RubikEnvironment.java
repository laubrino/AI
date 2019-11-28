package cz.laubrino.ai.rubikovka;

import cz.laubrino.ai.framework.ActionResult;
import cz.laubrino.ai.framework.Environment;
import cz.laubrino.ai.framework.State;

import java.util.Random;

import static cz.laubrino.ai.rubikovka.RubikEnvironment.Color.*;

/**
 * <pre>
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
 * </pre>
 *
 * @author tomas.laubr on 28.11.2019.
 */
public class RubikEnvironment implements Environment<Action> {
    /**
     * rubik cube 2x2x2, 3(4)bits/color, 24 surfaces => 12 bytes
     * |00.01|02.03|04.05|.....|22.23|    data in array
     */
    private byte[] kostka = new byte[12];
    private static final Color[] INITIAL_SURFACES = new Color[] {O, O, O, O, G, G, W,W, B,B,Y,Y,G,G,W,W,B,B,Y,Y,R,R,R,R};
    private Random randoms = new Random();

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

    @Override
    public boolean isFinalStateAchieved() {
        return isFaceSameColor(0,1,2,3) && isFaceSameColor(4,5,12,13) && isFaceSameColor(6,7,14,15) && isFaceSameColor(8,9,16,17)
                && isFaceSameColor(10,11,18,19) && isFaceSameColor(20,21,22,23);
    }

    @Override
    public void reset() {
        for (int i=0;i<24;i++) {
            set(i, INITIAL_SURFACES[i]);
        }

        shuffle(1000);
    }

    void resetNoShuffle() {
        for (int i=0;i<24;i++) {
            set(i, INITIAL_SURFACES[i]);
        }
    }

    /**
     * randomly move Rubik's cube
     */
    void shuffle(int numberOfShuffleMoves) {
        for (int i=0;i<numberOfShuffleMoves;i++) {
            int randomActionIndex = randoms.nextInt(Action.VALUES.length);
            step(Action.VALUES[randomActionIndex]);
        }
    }

    @Override
    public Action[] getAvailableActions() {
        return Action.VALUES;
    }

    @Override
    public State getState() {
        return new RubikState(kostka);
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

    private boolean isFaceSameColor(int surface0, int surface1, int surface2, int surface3) {
        Color color = get(surface0);

        return color == get(surface1) && color == get(surface2) && color == get(surface3);
    }

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

    private void makeTurn(int[][] specification) {
        for (int[] positions : specification) {
            shift(positions);
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

    @Override
    public ActionResult step(Action action) {
        switch (action) {
            case F: makeTurn(F_SHIFTS);
                break;
            case R: makeTurn(R_SHIFTS);
                break;
            case D: makeTurn(D_SHIFTS);
                break;

            default: throw new RuntimeException("Not implemented " + action);
        }

        if (isFinalStateAchieved()) {
            return new ActionResult(getState(), 10000, true);
        } else {
            return new ActionResult(getState(), 0, false);
        }

    }

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
}
