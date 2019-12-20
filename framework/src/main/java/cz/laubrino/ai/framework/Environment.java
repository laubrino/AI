package cz.laubrino.ai.framework;

/**
 * @author tomas.laubr on 24.10.2019.
 */
public interface Environment<A extends Enum<A>> {
    void reset();

    A[] getAvailableActions();

    ActionResult step(A action);

    State getState();
}
