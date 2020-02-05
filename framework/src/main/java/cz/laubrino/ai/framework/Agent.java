package cz.laubrino.ai.framework;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Agent<A extends Enum<A>> {
    private final float alphaDecay;
    private final float gamma;
    private final float epsilonDecay;
    private final QTable<A> qTable;

    private volatile float alpha;
    private volatile float epsilon;
    private final AtomicInteger learnsCounter = new AtomicInteger(0);
    private Random randoms = new Random();
    private final List<AgentObserver> observers = new ArrayList<>();

    /**
     * @param actions actions enum
     */
    public Agent(AgentConfiguration agentConfiguration, Class<A> actions) {
        this.alpha = agentConfiguration.getAlpha();
        this.alphaDecay = agentConfiguration.getAlphaDecay();
        this.gamma = agentConfiguration.getGamma();
        this.epsilon = agentConfiguration.getEpsilon();
        this.epsilonDecay = agentConfiguration.getEpsilonDecay();

        this.qTable = new QTable<>(actions);
    }

    void qLearn(State newS, float r, A a, State s) {
        if (s != null) {
            float oldQ = qTable.get(s, a);
            float newQ = oldQ + alpha *(r + gamma * qTable.max(newS) - oldQ);
            qTable.set(s, a, (short)newQ);

            notifyQChange(oldQ, newQ);

            int i = learnsCounter.updateAndGet(operand -> {
                if (operand >= 1000) {
                    return 0;
                } else {
                    return operand + 1;
                }
            });

            if (i == 0) {
                epsilon *= epsilonDecay;
                alpha *= alphaDecay;
            }
        }
    }

    private void notifyQChange(float oldQ, float newQ) {
        if (!observers.isEmpty()) {
            try {
                float absolute = Math.abs(newQ - oldQ);
                int percent = (int)(absolute/oldQ*100);
                for (AgentObserver observer : observers) {
                    observer.qValueChanged(absolute, percent);
                }
            } catch (ArithmeticException e) {
                // divide by zero
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

    public void addObserver(AgentObserver observer) {
        this.observers.add(observer);
    }
}
