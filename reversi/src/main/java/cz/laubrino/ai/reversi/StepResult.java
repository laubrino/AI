package cz.laubrino.ai.reversi;

/**
 * @author tomas.laubr on 1.11.2019.
 */
public class StepResult {
    private final State state;
    private final float reward;
    private final boolean done;
    private final Status status;

    public StepResult(State state, float reward, boolean done, Status status) {
        this.state = state;
        this.reward = reward;
        this.done = done;
        this.status = status;
    }

    public State getState() {
        return state;
    }

    public float getReward() {
        return reward;
    }

    public boolean isDone() {
        return done;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "StepResult{" +
                "s=" + state +
                ", reward=" + reward +
                ", done=" + done +
                ", reason=" + status +
                '}';
    }

    enum Status {
        ILLEGAL_ACTION,
        WIN,
        CONTINUE,
        DRAW,
        LOSE
    }
}
