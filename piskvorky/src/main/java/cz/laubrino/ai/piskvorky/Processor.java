package cz.laubrino.ai.piskvorky;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author tomas.laubr on 10.10.2019.
 */
public class Processor {
    static float REWARD = 10;
    static Cache<String, Optional<Field>> isPiskvorekCache = CacheBuilder.newBuilder().maximumSize(1_000_000).build();
    // kazdy stav ma available actions a kazda ta available action bude mit nejakou Q value
    static Cache<String, Map<Action, Float>> qTable = CacheBuilder.newBuilder().maximumSize(10_000_000).build();

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
    public static Optional<Action> chooseAction(State state, Cache<String, Map<Action, Float>> qTable, double epsilon) {
        try {
            Map<Action, Float> actionQValues = qTable
                    .get(state.toString(), () -> state.getAvailableActions().stream()
                            .collect(Collectors.toMap(Function.identity(), a -> 0f))
                    );

            Optional<Action> action;
            if (Math.random() < epsilon) {
                // choose random
                action = actionQValues.entrySet().stream()
                        .skip(new Random().nextInt(actionQValues.size()))
                        .map(Map.Entry::getKey)
                        .findAny();
            } else {
                // choose best
                action = actionQValues.entrySet().stream()
                        .max((o1, o2) -> Float.compare(o1.getValue(), o2.getValue()))
                        .map(Map.Entry::getKey);
            }

            return action;

        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public static ActionResult step(State state, Action action) {
        State newState = new State(state.board.clone(), state.length,
                state.field.flip());

        newState.board.put(action.position.x, action.position.y, state.field);

        Optional<Field> piskvorek = isPiskvorek(newState);
        if (piskvorek.isPresent()) {
            if (piskvorek.get() == state.field) {
                return new ActionResult(newState, REWARD, true, "vyhrali " + piskvorek.get());
            } else {
                return new ActionResult(newState, -REWARD, true, "prohrali " +  piskvorek.get());
            }
        }

        return new ActionResult(newState, 0, false, "hra pokracuje");
    }

    public static void main(String[] args) throws FileNotFoundException {
        float alpha = 0.4f;     // learning rate
        float gamma = 0.9f;
        double epsilon = 0.8;

        Field field = Field.X;

        for (int j=0;j<100000;j++) {
            Board b = new Board(3);

            State state = new State(b, 2, field);
            State prevState = null;
            Action prevAction = null;

            for (int i=0;i<25;i++) {
                // System.out.println(state.board);

                Action action = chooseAction(state, qTable, epsilon).get();
                ActionResult actionResult = step(state, action);

                if (prevState != null) {
                    // update Q value for previous [prevState, prevAction]
                    float qOld = qTable.getIfPresent(prevState.toString()).get(prevAction);
                    float qNew = qOld;

                    if (actionResult.done) {
                        qNew += alpha * (actionResult.reward - qOld);
                    } else {
                        qNew += alpha * (actionResult.reward + gamma * qTable.getIfPresent(state.toString()).get(action) - qOld);
                    }

                    qTable.getIfPresent(prevState.toString()).put(prevAction, qNew);
                }

                prevState = state;
                prevAction = action;
                state = actionResult.state;

                if (actionResult.done) {
                    System.out.println("***************** END ***************");
                    System.out.println(actionResult.info);
                    System.out.println(actionResult.state.board);
                    break;
                }
            }

            field = field.flip();       // zacina opacny hrac
        }

        outputQTable(qTable, new PrintStream(new File("c:/x/qTable3.txt")));
//        outputQTable(qTable, System.out);
    }

    public static void outputQTable(Cache<String, Map<Action, Float>> cache, PrintStream ps) {
        cache.asMap().entrySet().stream()
                .map(s -> s.getKey() + ": " + s.getValue().entrySet().stream().map(x -> x.getKey() + "(" + x.getValue() + ")").collect(Collectors.joining()))
                .forEach(ps::println);
    }

}
