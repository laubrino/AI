package cz.laubrino.ai.framework;

/**
 * @author tomas.laubr on 6.12.2019.
 */
public class LimitingEvictingQTable<A extends Enum<A>> extends QTable<A>{
    private final long maxSize;

    LimitingEvictingQTable(Class<A> actions, long maxSize) {
        super(actions);
        this.maxSize = maxSize;
    }

    @Override
    short[] getOrInit(State state) {
        if (qTable.size() < maxSize) {
            return super.getOrInit(state);
        } else {
            boolean removedSome = qTable.entrySet().removeIf(stateEntry -> {
                short[] values = stateEntry.getValue();
                for (short val : values) {
                    if (val != 0) {
                        return false;
                    }
                }
                return true;
            });

            if (removedSome) {
                return super.getOrInit(state);
            } else {
                return qTable.getOrDefault(state, new short[actions.length]);
            }
        }
    }

}
