package cz.laubrino.ai.reversi;

/**
 * @author tomas.laubr on 1.11.2019.
 */
public enum Policko {
    EMPTY("."),
    WHITE("o"),
    BLACK("x")
    ;

    private String s;

    Policko(String s) {
        this.s = s;
    }

    @Override
    public String toString() {
        return s;
    }

    public char toChar() {
        return s.charAt(0);
    }

    public static Policko parse(String s) {
        for (Policko p : Policko.values()) {
            if (p.s.equals(s)) {
                return p;
            }
        }

        throw new RuntimeException("No color '" + s + "'");
    }

    public static Policko parse(char ch) {
        for (Policko p : Policko.values()) {
            if (p.toChar() == ch) {
                return p;
            }
        }

        throw new RuntimeException("No color '" + ch + "'");
    }

}
