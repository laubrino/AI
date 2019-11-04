package cz.laubrino.ai.reversi;

/**
 * @author tomas.laubr on 4.11.2019.
 */
public class Agent implements Observer {
    State state;

    public void observeState(State state) {
        this.state = state;
    }

    public void notify(StepResult stepResult) {

    }

}
