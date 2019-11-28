package cz.laubrino.ai.framework;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Processor<A extends Enum<A>> {
    private final Agent<A> agent;
    private final EnvironmentFactory<A> environmentFactory;
    private final long episodes;
    private final int maxStepsPerEpisode;
    private final int testingEpisodes;          // number of plays in each testing step during learning (may be 0)

    public Processor(Agent<A> agent, EnvironmentFactory<A> environmentFactory, long episodes, int maxStepsPerEpisode, int testingEpisodes) {
        this.agent = agent;
        this.environmentFactory = environmentFactory;
        this.episodes = episodes;
        this.maxStepsPerEpisode = maxStepsPerEpisode;
        this.testingEpisodes = testingEpisodes;
    }

    /**
     * This queue blocks on offer() => used in feeding executor service with workers
     */
    private static class ExecutorBlockingQueue extends LinkedBlockingQueue<Runnable>
    {
        ExecutorBlockingQueue()
        {
            super(1000);
        }

        @Override
        public boolean offer(Runnable runnable)
        {
            // turn offer() and add() into a blocking calls (unless interrupted)
            try {
                put(runnable);
                return true;
            } catch(InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
            return false;
        }
    }

    public void process() throws IOException, InterruptedException {
        SamplingCounters samplingCounters = new SamplingCounters("Found solutions");
        List<Integer> numberOfMovesInEpisodes = Collections.synchronizedList(new ArrayList<>());

        List<Integer> sucessRate = new ArrayList<>();

        AtomicLong lastTimePrint = new AtomicLong(System.currentTimeMillis());
        AtomicInteger totalSuccessCount = new AtomicInteger(0);

        int cores = Runtime.getRuntime().availableProcessors();

        ExecutorService learningExecutorService = new ThreadPoolExecutor(cores+1, cores+1, 0L, TimeUnit.MILLISECONDS,
                new ExecutorBlockingQueue());

        ExecutorService testingExecutorService = new ThreadPoolExecutor(cores+1, cores+1, 0L, TimeUnit.MILLISECONDS,
                new ExecutorBlockingQueue());

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
    int solvePuzzle(Agent<A> agent, ExecutorService testingExecutorService) {
        int puzzleSolvedCounter;
        List<Future<Boolean>> futures = new ArrayList<>(testingEpisodes);

        for (int n=0;n<testingEpisodes;n++) {
            TesterWorker testerWorker = new TesterWorker(agent);
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


    private class TesterWorker implements Callable<Boolean> {
        private final Agent<A> agent;

        private TesterWorker(Agent<A> agent) {
            this.agent = agent;
        }

        @Override
        public Boolean call() {
            Environment<A> environment = environmentFactory.getInstance();
            environment.reset();

            int i;
            for (i=0;i<1000 && !environment.isFinalStateAchieved();i++) {       // no more that 1000 steps per episode
                A action = agent.chooseAction(environment.getState());
                environment.step(action);
            }

            return environment.isFinalStateAchieved();
        }
    }


    private class Worker implements Runnable {
        private final AtomicLong lastTimePrint;
        private final Agent<A> agent;
        private final List<Integer> numberOfMovesInEpisodes;
        private final AtomicInteger totalSuccessCount;
        private final SamplingCounters samplingCounters;

        public Worker(AtomicLong lastTimePrint, Agent<A> agent, AtomicInteger totalSuccessCount,
                      int episode, List<Integer> numberOfMovesInEpisodes, SamplingCounters samplingCounters) {
            this.lastTimePrint = lastTimePrint;
            this.agent = agent;
            this.totalSuccessCount = totalSuccessCount;
            this.episode = episode;
            this.numberOfMovesInEpisodes = numberOfMovesInEpisodes;
            this.samplingCounters = samplingCounters;
        }

        private final int episode;


        @Override
        public void run() {
            try {
                Environment<A> environment = environmentFactory.getInstance();
                environment.reset();

                if (System.currentTimeMillis() > (lastTimePrint.get() + 1_000)) {
                    System.out.println("Episode = " + episode);
                    System.out.println("Epsilon = " + agent.getEpsilon());
                    System.out.println(environment);
                    lastTimePrint.set(System.currentTimeMillis());
                }

                State s = null;

                int i;
                for (i = 0; i< maxStepsPerEpisode; i++) {
                    A action = agent.chooseAction(environment.getState());

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

                if (episode % (episodes/1000) == 0) {
                    samplingCounters.sample();
                    numberOfMovesInEpisodes.add(i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
