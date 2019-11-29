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
    void testingEpisodeFinished(boolean success, long steps);

}
