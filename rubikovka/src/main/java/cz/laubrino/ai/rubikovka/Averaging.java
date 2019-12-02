package cz.laubrino.ai.rubikovka;

/**
 * @author tomas.laubr on 2.12.2019.
 */
public class Averaging {
    private volatile float sum;
    private volatile int count;
    private boolean shouldReset = true;

    public void add(float amount) {
        if (shouldReset) {
            reset();
        }
        sum+=amount;
        count++;
    }

    public float getAverage() {
        return sum/count;
    }

    public float getAverageAndMarkReset() {
        float average = sum/count;
        shouldReset = true;
        return average;
    }

    public void reset() {
        sum = 0;
        count = 0;
        shouldReset = false;
    }
}
