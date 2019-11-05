package cz.laubrino.ai.reversi;

import java.io.PrintStream;
import java.util.Optional;

/**
 * @author tomas.laubr on 5.11.2019.
 */
public interface QTable<S,A> {
    void set(S state, A action, float value);
    float get(S state, A action);

    /**
     * max value for any action in a state
     */
    float max(S state);
    Optional<A> maxAction(S state);
    void print(PrintStream ps);
}
