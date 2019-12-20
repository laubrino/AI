package cz.laubrino.ai.framework;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Processor<A extends Enum<A>> implements AgentObserver {
    private final Agent<A> agent;
    private final EnvironmentFactory<A> environmentFactory;
    private final long episodes;
    private final int maxStepsPerEpisode;
    private final int testingEpisodes;          // number of plays in each testing step during learning (may be 0)
    private final float testingFrequency;

    private final Notifier notifier;

    /**
     *
     * @param agentConfiguration
     * @param environmentFactory
     * @param episodes
     * @param maxStepsPerEpisode
     * @param testingFrequency e.g. 0.001 means test 1000x per whole process
     * @param testingEpisodes number of testing episodes in each batch
     * @param actions actions enum class
     */
    public Processor(AgentConfiguration agentConfiguration, EnvironmentFactory<A> environmentFactory, long episodes,
                     int maxStepsPerEpisode, float testingFrequency, int testingEpisodes, Class<A> actions) {
        this.agent = new Agent<>(agentConfiguration, actions);
        this.environmentFactory = environmentFactory;
        this.episodes = episodes;
        this.maxStepsPerEpisode = maxStepsPerEpisode;
        this.testingEpisodes = testingEpisodes;
        this.testingFrequency = testingFrequency;

        notifier = new Notifier();
        this.agent.addObserver(this);
    }

    @Override
    public void qValueChanged(float deltaQ, int deltaQInPercent) {
        notifier.notifyQValueChanged(deltaQ, deltaQInPercent);
    }

    public void addObserver(Observer observer) {
        notifier.addObserver(observer);
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


    boolean shouldTest(int episode) {
        return (episode % ((int)(episodes*testingFrequency)) == 0);
    }

    public void process() throws InterruptedException {
        int cores = Runtime.getRuntime().availableProcessors();

        ExecutorService learningExecutorService = new ThreadPoolExecutor(cores+1, cores+1, 0L, TimeUnit.MILLISECONDS,
                new ExecutorBlockingQueue());

        ExecutorService testingExecutorService = new ThreadPoolExecutor(cores+1, cores+1, 0L, TimeUnit.MILLISECONDS,
                new ExecutorBlockingQueue());

        notifier.notifyProcessStart(new Observer.StartConfiguration(episodes));

        for (int episode = 0; episode < episodes; episode++) {
            Worker worker = new Worker(agent, episode);
            learningExecutorService.submit(worker);

            if (shouldTest(episode)) {
                testAgent(agent, testingExecutorService);
            }
        }

        learningExecutorService.shutdown();
        testingExecutorService.shutdown();
        learningExecutorService.awaitTermination(1, TimeUnit.HOURS);
        testingExecutorService.awaitTermination(1,TimeUnit.HOURS);

        notifier.notifyProcessEnd();
    }

    /**
     * Let an already trained agent to solve the puzzle
     * @return percent of solved puzzles
     */
    void testAgent(Agent<A> agent, ExecutorService testingExecutorService) {
        List<Future<ActionResult>> futures = new ArrayList<>(testingEpisodes);

        TesterWorker testerWorker = new TesterWorker(agent);
        int n;
        for (n=0;n<testingEpisodes;n++) {
            futures.add(testingExecutorService.submit(testerWorker));
        }

        float totalReward = 0;
        float minReward = Float.MAX_VALUE;
        float maxReward = Float.MIN_VALUE;
        int successfulTests = 0;
        for (Future<ActionResult> future : futures) {
            try {
                ActionResult actionResult = future.get();
                if (actionResult.isDone()) {
                    successfulTests++;
                }
                float reward = actionResult.getReward();
                totalReward += reward;
                if (reward < minReward) {
                    minReward = reward;
                }
                if (reward > maxReward) {
                    maxReward = reward;
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }

        notifier.notifyTestingBatchFinished(successfulTests, n, minReward, maxReward, totalReward/futures.size());
    }

    private class TesterWorker implements Callable<ActionResult> {
        private final Agent<A> agent;

        private TesterWorker(Agent<A> agent) {
            this.agent = agent;
        }

        @Override
        public ActionResult call() {     // no need to try-catch. Will be eventually reported on Future.get()
            Environment<A> environment = environmentFactory.getInstance();
            environment.reset();

            int step;
            ActionResult actionResult = null;
            for (step=0; step < maxStepsPerEpisode; step++) {
                A action = agent.chooseAction(environment.getState());
                actionResult = environment.step(action);
                if (actionResult.isDone()) {
                    break;
                }
            }

            notifier.notifyTestingEpisodeFinished(actionResult, step);

            return actionResult;
        }
    }


    private class Worker implements Runnable {
        private final Agent<A> agent;

        public Worker(Agent<A> agent, int episode) {
            this.agent = agent;
            this.episode = episode;
        }

        private final int episode;


        @Override
        public void run() {
            try {
                Environment<A> environment = environmentFactory.getInstance();
                environment.reset();

                State s = null;

                int i;
                for (i = 0; i< maxStepsPerEpisode; i++) {
                    A action = agent.chooseAction(environment.getState());

                    ActionResult actionResult = environment.step(action);

                    agent.qLearn(actionResult.getState(), actionResult.getReward(), action, s);

                    s = actionResult.getState();

                    if (actionResult.isDone()) {
                        break;
                    }
                }

                notifier.notifyLearningEpisodeFinished(episode, i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



}
