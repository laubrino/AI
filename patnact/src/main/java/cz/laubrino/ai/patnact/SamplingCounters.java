package cz.laubrino.ai.patnact;

import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author tomas.laubr on 7.11.2019.
 */
public class SamplingCounters {
    Map<String, SamplingCounter> samplingCounters = new HashMap<>();

    public SamplingCounters(String... samplingCounterNames) {
        for (String samplingCounterName : samplingCounterNames) {
            samplingCounters.put(samplingCounterName, new SamplingCounter());
        }
    }

    public void sample() {
        samplingCounters.forEach((name, samplingCounter) -> samplingCounter.sample());
    }

    public void incrementAndGet() {
        samplingCounters.forEach((name, samplingCounter) -> samplingCounter.incrementAndGet());
    }

    public void print(PrintStream printStream) {
        samplingCounters.entrySet().stream().sorted((o1, o2) -> Objects.compare(o1.getKey(), o2.getKey(), String::compareTo))
                .forEach(es -> printStream.println(es.getKey() + ": " + es.getValue().getCounts()));
    }

    String toString(String name) {
        SamplingCounter samplingCounter = samplingCounters.get(name);
        return name + ": " + samplingCounter.toString();
    }

    private static class SamplingCounter {
        private final List<Integer> counts = Collections.synchronizedList(new ArrayList<>());
        private final AtomicInteger counter = new AtomicInteger(0);

        public int incrementAndGet() {
            return counter.incrementAndGet();
        }

        void sample() {
            counts.add(counter.getAndSet(0));
        }

        public List<Integer> getCounts() {
            return counts;
        }

        @Override
        public String toString() {
            return counts.toString();
        }
    }
}
