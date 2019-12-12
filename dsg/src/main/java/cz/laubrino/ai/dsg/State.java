package cz.laubrino.ai.dsg;

import java.util.Objects;

/**
 * @author tomas.laubr on 10.12.2019.
 */
public class State extends cz.laubrino.ai.framework.State {
    private final int x;
    private final int y;
    private final boolean damaged;
    private final int prize;    // =0 if there is no prize, P=i if there is a prize at position Pi

    public State(int x, int y, boolean damaged, int prize) {
        this.x = x;
        this.y = y;
        this.damaged = damaged;
        this.prize = prize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return x == state.x &&
                y == state.y &&
                damaged == state.damaged &&
                prize == state.prize;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, damaged, prize);
    }

    @Override
    public String toString() {
        return "State{" +
                "X=" + x +
                ", Y=" + y +
                ", damaged=" + damaged +
                ", prize=" + prize +
                '}';
    }
}
