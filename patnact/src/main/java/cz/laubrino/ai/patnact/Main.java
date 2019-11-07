package cz.laubrino.ai.patnact;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author tomas.laubr on 29.10.2019.
 */
public class Main {
    private static final long EPISODES = 5_000_000;
    private static final int MAX_STEPS_PER_EPISODE = 1000;
    private static final int SHUFFLE_STEPS = 1000;
    private static final String PATH = "c:/x/patnact-qtable-"+Environment.BOARD_SIZE+"x"+Environment.BOARD_SIZE+"-" + EPISODES+".zip";
    private static final Random RANDOMS = new Random();


    public static void main(String[] args) throws IOException, InterruptedException {
        QTable qTable = new QTable();
        Agent agent = new Agent(qTable);
        List<Integer> illegalMoves = Collections.synchronizedList(new ArrayList<>());
        List<Integer> successed = Collections.synchronizedList(new ArrayList<>());
        List<Integer> numberOfMovesInEpisodes = Collections.synchronizedList(new ArrayList<>());
        List<Integer> sucessRate = new ArrayList<>();

        AtomicLong lastTimePrint = new AtomicLong(System.currentTimeMillis());
        AtomicInteger totalSuccessCount = new AtomicInteger(0);
        AtomicInteger illegalMovesCount = new AtomicInteger(0);
        AtomicInteger successCount = new AtomicInteger(0);

        ExecutorService executorService;// = Executors.newFixedThreadPool(1);
        executorService = new ThreadPoolExecutor(5, 5, 0L, TimeUnit.MILLISECONDS,
                new ExecutorBlockingQueue<>(100));

        for (int n = 0; n < EPISODES; n++) {
            Worker worker = new Worker(lastTimePrint, agent, illegalMoves, illegalMovesCount, totalSuccessCount, n, successed, successCount, numberOfMovesInEpisodes);
            executorService.submit(worker);

            if (n % (EPISODES/1000) == 0) {
                sucessRate.add(solvePuzzle(agent));
            }

        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.DAYS);

        System.out.println("**************** Found " + totalSuccessCount.get() + " solutions out of " + EPISODES);

        System.out.println("Illegal moves: " + Arrays.deepToString(illegalMoves.toArray()));
        System.out.println("Found solutions: " + Arrays.deepToString(successed.toArray()));
        System.out.println("Number of moves necessary to solve puzzle: " + Arrays.deepToString(numberOfMovesInEpisodes.toArray()));
        System.out.println("Sucess rate [%]: " + Arrays.deepToString(sucessRate.toArray()));

        saveToDisk(qTable);

        agent.setEpsilon(0);
        solvePuzzle(agent);
    }

    /**
     * Let an already trained agent to solve the puzzle
     * @return percent of solved puzzles
     */
    static int solvePuzzle(Agent agent) {
        int puzzleSolvedCounter = 0;
        int epizodes = 1_000;

        for (int n=0;n<epizodes;n++) {
            Environment environment = new Environment();
            environment.shuffle(RANDOMS.nextInt(800) + 200);        // shuffle 200-1000

            int i;
            for (i=0;i<1000 && !environment.isFinalStateAchieved();i++) {       // no more that 1000 steps per episode
                Action action = agent.chooseAction(environment.getState(), 0);
                environment.step(action);
            }

            if (environment.isFinalStateAchieved()) {
                puzzleSolvedCounter++;
            }
        }

        int percentage = (int)((float)puzzleSolvedCounter/(float)epizodes * 100);
        return percentage;
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
        private final List<Integer> numberOfMovesInEpisodes;
        private final AtomicInteger illegalMovesCount;
        private final AtomicInteger totalSuccessCount;
        private final AtomicInteger successCount;

        public Worker(AtomicLong lastTimePrint, Agent agent, List<Integer> illegalMoves, AtomicInteger illegalMovesCount, AtomicInteger totalSuccessCount,
                      int n, List<Integer> successes, AtomicInteger successCount, List<Integer> numberOfMovesInEpisodes) {
            this.lastTimePrint = lastTimePrint;
            this.agent = agent;
            this.illegalMoves = illegalMoves;
            this.illegalMovesCount = illegalMovesCount;
            this.totalSuccessCount = totalSuccessCount;
            this.n = n;
            this.successes = successes;
            this.successCount = successCount;
            this.numberOfMovesInEpisodes = numberOfMovesInEpisodes;
        }

        private final int n;


        @Override
        public void run() {
            Environment environment = new Environment();
            environment.reset();
            environment.shuffle(SHUFFLE_STEPS);

            if (System.currentTimeMillis() > (lastTimePrint.get() + 1_000)) {
                System.out.println("Iteration n = " + n);
                System.out.println("Epsilon = " + agent.getEpsilon());
                System.out.println(environment);
                lastTimePrint.set(System.currentTimeMillis());
            }

            State s = null;

            int i;
            for (i = 0; i< MAX_STEPS_PER_EPISODE; i++) {
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

            if (n % (EPISODES/1000) == 0) {
                illegalMoves.add(illegalMovesCount.getAndSet(0));
                successes.add(successCount.getAndSet(0));
                numberOfMovesInEpisodes.add(i);
            }

        }
    }
}
