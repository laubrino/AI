package cz.laubrino.ai.patnact;

import java.util.Random;

/**
 * @author tomas.laubr on 29.10.2019.
 */
public class Agent {
    private static final float ALPHA = 0.5f;
    private static final float GAMMA = 0.9f;
    private static final double EPSILON = 0.1f;

    private final QTable qTable;
    Random randoms = new Random();

    public Agent(QTable qTable) {
        this.qTable = qTable;
    }

    void qLearn(String newS, float r, Action a, String s) {
        if (s != null) {
            float oldQ = qTable.get(s, a);
            float newQ = oldQ + ALPHA*(r + GAMMA * qTable.max(newS) - oldQ);
            qTable.set(s, a, newQ);
        }
    }

    Action chooseAction(String state) {
        if (randoms.nextDouble() < EPSILON) {       // take random action
            return Action.values()[randoms.nextInt(Action.values().length)];
        } else {
            return qTable.maxAction(state);
        }
    }
}
