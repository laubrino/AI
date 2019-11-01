package cz.laubrino.ai.reversi;

/**
 * @author tomas.laubr on 1.11.2019.
 */
public class StepResult {
    private final State s;
    private final float reward;
    private final boolean done;
    private final Reason reason;

    public StepResult(State s, float reward, boolean done, Reason reason) {
        this.s = s;
        this.reward = reward;
        this.done = done;
        this.reason = reason;
    }

    public State getS() {
        return s;
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
                "s=" + s +
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
