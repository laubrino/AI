package cz.laubrino.ai.rubikovka;

import java.util.Random;

public class Agent {
    private static final float ALPHA = 0.5f;
    private static final float GAMMA = 0.9f;
    private static final float EPSILON_DECAY = 0.99995f;

    private final QTable<Short> qTable;
    private Random randoms = new Random();
    volatile private double epsilon = 0.5f;
    volatile private long learnsCounter = 0;

    public Agent(QTable qTable) {
        this.qTable = qTable;
    }

    void qLearn(State newS, float r, Action a, State s) {
        if (s != null) {
            float oldQ = qTable.get(s, a);
            float newQ = oldQ + ALPHA*(r + GAMMA * qTable.max(newS) - oldQ);
            qTable.set(s, a, (short)newQ);

            learnsCounter++;
            if (learnsCounter > 1000) {
                epsilon = epsilon*EPSILON_DECAY;
                learnsCounter = 0;
            }
        }
    }

    public Action chooseAction(State state, double epsilon) {
        if (randoms.nextDouble() < epsilon) {       // take random action
            return Action.VALUES[randoms.nextInt(Action.VALUES.length)];
        } else {
            return qTable.maxAction(state);
        }
    }

    public Action chooseAction(State state) {
        return chooseAction(state, this.epsilon);
    }

    public double getEpsilon() {
        return epsilon;
    }

    public void setEpsilon(double epsilon) {
        this.epsilon = epsilon;
    }
}
