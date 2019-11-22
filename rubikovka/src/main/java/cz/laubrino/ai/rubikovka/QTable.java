package cz.laubrino.ai.rubikovka;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Stream;

public class QTable<N extends Number & Comparable> {
    private static final int ACTIONS_COUNT = Action.VALUES.length;

    private Map<State, N[]> qTable = Collections.synchronizedMap(new HashMap<>());
    private Random randoms = new Random();

    static <N> N[] newArray(int length, N... array)
    {
        return (N[])Array.newInstance(array.getClass().getComponentType(), length);
    }

    void set(State state, Action action, N value) {
        N[] values = qTable.computeIfAbsent(state, k -> {
            N[] vals = newArray(ACTIONS_COUNT);
            Arrays.fill(vals, 0);
            return vals;
        });
        values[action.ordinal()] = value;
    }

    N get(State state, Action action) {
        N[] values = qTable.computeIfAbsent(state, k -> {
            N[] vals = newArray(ACTIONS_COUNT);
            Arrays.fill(vals, 0);
            return vals;
        });
        return values[action.ordinal()];
    }

    N max(State state) {
        N[] values = qTable.computeIfAbsent(state, k -> {
            N[] vals = newArray(ACTIONS_COUNT);
            Arrays.fill(vals, 0);
            return vals;
        });

        return Arrays.stream(values).max(N::compareTo).orElseThrow(() -> new RuntimeException("There has to be one..."));
    }

    Action maxAction(State state) {
        N[] values = qTable.computeIfAbsent(state, k -> {
            N[] vals = newArray(ACTIONS_COUNT);
            Arrays.fill(vals, 0);
            return vals;
        });

        N maxValue = Stream.of(values).max(N::compareTo).get();

        List<Action> maxActions = new ArrayList<>();
        for (int i=0;i<Action.VALUES.length;i++) {
            if (values[i] == maxValue) {
                maxActions.add(Action.VALUES[i]);
            }
        }

        if(!maxActions.isEmpty()) {
            return maxActions.stream().skip(randoms.nextInt(maxActions.size())).findFirst().get();
        } else {
            return Action.VALUES[randoms.nextInt(Action.VALUES.length)];
        }
    }

    void output(DataOutputStream os) throws IOException {
        for (Map.Entry<State, N[]> entry : qTable.entrySet()) {
            State state = entry.getKey();
            N[] values = entry.getValue();
            os.writeChars(state.toString());
            os.writeChars(": ");
            os.writeChars(Arrays.deepToString(values));
            os.writeChars("\n");
        }
    }

    void print(PrintStream ps) {
        this.print(ps, Long.MAX_VALUE);
        qTable.forEach((state,values) -> {
            ps.print(state);
            ps.print(": ");
            ps.println(Arrays.deepToString(values));
        });
    }

    void print(PrintStream ps, long maxRecords) {
        for (Map.Entry<State, N[]> entry : qTable.entrySet()) {
            if (maxRecords-- <= 0) {
                break;
            }

            State state = entry.getKey();
            N[] values = entry.getValue();
            ps.print(state);
            ps.print(": ");
            ps.println(Arrays.deepToString(values));
        }
    }

}
