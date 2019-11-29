package cz.laubrino.ai.framework;

/**
 * @author tomas.laubr on 29.11.2019.
 */
interface AgentObserver {
    /**
     * reports Q value change (absolute and in percent)
     */
    void qValueChanged(float deltaQ, int deltaQInPercent);
}
