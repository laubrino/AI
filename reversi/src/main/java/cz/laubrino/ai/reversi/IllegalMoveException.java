package cz.laubrino.ai.reversi;

/**
 * @author tomas.laubr on 4.11.2019.
 */
public class IllegalMoveException extends Exception {
    public IllegalMoveException(String message) {
        super(message);
    }
}
