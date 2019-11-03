package cz.laubrino.ai.reversi;

import java.util.EnumMap;
import java.util.Objects;

/**
 * @author tomas.laubr on 1.11.2019.
 */
public class Action {
    private static final EnumMap<Policko, Action> PASS_ACTIONS = new EnumMap<>(Policko.class);

    static {
        PASS_ACTIONS.put(Policko.WHITE, new Action(-1,-1,Policko.WHITE));
        PASS_ACTIONS.put(Policko.BLACK, new Action(-1,-1,Policko.BLACK));
    }

    private final int x;
    private final int y;
    private final Policko p;

    public boolean isPassAction() {
        return x == -1 || y == -1;
    }

    public static Action getPassAction(Policko policko) {
        return PASS_ACTIONS.get(policko);
    }

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
