package cz.laubrino.ai.framework.observers;

/**
 * @author tomas.laubr on 2.12.2019.
 */
class Averaging {
    private volatile float sum;
    private volatile int count;
    private volatile boolean shouldReset = true;

    public synchronized void add(float amount) {
        if (shouldReset) {
            reset();
        }
        sum+=amount;
        count++;
    }

    public synchronized float getAverage() {
        return sum/count;
    }

    public synchronized float getAverageAndMarkReset() {
        shouldReset = true;

        if (count == 0) {
            return 0;
        }

        float average = sum/count;
        return average;
    }

    public synchronized void reset() {
        sum = 0;
        count = 0;
        shouldReset = false;
    }
}
