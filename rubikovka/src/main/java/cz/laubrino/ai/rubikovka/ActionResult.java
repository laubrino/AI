package cz.laubrino.ai.rubikovka;

public class ActionResult {
    final State state;
    final float reward;
    final boolean done;

    public ActionResult(State state, float reward, boolean done) {
        this.state = state;
        this.reward = reward;
        this.done = done;
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

    @Override
    public String toString() {
        return "ActionResult{" +
                "state='" + state + '\'' +
                ", reward=" + reward +
                ", done=" + done +
                '}';
    }
}
