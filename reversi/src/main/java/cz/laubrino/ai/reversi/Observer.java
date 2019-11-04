package cz.laubrino.ai.reversi;

/**
 * @author tomas.laubr on 4.11.2019.
 */
public interface Observer {
    void notify(StepResult stepResult);
}
