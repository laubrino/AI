package cz.laubrino.ai.piskvorky;

import java.util.Objects;

/**
 * @author tomas.laubr on 10.10.2019.
 */
public class Position implements Comparable<Position>{
    int x;
    int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x &&
                y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public int compareTo(Position o) {
        if (x > o.x) {
            return 1;
        }
        if (x < o.x) {
            return -1;
        }

        return y - o.y;
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + "]";
    }
}
