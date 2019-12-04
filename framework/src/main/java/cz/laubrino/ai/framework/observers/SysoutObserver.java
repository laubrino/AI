package cz.laubrino.ai.framework.observers;

import cz.laubrino.ai.framework.Observer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author tomas.laubr on 2.12.2019.
 */
public class SysoutObserver implements Observer {
    private volatile long episode;
    private Averaging testingPercent;
    private Averaging testingSteps;
    private long previousEpisode = 0;
    private ScheduledExecutorService scheduledExecutorService;

    public SysoutObserver() {
        testingPercent = new Averaging();
        testingSteps = new Averaging();
    }

    @Override
    public void qValueChanged(float deltaQ, int deltaQInPercent) {

    }

    @Override
    public void learningEpisodeFinished(long episode, long steps) {
        this.episode = episode;
    }

    @Override
    public void testingEpisodeFinished(boolean success, long steps) {
        testingSteps.add(steps);
    }

    @Override
    public void testingBatchFinished(int successEpisodes, int allEpisodes) {
        testingPercent.add((float)successEpisodes/allEpisodes*100);
    }

    @Override
    public void end() {
        try {
            scheduledExecutorService.shutdown();
            scheduledExecutorService.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        sample();               // flush everything
    }

    @Override
    public void start(StartConfiguration startConfiguration) {
        System.out.format("Processing %,d episodes.%n", startConfiguration.getEpisodes());
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(this::sample, 0, 1, TimeUnit.SECONDS);
    }

    void sample() {
        long episodesPerSecond = episode - previousEpisode;
        previousEpisode = episode;
        System.out.format("Episode %,d (%,d/s), testing success %d%% (%d steps needed)%n",
                episode, episodesPerSecond, (int)testingPercent.getAverageAndMarkReset(), (int)testingSteps.getAverageAndMarkReset());
    }
}
