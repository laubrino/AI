package cz.laubrino.ai.reversi;

/**
 * @author tomas.laubr on 1.11.2019.
 */
public enum Policko {
    EMPTY("."),
    WHITE("o"),
    BLACK("x")
//    WHITE("○"),
//    BLACK("●")
    ;

    private String s;

    Policko(String s) {
        this.s = s;
    }

    @Override
    public String toString() {
        return s;
    }
}
