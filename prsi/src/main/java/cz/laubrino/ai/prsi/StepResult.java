package cz.laubrino.ai.prsi;

/**
 * @author tomas.laubr on 17.10.2019.
 */
public class StepResult {
    ObservedState observedState;
    float reward;
    boolean done;
    String info;

    public StepResult(ObservedState observedState, float reward, boolean done, String info) {
        this.observedState = observedState;
        this.reward = reward;
        this.done = done;
        this.info = info;
    }

    public ObservedState getObservedState() {
        return observedState;
    }

    public float getReward() {
        return reward;
    }

    public boolean isDone() {
        return done;
    }

    public String getInfo() {
        return info;
    }
}
