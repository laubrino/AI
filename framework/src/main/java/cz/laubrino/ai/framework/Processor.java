package cz.laubrino.ai.framework;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Processor<A extends Enum<A>> {
    private final Agent agent;
    private final Environment environment;
    private final long episodes;
    private final int maxStepsPerEpisode;
    private final int testingEpisodes;          // number of plays in each testing step during learning (may be 0)

    public Processor(Agent agent, Environment environment, long episodes, int maxStepsPerEpisode, int testingEpisodes) {
        this.agent = agent;
        this.environment = environment;
        this.episodes = episodes;
        this.maxStepsPerEpisode = maxStepsPerEpisode;
        this.testingEpisodes = testingEpisodes;
    }

    public void process() throws IOException, InterruptedException {
        SamplingCounters samplingCounters = new SamplingCounters("Found solutions");
        List<Integer> numberOfMovesInEpisodes = Collections.synchronizedList(new ArrayList<>());

        List<Integer> sucessRate = new ArrayList<>();

        AtomicLong lastTimePrint = new AtomicLong(System.currentTimeMillis());
        AtomicInteger totalSuccessCount = new AtomicInteger(0);

        ExecutorService learningExecutorService = new ThreadPoolExecutor(5, 5, 0L, TimeUnit.MILLISECONDS,
                new ExecutorBlockingQueue<>(100));

        ExecutorService testingExecutorService = new ThreadPoolExecutor(5, 5, 0L, TimeUnit.MILLISECONDS,
                new ExecutorBlockingQueue<>(1000));

        for (int n = 0; n < episodes; n++) {
            Worker worker = new Worker(lastTimePrint, agent, totalSuccessCount, n, numberOfMovesInEpisodes, samplingCounters);
            learningExecutorService.submit(worker);

            if (n % (episodes/1000) == 0) {
                sucessRate.add(solvePuzzle(agent, testingExecutorService));
            }

        }

        learningExecutorService.shutdown();
        testingExecutorService.shutdown();
        learningExecutorService.awaitTermination(1, TimeUnit.HOURS);
        testingExecutorService.awaitTermination(1,TimeUnit.HOURS);

        System.out.println("**************** Found " + totalSuccessCount.get() + " solutions out of " + episodes);
        System.out.println(samplingCounters);
        System.out.println("Number of moves necessary to solve puzzle: " + Arrays.deepToString(numberOfMovesInEpisodes.toArray()));
        System.out.println("Sucess rate [%]: " + Arrays.deepToString(sucessRate.toArray()));
    }

    /**
     * Let an already trained agent to solve the puzzle
     * @return percent of solved puzzles
     */
    int solvePuzzle(Agent agent, ExecutorService testingExecutorService) {
        int puzzleSolvedCounter;
        List<Future<Boolean>> futures = new ArrayList<>(testingEpisodes);

        for (int n=0;n<testingEpisodes;n++) {
            TesterWorker<A> testerWorker = new TesterWorker(agent);
            futures.add(testingExecutorService.submit(testerWorker));
        }

        puzzleSolvedCounter = (int)futures.stream().filter(booleanFuture -> {
            try {
                return booleanFuture.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
                return false;
            }
        }).count();


        int percentage = (int)((float)puzzleSolvedCounter/(float)testingEpisodes * 100);
        return percentage;
    }


    private static class TesterWorker<E extends Enum<E>> implements Callable<Boolean> {
        private final Agent<E> agent;

        private TesterWorker(Agent agent) {
            this.agent = agent;
        }

        @Override
        public Boolean call() {
            Environment environment = new Environment();

            environment.reset();

            int i;
            for (i=0;i<1000 && !environment.isFinalStateAchieved();i++) {       // no more that 1000 steps per episode
                E action = agent.chooseAction(environment.getState());
                environment.step(action);
            }

            return environment.isFinalStateAchieved();
        }
    }


    private static class Worker implements Runnable {
        private final AtomicLong lastTimePrint;
        private final Agent agent;
        private final List<Integer> numberOfMovesInEpisodes;
        private final AtomicInteger totalSuccessCount;
        private final SamplingCounters samplingCounters;

        public Worker(AtomicLong lastTimePrint, Agent agent, AtomicInteger totalSuccessCount,
                      int n, List<Integer> numberOfMovesInEpisodes, SamplingCounters samplingCounters) {
            this.lastTimePrint = lastTimePrint;
            this.agent = agent;
            this.totalSuccessCount = totalSuccessCount;
            this.n = n;
            this.numberOfMovesInEpisodes = numberOfMovesInEpisodes;
            this.samplingCounters = samplingCounters;
        }

        private final int n;


        @Override
        public void run() {
            try {
                Environment environment = new Environment();
                environment.reset();

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
                        if (actionResult.getReward() > 0f) {
                            totalSuccessCount.incrementAndGet();
                            samplingCounters.incrementAndGet("Found solutions");
                        }
                        break;
                    }
                }

                if (n % (EPISODES/1000) == 0) {
                    samplingCounters.sample();
                    numberOfMovesInEpisodes.add(i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}