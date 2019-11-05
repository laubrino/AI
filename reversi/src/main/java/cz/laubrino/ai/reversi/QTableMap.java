package cz.laubrino.ai.reversi;

import java.io.PrintStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author tomas.laubr on 1.11.2019.
 */
public class QTableMap implements QTable<State, Action> {
    private Random randoms = new Random();
    private Map<State, Map<Action, Float>> qTable = new HashMap<>();

    @Override
    public void set(State state, Action action, float value) {
        Map<Action, Float> values = qTable.computeIfAbsent(state, k -> new HashMap<>());
        values.put(action, value);
    }

    @Override
    public float get(State state, Action action) {
        Map<Action, Float> values = qTable.computeIfAbsent(state, k -> new HashMap<>());
        return values.computeIfAbsent(action, action1 -> 0f);
    }

    @Override
    public float max(State state) {
        Map<Action, Float> values = qTable.computeIfAbsent(state, k -> new HashMap<>());
        Optional<Float> optMax = values.values().stream().max(Float::compareTo);
        return optMax.orElse(0f);
    }

    @Override
    public Optional<Action> maxAction(State state) {
        float maxValue = max(state);
        Map<Action, Float> values = qTable.computeIfAbsent(state, k -> new HashMap<>());
        Set<Action> maxActions = values.entrySet().stream()
                .filter(actionFloatEntry -> actionFloatEntry.getValue() >= maxValue)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
        return maxActions.stream().skip(randoms.nextInt(maxActions.size())).findFirst();
    }

    @Override
    public void print(PrintStream ps) {
        qTable.forEach((state,values) -> {
            ps.print(state.toString());
            ps.print(": ");
            ps.println(values.toString());
        });

    }
}
