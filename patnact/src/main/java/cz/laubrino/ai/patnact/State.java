package cz.laubrino.ai.patnact;

import java.util.Arrays;

/**
 * @author tomas.laubr on 30.10.2019.
 */
public class State extends cz.laubrino.ai.framework.State {
    final byte[] bytes;

    public State(byte[] bytes) {
        this.bytes = bytes;
    }

    /**
     * For testing only
     * @param s
     */
    public State(String s) {
        this.bytes = s.getBytes();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return Arrays.equals(bytes, state.bytes);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(bytes);
    }

    @Override
    public String toString() {
        return Arrays.toString(bytes);
    }
}
