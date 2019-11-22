package cz.laubrino.ai.rubikovka;

/**
 * See <a href="http://www.rubiksplace.com/move-notations/">Move notations</a>
 * @author tomas.laubr on 21.11.2019.
 */
public enum Action {
    F, B, R, L, U, D,
    Fp, Bp, Rp, Lp, Up, Dp;

    public static Action[] VALUES = values();
}
