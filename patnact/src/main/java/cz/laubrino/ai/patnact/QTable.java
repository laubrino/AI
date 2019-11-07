package cz.laubrino.ai.patnact;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.stream.Stream;

/**
 * @author tomas.laubr on 29.10.2019.
 */
public class QTable {
    private static final int ACTIONS_COUNT = Action.values().length;

    private Map<State, Float[]> qTable = Collections.synchronizedMap(new HashMap<>());
    private Random randoms = new Random();

    void set(State state, Action action, float value) {
        Float[] values = qTable.computeIfAbsent(state, k -> {
            Float[] vals = new Float[ACTIONS_COUNT];
            Arrays.fill(vals, 0f);
            return vals;
        });
        values[action.ordinal()] = value;
    }

    float get(State state, Action action) {
        Float[] values = qTable.computeIfAbsent(state, k -> {
            Float[] vals = new Float[ACTIONS_COUNT];
            Arrays.fill(vals, 0f);
            return vals;
        });
        return values[action.ordinal()];
    }

    float max(State state) {
        Float[] values = qTable.computeIfAbsent(state, k -> {
            Float[] vals = new Float[ACTIONS_COUNT];
            Arrays.fill(vals, 0f);
            return vals;
        });

        return Arrays.stream(values).max(Float::compareTo).orElseThrow(() -> new RuntimeException("There has to be one..."));
    }

    Action maxAction(State state) {
        Float[] values = qTable.computeIfAbsent(state, k -> {
            Float[] vals = new Float[ACTIONS_COUNT];
            Arrays.fill(vals, 0f);
            return vals;
        });

        float maxValue = Stream.of(values).max(Float::compareTo).get();

        List<Action> maxActions = new ArrayList<>();
        for (int i=0;i<Action.values().length;i++) {
            if (values[i] == maxValue) {
                maxActions.add(Action.values()[i]);
            }
        }

        if(!maxActions.isEmpty()) {
            return maxActions.stream().skip(randoms.nextInt(maxActions.size())).findFirst().get();
        } else {
            return Action.values()[randoms.nextInt(Action.values().length)];
        }
    }

    void output(DataOutputStream os) throws IOException {
        for (Map.Entry<State, Float[]> entry : qTable.entrySet()) {
            State state = entry.getKey();
            Float[] values = entry.getValue();
            os.writeChars(state.toString());
            os.writeChars(": ");
            os.writeChars(Arrays.deepToString(values));
            os.writeChars("\n");
        }
    }

    void print(PrintStream ps) {
        qTable.forEach((state,values) -> {
            ps.print(state);
            ps.print(": ");
            ps.println(Arrays.deepToString(values));
        });
    }
}
