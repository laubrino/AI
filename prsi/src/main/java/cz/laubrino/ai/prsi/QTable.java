package cz.laubrino.ai.prsi;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.io.PrintStream;
import java.util.EnumMap;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @author tomas.laubr on 17.10.2019.
 */
public class QTable {
    Cache<String, EnumMap<Action, Float>> qTable = CacheBuilder.newBuilder().maximumSize(10_000_000).build();

    EnumMap<Action, Float> get(String observedState) {
        try {
            return qTable.get(observedState, () -> new EnumMap<>(Action.class));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    float get(String observedState, Action action) {
        try {
            EnumMap<Action, Float> enumMap = qTable.get(observedState, () -> {
                EnumMap<Action, Float> actionFloatEnumMap = new EnumMap<>(Action.class);
                actionFloatEnumMap.put(action, 0f);
                return actionFloatEnumMap;
            });
            return enumMap.computeIfAbsent(action, k -> 0f);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    void put(String observedState, Action action, float qValue) {
        try {
            EnumMap<Action, Float> enumMap = qTable.get(observedState, () -> {
                EnumMap<Action, Float> actionFloatEnumMap = new EnumMap<>(Action.class);
                actionFloatEnumMap.put(action, 0f);
                return actionFloatEnumMap;
            });
            enumMap.put(action, qValue);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * e.g. <tt>'stav2 [LIZNI 10.1][VYNES_ZALUDOVY_SVRSEK 2.5]'</tt>
     * @param ps
     */
    public void print(PrintStream ps) {
        qTable.asMap().entrySet().stream()
                .map(es -> es.getKey() + " " + es.getValue().entrySet().stream().map(e -> "[" + e.getKey() + " " + e.getValue() + "]").collect(Collectors.joining()))
                .forEach(ps::println);
    }
}
