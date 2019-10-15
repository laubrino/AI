package cz.laubrino.ai.piskvorky;

/**
 * @author tomas.laubr on 11.10.2019.
 */
public class ActionResult {
    State state;
    float reward;
    boolean done;
    String info;

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
        return "ActionResult{" +
                ", reward=" + reward +
                ", done=" + done +
                ", info='" + info + '\'' +
                '}';
    }
}
