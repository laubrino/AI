package cz.laubrino.ai.piskvorky;

import java.util.Objects;

/**
 * Put a field on a position
 * @author tomas.laubr on 10.10.2019.
 */
public class Action implements Comparable<Action> {
    Position position;

    public Action(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public int compareTo(Action o) {
        return position.compareTo(o.position);
    }

    @Override
    public String toString() {
        return position.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Action action = (Action) o;
        return Objects.equals(getPosition(), action.getPosition());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPosition());
    }
}
