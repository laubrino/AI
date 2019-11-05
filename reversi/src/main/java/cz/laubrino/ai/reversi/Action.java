package cz.laubrino.ai.reversi;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author tomas.laubr on 1.11.2019.
 */
public class Action {
    private static final EnumMap<Policko, Action> PASS_ACTIONS = new EnumMap<>(Policko.class);
    private static final Pattern PATTERN = Pattern.compile("\\[(-?\\d),(-?\\d)\\](.)");


    static {
        PASS_ACTIONS.put(Policko.WHITE, new Action(-1,-1,Policko.WHITE));
        PASS_ACTIONS.put(Policko.BLACK, new Action(-1,-1,Policko.BLACK));
    }

    private final int x;
    private final int y;
    private final Policko p;

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

    /**
     * @param action expected format is '[x,y]C' e.g. "[4,5]x" or "[-1,-1]o"
     */
    public Action(String action) {
        Matcher matcher = PATTERN.matcher(action);

        if (!matcher.matches()) {
            throw new RuntimeException(action);
        }

        this.x = Integer.parseInt(matcher.group(1));
        this.y = Integer.parseInt(matcher.group(2));
        this.p = Policko.parse(matcher.group(3));
    }


    public boolean isPassAction() {
        return x == -1 || y == -1;
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
