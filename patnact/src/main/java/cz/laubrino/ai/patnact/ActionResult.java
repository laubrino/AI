package cz.laubrino.ai.patnact;

/**
 * @author tomas.laubr on 29.10.2019.
 */
public class ActionResult {
    final String state;
    final float reward;
    final boolean done;

    public ActionResult(String state, float reward, boolean done) {
        this.state = state;
        this.reward = reward;
        this.done = done;
    }

    public String getState() {
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
