package cz.laubrino.ai.framework;

/**
 * @author tomas.laubr on 29.11.2019.
 */
public interface Observer {
    /**
     * reports Q value change (absolute and in percent)
     */
    void qValueChanged(float deltaQ, int deltaQInPercent);

    /**
     * @param episode number of episode
     * @param steps steps necessary to finish the episode
     */
    void learningEpisodeFinished(long episode, long steps);

    /**
     * @param steps steps necessary to finish the episode
     */
    void testingEpisodeFinished(ActionResult actionResult, long steps);

    /**
     * Triggered after each max-testing episodes
     * @param successEpisodes
     * @param allEpisodes
     */
    void testingBatchFinished(int successEpisodes, int allEpisodes, float minReward, float maxReward, float averageReward);

    /**
     * Triggered on process end.
     */
    void end();

    /**
     * Notify process start, pass configuration
     * @param startConfiguration
     */
    void start(StartConfiguration startConfiguration);

    class StartConfiguration {
        private final long episodes;

        public StartConfiguration(long episodes) {
            this.episodes = episodes;
        }

        public long getEpisodes() {
            return episodes;
        }
    }


}
