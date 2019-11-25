package cz.laubrino.ai.rubikovka;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author tomas.laubr on 22.11.2019.
 */
public class State {
    final byte[] kostka;

    public State(byte[] kostka) {
        this.kostka = Arrays.copyOf(kostka, kostka.length);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return Objects.equals(kostka, state.kostka);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kostka);
    }

    @Override
    public String toString() {
        return Arrays.toString(kostka);
    }
}
