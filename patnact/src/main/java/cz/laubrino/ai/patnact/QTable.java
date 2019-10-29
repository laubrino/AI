package cz.laubrino.ai.patnact;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tomas.laubr on 29.10.2019.
 */
public class QTable {
    private static final int ACTIONS_COUNT = Action.values().length;

    private Map<String, Float[]> qTable = Collections.synchronizedMap(new HashMap<>());

    void set(String state, Action action, float value) {
        Float[] values = qTable.computeIfAbsent(state, k -> {
            Float[] vals = new Float[ACTIONS_COUNT];
            Arrays.fill(vals, 0f);
            return vals;
        });
        values[action.ordinal()] = value;
    }

    float get(String state, Action action) {
        Float[] values = qTable.computeIfAbsent(state, k -> {
            Float[] vals = new Float[ACTIONS_COUNT];
            Arrays.fill(vals, 0f);
            return vals;
        });
        return values[action.ordinal()];
    }

    float max(String state) {
        Float[] values = qTable.computeIfAbsent(state, k -> {
            Float[] vals = new Float[ACTIONS_COUNT];
            Arrays.fill(vals, 0f);
            return vals;
        });

        return Arrays.stream(values).max(Float::compareTo).orElseThrow(() -> new RuntimeException("There has to be one..."));
    }

    Action maxAction(String state) {
        Float[] values = qTable.computeIfAbsent(state, k -> {
            Float[] vals = new Float[ACTIONS_COUNT];
            Arrays.fill(vals, 0f);
            return vals;
        });

        float maxValue = -Float.MAX_VALUE;
        Action maxAction = Action.MOVE_LEFT;
        for (int i=0; i<values.length;i++) {
            if (values[i] > maxValue) {
                maxValue = values[i];
                maxAction = Action.values()[i];
            }
        }

        return maxAction;
    }

    void print(PrintStream ps) {
        qTable.forEach((state,values) -> {
            ps.print(state);
            ps.print(": ");
            ps.println(Arrays.deepToString(values));
        });
    }
}
