package cz.laubrino.ai.rubikovka;

import java.util.BitSet;
import java.util.Objects;

/**
 * @author tomas.laubr on 22.11.2019.
 */
public class State {
    final BitSet kostka;

    public State(BitSet kostka) {
        this.kostka = (BitSet)kostka.clone();
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
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<24;i++) {
            int colorIndex = (kostka.get(i * 3) ? 4 : 0) + (kostka.get(i * 3 + 1) ? 2 : 0) + (kostka.get(i * 3 + 2) ? 1 : 0);
            sb.append(Environment.Color.getByIndex(colorIndex));
        }
        return sb.toString();
    }
}
