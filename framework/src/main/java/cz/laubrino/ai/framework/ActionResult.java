package cz.laubrino.ai.framework;

public class ActionResult {
    private final State state;
    private final float reward;
    private final Type type;
    private String info;

    public ActionResult(State state, float reward, Type type) {
        this(state, reward, type, null);
    }

    public ActionResult(State state, float reward, Type type, String info) {
        this.state = state;
        this.reward = reward;
        this.type = type;
        this.info = info;
    }

    public State getState() {
        return state;
    }

    public float getReward() {
        return reward;
    }

    public Type getType() {
        return type;
    }

    public boolean isDone() {
        return type == Type.OK || type == Type.FAIL;
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
                    ", type=" + type +
                    ", info='" + info + '\'' +
                    '}';
        } else {
            return "ActionResult{" +
                    "state=" + state +
                    ", reward=" + reward +
                    ", type=" + type +
                    '}';
        }
    }

    public enum Type {
        OK, FAIL, CONTINUE
    }
}
