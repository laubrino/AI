package cz.laubrino.ai.framework;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.IntUnaryOperator;

public class Agent<A extends Enum<A>> {
    private final float alpha;
    private final float gamma;
    private final float epsilonDecay;
    private final QTable<A> qTable;

    private volatile float epsilon;
    private final AtomicInteger learnsCounter = new AtomicInteger(0);

    private Random randoms = new Random();

    /**
     *
     * @param alpha learning rate (0..1)
     * @param gamma discount factor (0..1)
     * @param epsilon initial epsilon (0..1)
     * @param epsilonDecay (0..1)  e.g. 0.99995f. Or 1 for no decay.
     * @param actions actions enum
     */
    public Agent(float alpha, float gamma, float epsilon, float epsilonDecay, Class<A> actions) {
        this.alpha = alpha;
        this.gamma = gamma;
        this.epsilon = epsilon;
        this.epsilonDecay = epsilonDecay;

        this.qTable = new QTable<>(actions);
    }

    void qLearn(State newS, float r, A a, State s) {
        if (s != null) {
            float oldQ = qTable.get(s, a);
            short newQ = (short)(oldQ + alpha *(r + gamma * qTable.max(newS) - oldQ));
            qTable.set(s, a, newQ);

            int i = learnsCounter.updateAndGet(operand -> {
                if (operand >= 1000) {
                    return 0;
                } else {
                    return operand + 1;
                }
            });

            if (i == 0) {
                epsilon *= epsilonDecay;
            }
        }
    }

    /**
     *
     * @param state
     * @param eGreedyExplore if {@code true} select random action with some probability. Otherwise choose best action.
     * @return
     */
    public A chooseAction(State state, A[] availableActions, boolean eGreedyExplore) {
        if (eGreedyExplore && randoms.nextDouble() < epsilon) {       // take random action
            return availableActions[randoms.nextInt(availableActions.length)];
        } else {
            return qTable.maxAction(state);
        }
    }

    public A chooseAction(State state, A[] availableActions) {
        return chooseAction(state, availableActions, true);
    }

    /**
     * For testing an already learned agent.
     * @param state
     * @return
     */
    public A chooseAction(State state) {
        return chooseAction(state, null, false);
    }

    public double getEpsilon() {
        return epsilon;
    }

    public void setEpsilon(float epsilon) {
        this.epsilon = epsilon;
    }
}
