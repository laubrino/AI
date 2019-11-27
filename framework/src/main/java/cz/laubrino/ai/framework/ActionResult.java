package cz.laubrino.ai.framework;

public class ActionResult {
    private final State state;
    private final float reward;
    private final boolean done;
    private String info;

    public ActionResult(State state, float reward, boolean done) {
        this(state, reward, done, null);
    }

    public ActionResult(State state, float reward, boolean done, String info) {
        this.state = state;
        this.reward = reward;
        this.done = done;
        this.info = info;
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

    public String getInfo() {
        return info;
    }

    @Override
    public String toString() {
        if (info != null) {
            return "ActionResult{" +
                    "state=" + state +
                    ", reward=" + reward +
                    ", done=" + done +
                    ", info='" + info + '\'' +
                    '}';
        } else {
            return "ActionResult{" +
                    "state=" + state +
                    ", reward=" + reward +
                    ", done=" + done +
                    '}';
        }
    }
}
