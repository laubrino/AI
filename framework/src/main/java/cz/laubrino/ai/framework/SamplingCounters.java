package cz.laubrino.ai.framework;

import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

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

    public int incrementAndGet(String name) {
        return samplingCounters.get(name).incrementAndGet();
    }

    public void print(PrintStream printStream) {
        samplingCounters.entrySet().stream().sorted((es1, es2) -> Objects.compare(es1.getKey(), es2.getKey(), String::compareTo))
                .forEach(es -> printStream.println(es.getKey() + ": " + es.getValue().getCounts()));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        samplingCounters.entrySet().stream().sorted((es1, es2) -> Objects.compare(es1.getKey(), es2.getKey(), String::compareTo))
                .forEach(es -> {
                    sb.append(es.getKey());
                    sb.append(": ");
                    sb.append(es.getValue().getCounts());
                    sb.append("\n");
                });

        return sb.toString();
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
