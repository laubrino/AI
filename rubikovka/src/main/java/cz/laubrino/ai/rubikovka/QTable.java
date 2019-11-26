package cz.laubrino.ai.rubikovka;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;
import java.util.stream.IntStream;

/**
 * @author tomas.laubr on 29.10.2019.
 */
public class QTable {
    private static final int ACTIONS_COUNT = Action.VALUES.length;

    private Map<State, short[]> qTable = Collections.synchronizedMap(new HashMap<>());
    private Random randoms = new Random();

    void set(State state, Action action, float value) {
        short[] values = getOrInit(state);
        values[action.ordinal()] = (short)value;
    }

    private short[] getOrInit(State state) {
        return qTable.computeIfAbsent(state, k -> {
            short[] vals = new short[ACTIONS_COUNT];
            Arrays.fill(vals, (short)0);
            return vals;
        });
    }



    short get(State state, Action action) {
        short[] values = getOrInit(state);
        return values[action.ordinal()];
    }

    private short maxValue(short[] values) {
        short maxValue = Short.MIN_VALUE;

        for (int i=0;i<values.length;i++) {
            if (values[i] > maxValue) {
                maxValue = values[i];
            }
        }

        return maxValue;
    }

    short max(State state) {
        short[] values = getOrInit(state);
        return maxValue(values);
    }

    Action maxAction(State state) {
        short[] values = getOrInit(state);

        short maxValue = maxValue(values);

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
        for (Map.Entry<State, short[]> entry : qTable.entrySet()) {
            State state = entry.getKey();
            short[] values = entry.getValue();
            os.writeChars(state.toString());
            os.writeChars(": ");
            os.writeChars(Arrays.toString(values));
            os.writeChars("\n");
        }
    }

    void print(PrintStream ps) {
        this.print(ps, Long.MAX_VALUE);
    }

    void print(PrintStream ps, long maxRecords) {
        for (Map.Entry<State, short[]> entry : qTable.entrySet()) {
            if (maxRecords-- <= 0) {
                break;
            }

            State state = entry.getKey();
            short[] values = entry.getValue();
            ps.print(state);
            ps.print(": ");
            ps.println(Arrays.toString(values));
        }
    }
}
