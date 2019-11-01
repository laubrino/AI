package cz.laubrino.ai.reversi;

import java.util.Objects;

/**
 * @author tomas.laubr on 1.11.2019.
 */
public class Action {
    private final int x;
    private final int y;
    private final Policko p;

    /**
     *  a black/white on board position [x,y]
     * @param x
     * @param y
     * @param p
     */
    public Action(int x, int y, Policko p) {
        if (p == Policko.EMPTY) {
            throw new AssertionError(p);
        }
        this.x = x;
        this.y = y;
        this.p = p;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Policko getP() {
        return p;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Action action = (Action) o;
        return getX() == action.getX() &&
                getY() == action.getY() &&
                getP() == action.getP();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY(), getP());
    }

    @Override
    public String toString() {
        return "[" + x + "," + y + "]" + p.toString();
    }
}
