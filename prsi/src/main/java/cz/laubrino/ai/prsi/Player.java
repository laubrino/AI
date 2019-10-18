package cz.laubrino.ai.prsi;

import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

/**
 * @author tomas.laubr on 17.10.2019.
 */
public class Player {
    private static final double epsilon = 0.9;

    String name;
    ObservedState observedState;
    QTable qTable;

    Action prevAction;

    public Optional<Action> getPrevAction() {
        return Optional.ofNullable(prevAction);
    }

    public void setPrevAction(Action prevAction) {
        this.prevAction = prevAction;
    }

    public Optional<ObservedState> getPrevObservedState() {
        return Optional.ofNullable(prevObservedState);
    }

    public void setPrevObservedState(ObservedState prevObservedState) {
        this.prevObservedState = prevObservedState;
    }

    ObservedState prevObservedState;

    public Player(String name, QTable qTable) {
        this.name = name;
        this.qTable = qTable;
    }

    public ObservedState getObservedState() {
        return observedState;
    }

    public void setObservedState(ObservedState observedState) {
        this.observedState = observedState;
    }

    public String getName() {
        return name;
    }

    /**
     * Either choose best action according to qTable or sample a random action
     * @return
     */
    public Action chooseAction(Set<Action> allAvailableEnvironmentActions) {
        if (Math.random() < epsilon && !allAvailableEnvironmentActions.isEmpty()) {       // udelej nahodnou akci
            return allAvailableEnvironmentActions.stream().skip(new Random().nextInt(allAvailableEnvironmentActions.size())).findAny().get();
        } else {
            // TODO: tohle je blbe, tady se neco musi opravit
            Optional<Action> action = qTable.get(observedState.stringSnapshot).entrySet().stream()
                    .max((es1, es2) -> Float.compare(es1.getValue(), es2.getValue()))
                    .map(Map.Entry::getKey);

            return action.orElseGet(() -> allAvailableEnvironmentActions.stream().skip(new Random().nextInt(allAvailableEnvironmentActions.size())).findAny().get());
        }
    }

    public QTable getqTable() {
        return qTable;
    }
}
