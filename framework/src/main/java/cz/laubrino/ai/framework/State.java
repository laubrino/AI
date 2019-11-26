package cz.laubrino.ai.framework;

/**
 * Instances are stored to QTable as keys
 * @author tomas.laubr on 26.11.2019.
 */
public abstract class State {
    @Override
    public abstract boolean equals(Object obj);

    @Override
    public abstract int hashCode();

    /**
     * For output
     * @return
     */
    @Override
    public abstract String toString();
}
