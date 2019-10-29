package cz.laubrino.ai.patnact;

import java.util.Random;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

/**
 * @author tomas.laubr on 29.10.2019.
 */
public class Player {
    private static final float ALPHA = 0.5f;
    private static final float GAMMA = 0.9f;
    private static final double EPSILON = 0.2f;

    private final QTable qTable;
    private String s;
    Random randoms = new Random();

    public Player(QTable qTable) {
        this.qTable = qTable;
    }

    void qLearn(String newS, float r, Action a) {
        if (s != null) {
            float oldQ = qTable.get(s, a);
            float newQ = oldQ + ALPHA*(r + GAMMA * qTable.max(newS) - oldQ);
            qTable.set(s, a, newQ);
        }

        s = newS;
    }

    Action chooseAction(String state) {
        if (randoms.nextDouble() < EPSILON) {
            return Action.values()[randoms.nextInt(Action.values().length)];
        } else {
            return qTable.maxAction(state);
        }
    }
}