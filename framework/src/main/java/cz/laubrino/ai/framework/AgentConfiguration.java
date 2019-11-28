package cz.laubrino.ai.framework;

/**
 * @author tomas.laubr on 28.11.2019.
 */
public class AgentConfiguration {
    private final float alpha;
    private final float gamma;
    private final float epsilonDecay;
    private final float epsilon;

    public AgentConfiguration(float alpha, float gamma, float epsilonDecay, float epsilon) {
        this.alpha = alpha;
        this.gamma = gamma;
        this.epsilonDecay = epsilonDecay;
        this.epsilon = epsilon;
    }

    public float getAlpha() {
        return alpha;
    }

    public float getGamma() {
        return gamma;
    }

    public float getEpsilonDecay() {
        return epsilonDecay;
    }

    public float getEpsilon() {
        return epsilon;
    }
}
