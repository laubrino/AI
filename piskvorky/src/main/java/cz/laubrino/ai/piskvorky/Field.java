package cz.laubrino.ai.piskvorky;

/**
 * @author tomas.laubr on 10.10.2019.
 */
public enum Field {
    O("O"),
    X("X"),
    NIC(" ");

    private String s;

    Field(String s) {
        this.s = s;
    }



    @Override
    public String toString() {
        return s;
    }
}
