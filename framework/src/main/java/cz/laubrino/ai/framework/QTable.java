package cz.laubrino.ai.framework;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

/**
 * Parametrized by an actions enum
 * @author tomas.laubr on 26.11.2019.
 */
class QTable<A extends Enum<A>> {
    private final A[] actions;
    private Map<State, short[]> qTable = Collections.synchronizedMap(new HashMap<>());
    private Random randoms = new Random();

    QTable(Class<A> actions) {
        this.actions = actions.getEnumConstants();
    }

    void set(State state, A action, short value) {
        short[] values = getOrInit(state);
        values[action.ordinal()] = value;
    }

    private short[] getOrInit(State state) {
        return qTable.computeIfAbsent(state, k -> {
            short[] vals = new short[actions.length];
            Arrays.fill(vals, (short)0);
            return vals;
        });
    }



    short get(State state, A action) {
        short[] values = getOrInit(state);
        return values[action.ordinal()];
    }

    private short maxValue(short[] values) {
        short maxValue = Short.MIN_VALUE;

        for (short value : values) {
            if (value > maxValue) {
                maxValue = value;
            }
        }

        return maxValue;
    }

    short max(State state) {
        short[] values = getOrInit(state);
        return maxValue(values);
    }

    A maxAction(State state) {
        short[] values = getOrInit(state);

        short maxValue = maxValue(values);

        List<A> maxActions = new LinkedList<>();
        for (int i = 0; i<this.actions.length; i++) {
            if (values[i] == maxValue) {
                maxActions.add(this.actions[i]);
            }
        }

        if(!maxActions.isEmpty()) {
            int randomActionIndex = randoms.nextInt(maxActions.size());
            return maxActions.get(randomActionIndex);
        } else {
            return this.actions[randoms.nextInt(this.actions.length)];
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
