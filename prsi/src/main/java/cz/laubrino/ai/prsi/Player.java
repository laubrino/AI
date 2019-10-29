package cz.laubrino.ai.prsi;

import java.util.*;

/**
 * @author tomas.laubr on 17.10.2019.
 */
public class Player {
    public static final float GAMMA = 0.9f;
    public static final float ALPHA = 0.4f;         // learning rate
    private static final double epsilon = 0.9;

    String name;
    ObservedState observedState;
    ObservedState observedState2;
    Action action2;
    Action action;

    QTable qTable;


    public Optional<Action> getAction2() {
        return Optional.ofNullable(action2);
    }

    public void setAction2(Action action2) {
        this.action2 = action2;
    }

    public Player(String name, QTable qTable) {
        this.name = name;
        this.qTable = qTable;
    }

    public ObservedState getObservedState2() {
        return observedState2;
    }

    public void setObservedState2(ObservedState observedState2) {
        this.observedState2 = observedState2;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
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

            return action.orElseGet(() -> {
                System.out.println("****************************** JO");
                return allAvailableEnvironmentActions.stream().skip(new Random().nextInt(allAvailableEnvironmentActions.size())).findAny().get();
            });
        }
    }

    public void learn(float reward) {
        if (observedState != null && action != null) {
            float predict = qTable.get(observedState.toString(), action);
            float target = reward + GAMMA * qTable.get(observedState2.toString(), action2);
            float newQ = predict + ALPHA * (target - predict);
            qTable.put(observedState.toString(), action, newQ);
        }
    }

    public QTable getqTable() {
        return qTable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(getName(), player.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", observedState=" + observedState +
                ", observedState2=" + observedState2 +
                ", action2=" + action2 +
                ", action=" + action +
                ", qTable=" + qTable +
                '}';
    }
}
