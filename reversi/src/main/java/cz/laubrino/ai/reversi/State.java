package cz.laubrino.ai.reversi;

import java.util.Arrays;

/**
 * Observed state
 * @author tomas.laubr on 1.11.2019.
 */
public class State {
    Policko[] state = new Policko[Environment.BOARD_SIZE * Environment.BOARD_SIZE];

    public State(Policko[] state) {
        System.arraycopy(state, 0, this.state, 0, Environment.BOARD_SIZE * Environment.BOARD_SIZE);
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
        StringBuilder sb = new StringBuilder("[");
        for (int i=0;i<Environment.BOARD_SIZE * Environment.BOARD_SIZE;i++) {
            sb.append(state[i] == Policko.EMPTY ? "." : state[i].toString());
            if (i != 0 && i!= (Environment.BOARD_SIZE * Environment.BOARD_SIZE -1) && (i%Environment.BOARD_SIZE == Environment.BOARD_SIZE-1)) {
                sb.append("|");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
