package cz.laubrino.ai.framework;

/**
 * @author tomas.laubr on 28.11.2019.
 */
public class AgentConfiguration {
    private final float alpha;
    private final float alphaDecay;
    private final float gamma;
    private final float epsilonDecay;
    private final float epsilon;

    /**
     * @param alpha learning rate (0..1)
     * @param alphaDecay (0..1) (see epsilonDecay)
     * @param gamma discount factor (0..1)
     * @param epsilon initial epsilon (0..1)
     * @param epsilonDecay (0..1)  e.g. 0.99995f. Or 1 for no decay.
     */
    public AgentConfiguration(float alpha, float alphaDecay, float gamma, float epsilon, float epsilonDecay) {
        this.alpha = alpha;
        this.alphaDecay = alphaDecay;
        this.gamma = gamma;
        this.epsilonDecay = epsilonDecay;
        this.epsilon = epsilon;
    }

    public float getAlpha() {
        return alpha;
    }

    public float getAlphaDecay() {
        return alphaDecay;
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
