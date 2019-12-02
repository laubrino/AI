package cz.laubrino.ai.rubikovka;

import cz.laubrino.ai.framework.Observer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author tomas.laubr on 2.12.2019.
 */
public class RubikObserver implements Observer {
    private volatile long episode;
    private Averaging testingPercent;
    private ScheduledExecutorService scheduledExecutorService;

    public RubikObserver() {
        testingPercent = new Averaging();
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(this::sample, 0, 1, TimeUnit.SECONDS);
    }

    public void shutDown() throws InterruptedException {
        scheduledExecutorService.shutdownNow();
        scheduledExecutorService.awaitTermination(10, TimeUnit.SECONDS);
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

    }

    @Override
    public void testingBatchFinished(int successEpisodes, int allEpisodes) {
        testingPercent.add((float)successEpisodes/allEpisodes*100);
    }

    void sample() {
        System.out.format("Episode %,d, testing success %d%%%n", episode, (int)testingPercent.getAverageAndMarkReset());
    }
}
