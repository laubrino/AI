package cz.laubrino.ai.framework;

/**
 * @author tomas.laubr on 28.11.2019.
 */
@FunctionalInterface
public interface EnvironmentFactory<E extends Enum<E>> {
    Environment<E> getInstance();
}
