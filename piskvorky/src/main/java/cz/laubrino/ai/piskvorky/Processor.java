package cz.laubrino.ai.piskvorky;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author tomas.laubr on 10.10.2019.
 */
public class Processor {
    static float REWARD = 1;
    static Cache<String, Optional<Field>> isPiskvorekCache = CacheBuilder.newBuilder().maximumSize(1_000_000).build();
    // kazdy stav ma available actions a kazda ta available action bude mit nejakou Q value
    static Cache<String, Map<Action, Float>> qTableX = CacheBuilder.newBuilder().maximumSize(10_000_000).build();
    static Cache<String, Map<Action, Float>> qTableO = CacheBuilder.newBuilder().maximumSize(10_000_000).build();

    /**
     * Check if board is safe.
     * Check if there is a line of {@code length} fields. Either row, col or diag.
     * @param state
     * @return
     */
    public static Optional<Field> isPiskvorek(State state) {
        try {
            return isPiskvorekCache.get(state.toString(), () -> {
                for (int i=0;i<state.board.size();i++) {
                    for (int j=0;j<state.board.size();j++) {
                        if (state.board.get(i,j) != Field.NIC) {
                            if (checkPiskvorek(state.board, i, j, state.length)) {
                                return Optional.of(state.board.get(i,j));
                            }
                        }
                    }
                }

                return Optional.empty();
            });
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    // check row, col and diagonals if there is a piskvorek
    private static boolean checkPiskvorek(Board board, int x, int y, int length) {
        Field f = board.get(x, y);

        int counter = 0;
        for (int i=x;i<board.size();i++) {
            if (board.get(i,y).equals(f)) {
                counter++;
            } else break;
        }
        if (counter >= length) {
            return true;
        }


        counter = 0;
        for (int i=y;i<board.size();i++) {
            if (board.get(x,i).equals(f)) {
                counter++;
            } else break;
        }
        if (counter >= length) {
            return true;
        }

        // diagonala \
        counter = 0;
        for (int i=0;i<length && (x+i < board.size()) && (y+i < board.size());i++) {
            if (board.get(x+i,y+i).equals(f)) {
                counter++;
            } else break;
        }
        if (counter >= length) {
            return true;
        }

        // diagonala /
        counter = 0;
        for (int i=0;i<length && (x+i < board.size()) && (y-i > 0);i++) {
            if (board.get(x+i,y-i).equals(f)) {
                counter++;
            } else break;
        }
        if (counter >= length) {
            return true;
        }

        return false;
    }

    /**
     * Choose best action based on qValue
     * @param state
     * @return
     */
    public static Optional<Action> chooseAction(State state, Cache<String, Map<Action, Float>> qTable) {
        System.out.println("choose action for state: " +state);
        try {
            Map<Action, Float> actionQValues = qTable.get(state.toString(), () -> state.getAvailableActions().stream()
                    .collect(Collectors.toMap(Function.identity(), a -> 0f)));
            System.out.println("test: " + qTable.getIfPresent(state.toString()));
            Optional<Action> action = actionQValues.entrySet().stream()
                    .max((o1, o2) -> Float.compare(o1.getValue(), o2.getValue()))
                    .map(Map.Entry::getKey);
            return action;
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public static ActionResult step(State state, Action action, Field field) {
        Optional<Field> piskvorek = isPiskvorek(state);
        if (piskvorek.isPresent()) {
            if (piskvorek.get() != field) {
                return new ActionResult(state, -REWARD, true, "we lost");
            } else {
                return new ActionResult(state, REWARD, true, "vyhrali jsme");
            }
        }

        state.board.put(action.position.x, action.position.y, field);
        piskvorek = isPiskvorek(state);
        if (piskvorek.isPresent()) {
            return new ActionResult(state, REWARD, true, "vyhrali jsme");
        }

        return new ActionResult(state, 0, false, "hra pokracuje");
    }

    public static void main(String[] args) {
        float alpha = 0.4f;     // learning rate
        float gamma = 0.9f;

        Board b = new Board(5);


        State prevObserv = null;
        Action prevActionX = null;
        Action prevActionO = null;

        State observ = new State(b, 3);

        Action actionX = chooseAction(observ, qTableX).get();

        /**
         * maximum steps possible is board_size * board_size
         */
        for (int i=0; i < observ.board.size()*observ.board.size(); i++) {
            System.out.println(observ.board.toString());

            ActionResult actionResult = null;



            actionResult = step(observ, actionX, Field.X);
            observ = actionResult.state;

            actionX = chooseAction(observ, qTableX).get();

            if (prevObserv != null) {
                System.out.println("get from Q table for state: " + prevObserv);
                Map<Action, Float> actionsQValues = qTableX.getIfPresent(prevObserv.toString());
                System.out.println("prevAction: " + prevActionX);
                float qOld = actionsQValues.get(prevActionX);
                float qNew = qOld;

                if(actionResult.done) {
                    qNew += alpha * (actionResult.reward - qOld);
                } else {
                    System.out.println("get from Q table for state: " + observ);
                    Map<Action, Float> actionsQValues2 = qTableX.getIfPresent(observ.toString());
                    qNew += alpha * (actionResult.reward + gamma * actionsQValues2.get(actionX) - qOld);
                }

                actionsQValues.put(prevActionX, qNew);
            }

            prevObserv = observ;
            prevActionX = actionX;




//            if ((i&1) == 0) {
//
//            } else {
////                Action actionO = chooseAction(observ, qTableO).get();
////                actionResult = step(observ, actionO, Field.O);
//            }
//
//            if (actionResult != null && actionResult.done) {
//                System.out.println("**************  END ******************");
//                break;
//            }

            System.out.println(actionResult);
        }

        printCache(qTableX);
        printCache(qTableO);

    }

    public static void printCache(Cache<String, Map<Action, Float>> cache) {
        cache.asMap().entrySet().stream()
                .map(s -> s.getKey() + ": " + s.getValue().entrySet().stream().map(x -> x.getKey() + "(" + x.getValue() + ")").collect(Collectors.joining()))
                .forEach(System.out::println);
    }

}
