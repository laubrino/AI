package cz.laubrino.ai.reversi;

import java.util.Arrays;

/**
 * Observed state
 * @author tomas.laubr on 1.11.2019.
 */
public class State {
    private static final char EMPTY_CHAR = '.';
    private static final char SPLIT_CHAR = '|';
    private static final char PREFIX_CHAR = '[';
    private static final char SUFFIX_CHAR = ']';

    Policko[] state = new Policko[Environment.BOARD_SIZE * Environment.BOARD_SIZE];

    /**
     * Will make a deep copy of the {@code state} array
     * @param state
     */
    public State(Policko[] state) {
        System.arraycopy(state, 0, this.state, 0, Environment.BOARD_SIZE * Environment.BOARD_SIZE);
    }

    /**
     * Reverse of {@link #toString()} method
     * @param stateAsString
     */
    public State(String stateAsString) {
        int i=0;
        for (char ch : stateAsString.toCharArray()) {
            switch (ch) {
                case EMPTY_CHAR:
                    state[i++] = Policko.EMPTY;
                    break;
                case PREFIX_CHAR:
                case SUFFIX_CHAR:
                case SPLIT_CHAR:
                    break;
                default:
                    if (ch == Policko.WHITE.toChar()) {
                        state[i++] = Policko.WHITE;
                    }
                    if (ch == Policko.BLACK.toChar()) {
                        state[i++] = Policko.BLACK;
                    }
                    break;
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state1 = (State) o;
        return Arrays.equals(state, state1.state);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(state);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_CHAR);
        for (int i=0;i<Environment.BOARD_SIZE * Environment.BOARD_SIZE;i++) {
            sb.append(state[i] == Policko.EMPTY ? EMPTY_CHAR : state[i].toString());
            if (i != 0 && i!= (Environment.BOARD_SIZE * Environment.BOARD_SIZE -1) && (i%Environment.BOARD_SIZE == Environment.BOARD_SIZE-1)) {
                sb.append(SPLIT_CHAR);
            }
        }
        sb.append(SUFFIX_CHAR);
        return sb.toString();
    }
}
