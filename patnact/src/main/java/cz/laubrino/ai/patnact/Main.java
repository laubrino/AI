package cz.laubrino.ai.patnact;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author tomas.laubr on 29.10.2019.
 */
public class Main {
    private static final String PATH = "c:/x/patnact-qtable.txt";
    private static final int EPISODES = 1_000_000;
    private static final int STEPS = 1000;

    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        QTable qTable = new QTable();
        Agent agent = new Agent(qTable);
        List<Integer> illegalMoves = Collections.synchronizedList(new ArrayList<>());

        AtomicLong lastTimePrint = new AtomicLong(System.currentTimeMillis());

        AtomicInteger successCount = new AtomicInteger(0);

//        for (int n = 0; n < EPISODES; n++) {
//            Environment environment = new Environment();
//            environment.reset();
//            environment.shuffle(100);
//
//            if (System.currentTimeMillis() > (lastTimePrint + 1_000)) {
//                System.out.println("n = " + n);
//                System.out.println(environment);
//                lastTimePrint = System.currentTimeMillis();
//            }
//
//            for (int i=0;i<STEPS;i++) {
//                Action action = agent.chooseAction(environment.getState());
//
//                ActionResult actionResult = environment.step(action);
//
//                agent.qLearn(actionResult.getState(), actionResult.getReward(), action);
//
//                if (actionResult.isDone()) {
//                    if (actionResult.getReward() > 0) {
//                        successCount++;
//                    }
//                    break;
//                }
//            }
//        }
//
//        System.out.println("**************** Found " + successCount + " solutions out of " + EPISODES);
//        successCount = 0;

        AtomicInteger illegalMovesCount = new AtomicInteger(0);

        ExecutorService executorService;// = Executors.newFixedThreadPool(1);
        executorService = new ThreadPoolExecutor(5, 5, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>());

        for (int n = 0; n < EPISODES; n++) {
            Worker worker = new Worker(lastTimePrint, agent, illegalMoves, illegalMovesCount, successCount, n);
            executorService.submit(worker);
        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.DAYS);

        System.out.println("**************** Found " + successCount.get() + " solutions out of " + EPISODES);

        System.out.println("Illegal moves: " + Arrays.deepToString(illegalMoves.toArray()));

        System.out.print("Saving qTable to " + PATH + "....");
        qTable.print(new PrintStream(PATH));
        System.out.println("done.");
    }

    private static class Worker implements Runnable {
        private final AtomicLong lastTimePrint;
        private final Agent agent;
        private final List<Integer> illegalMoves;
        private final AtomicInteger illegalMovesCount;
        private final AtomicInteger successCount;

        public Worker(AtomicLong lastTimePrint, Agent agent, List<Integer> illegalMoves, AtomicInteger illegalMovesCount, AtomicInteger successCount, int n) {
            this.lastTimePrint = lastTimePrint;
            this.agent = agent;
            this.illegalMoves = illegalMoves;
            this.illegalMovesCount = illegalMovesCount;
            this.successCount = successCount;
            this.n = n;
        }

        private final int n;


        @Override
        public void run() {
            Environment environment = new Environment();
            environment.reset();
            environment.shuffle(1000);

            if (System.currentTimeMillis() > (lastTimePrint.get() + 1_000)) {
                System.out.println("Iteration n = " + n);
                System.out.println(environment);
                lastTimePrint.set(System.currentTimeMillis());
            }

            String s = null;

            for (int i=0;i<STEPS;i++) {
                Action action = agent.chooseAction(environment.getState());

                ActionResult actionResult = environment.step(action);

                agent.qLearn(actionResult.getState(), actionResult.getReward(), action, s);

                s = actionResult.getState();

                if (actionResult.isDone()) {
                    if (actionResult.getReward() < 0) {
                        illegalMovesCount.incrementAndGet();
                    }
                    if (actionResult.getReward() > 0) {
                        System.out.println("******************************************************************");
                        System.out.println("*********************  B I N G O  ********************************");
                        System.out.println("******************************************************************");
                        successCount.incrementAndGet();
                    }
                    break;
                }
            }

            if (n % 1000 == 0) {
                illegalMoves.add(illegalMovesCount.getAndSet(0));
            }

        }
    }
}
