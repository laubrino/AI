package cz.laubrino.ai.patnact;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author tomas.laubr on 29.10.2019.
 */
public class Main {
    private static final String PATH = "c:/x/patnact-qtable.zip";
    private static final long EPISODES = 5_000_000;
    private static final int MAX_STEPS_PER_EPISODE = 1000;
    private static final int SHUFFLE_STEPS = 100;

    public static void main(String[] args) throws IOException, InterruptedException {
        QTable qTable = new QTable();
        Agent agent = new Agent(qTable);
        List<Integer> illegalMoves = Collections.synchronizedList(new ArrayList<>());
        List<Integer> successed = Collections.synchronizedList(new ArrayList<>());

        AtomicLong lastTimePrint = new AtomicLong(System.currentTimeMillis());
        AtomicInteger totalSuccessCount = new AtomicInteger(0);
        AtomicInteger illegalMovesCount = new AtomicInteger(0);
        AtomicInteger successCount = new AtomicInteger(0);

        ExecutorService executorService;// = Executors.newFixedThreadPool(1);
        executorService = new ThreadPoolExecutor(5, 5, 0L, TimeUnit.MILLISECONDS,
                new ExecutorBlockingQueue<>(1000));

        for (int n = 0; n < EPISODES; n++) {
            Worker worker = new Worker(lastTimePrint, agent, illegalMoves, illegalMovesCount, totalSuccessCount, n, successed, successCount);
            executorService.submit(worker);
        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.DAYS);

        System.out.println("**************** Found " + totalSuccessCount.get() + " solutions out of " + EPISODES);

        System.out.println("Illegal moves: " + Arrays.deepToString(illegalMoves.toArray()));
        System.out.println("Found solutions: " + Arrays.deepToString(successed.toArray()));

        //saveToDisk(qTable);

    }

    static void saveToDisk(QTable qTable) throws IOException {
        System.out.print("Saving qTable to " + PATH + "....");
        ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(PATH)));
        zipOutputStream.putNextEntry(new ZipEntry("table.txt"));
        PrintStream printStream = new PrintStream((zipOutputStream));
        qTable.print(printStream);
        zipOutputStream.closeEntry();
        printStream.close();
        System.out.println("done.");
    }

    private static class Worker implements Runnable {
        private final AtomicLong lastTimePrint;
        private final Agent agent;
        private final List<Integer> illegalMoves;
        private final List<Integer> successes;
        private final AtomicInteger illegalMovesCount;
        private final AtomicInteger totalSuccessCount;
        private final AtomicInteger successCount;

        public Worker(AtomicLong lastTimePrint, Agent agent, List<Integer> illegalMoves, AtomicInteger illegalMovesCount, AtomicInteger totalSuccessCount, int n, List<Integer> successes, AtomicInteger successCount) {
            this.lastTimePrint = lastTimePrint;
            this.agent = agent;
            this.illegalMoves = illegalMoves;
            this.illegalMovesCount = illegalMovesCount;
            this.totalSuccessCount = totalSuccessCount;
            this.n = n;
            this.successes = successes;
            this.successCount = successCount;
        }

        private final int n;


        @Override
        public void run() {
            Environment environment = new Environment();
            environment.reset();
            environment.shuffle(SHUFFLE_STEPS);

            if (System.currentTimeMillis() > (lastTimePrint.get() + 1_000)) {
                System.out.println("Iteration n = " + n);
                System.out.println(environment);
                lastTimePrint.set(System.currentTimeMillis());
            }

            State s = null;

            for (long i = 0; i< MAX_STEPS_PER_EPISODE; i++) {
                Action action = agent.chooseAction(environment.getState());

                ActionResult actionResult = environment.step(action);

                agent.qLearn(actionResult.getState(), actionResult.getReward(), action, s);

                s = actionResult.getState();

                if (actionResult.isDone()) {
                    if (actionResult.getReward() < 0f) {
                        illegalMovesCount.incrementAndGet();
                    }
                    if (actionResult.getReward() > 0f) {
//                        System.out.println(
//                                "******************************************************************\n" +
//                                "*********************  B I N G O  ********************************\n" +
//                                environment+"\n"+
//                                "******************************************************************");
                        totalSuccessCount.incrementAndGet();
                        successCount.incrementAndGet();
                    }
                    break;
                }
            }

            if (n % 1000 == 0) {
                illegalMoves.add(illegalMovesCount.getAndSet(0));
                successes.add(successCount.getAndSet(0));
            }

        }
    }
}
