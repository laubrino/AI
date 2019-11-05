package cz.laubrino.ai.reversi;

import java.util.Random;
import java.util.Set;

/**
 * @author tomas.laubr on 4.11.2019.
 */
public class Agent implements Observer {
    private static final float ALPHA = 0.2f;    // learn factor
    private static final float GAMMA = 0.9f;    // high for qTable, low for NN

    Random randoms = new Random();
    private final QTable<State, Action> qTable;
    private float EPSILON = 0.1f;               // random factor

    private StepResult latestStepResult;
    private StepResult previousStepResult;
    private Action latestAction;

    public Agent(QTable<State, Action> qTable) {
        this.qTable = qTable;
    }

    public void notify(StepResult newStepResult) {
        if (newStepResult.equals(latestStepResult)) {      // do nothing if nothing has changed
            return;
        }

        previousStepResult = latestStepResult;
        latestStepResult = newStepResult;

        if (previousStepResult != null && latestAction != null) {
            qLearn(latestStepResult.getState(), latestStepResult.getReward(), latestAction, previousStepResult.getState());
        }

    }

    private void qLearn(State newS, float r, Action a, State s) {
        if (s != null) {
            float oldQ = qTable.get(s, a);
            float newQ = oldQ + ALPHA*(r + GAMMA * qTable.max(newS) - oldQ);
            qTable.set(s, a, newQ);
        }
    }

    Action chooseAction(State state, Set<Action> availableActions) {
        if (randoms.nextDouble() < EPSILON) {       // take random action
            this.latestAction = availableActions.stream().skip(randoms.nextInt(availableActions.size())).findFirst().get();
        } else {
            this.latestAction = qTable.maxAction(state)
                    .orElseGet(() -> availableActions.stream().skip(randoms.nextInt(availableActions.size())).findFirst().get());
        }

        return this.latestAction;
    }


}
