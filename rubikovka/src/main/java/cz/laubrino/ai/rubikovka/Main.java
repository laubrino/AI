package cz.laubrino.ai.rubikovka;

import cz.laubrino.ai.framework.AgentConfiguration;
import cz.laubrino.ai.framework.Processor;
import cz.laubrino.ai.framework.observers.ChartObserver;
import cz.laubrino.ai.framework.observers.SysoutObserver;

public class Main {
    private static final long EPISODES = 2_000_000;
    private static final int MAX_STEPS_PER_EPISODE = 1_000;
    private static final int TESTING_EPIZODES = 100;        // number of plays in each testing step during learning
    private static final float TESTING_FREQ = 0.001f;


    public static void main(String[] args) throws InterruptedException {
        Processor<Action> processor = new Processor<>(new AgentConfiguration(0.5f, 0.9f, 0.99995f, 1f), RubikEnvironment::new,
                EPISODES, MAX_STEPS_PER_EPISODE, TESTING_FREQ, TESTING_EPIZODES, Action.class);

        processor.addObserver(new SysoutObserver());
        processor.addObserver(new ChartObserver("Rubik's Cube RL"));

        processor.process();
    }
}
