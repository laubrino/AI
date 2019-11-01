package cz.laubrino.ai.reversi;

/**
 * @author tomas.laubr on 1.11.2019.
 */
public class StepResult {
    private final State state;
    private final float reward;
    private final boolean done;
    private final Reason reason;

    public StepResult(State state, float reward, boolean done, Reason reason) {
        this.state = state;
        this.reward = reward;
        this.done = done;
        this.reason = reason;
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

    public Reason getReason() {
        return reason;
    }

    @Override
    public String toString() {
        return "StepResult{" +
                "s=" + state +
                ", reward=" + reward +
                ", done=" + done +
                ", reason=" + reason +
                '}';
    }

    enum Reason {
        INVALID_MOVE,
        WIN,
        CONTINUE
    }
}
