package cz.laubrino.ai.patnact;

import cz.laubrino.ai.framework.AgentConfiguration;
import cz.laubrino.ai.framework.Processor;
import cz.laubrino.ai.framework.observers.ChartObserver;
import cz.laubrino.ai.framework.observers.SysoutObserver;

/**
 * @author tomas.laubr on 29.10.2019.
 */
public class Main {
    private static final long EPISODES = 50_000_000;
    private static final int MAX_STEPS_PER_EPISODE = 10000;


    public static void main(String[] args) throws InterruptedException {

        Processor<Action> processor = new Processor<>(new AgentConfiguration(0.5f, 0.9f, 0.99995f, 1f), PatnactEnvironment::new,
                EPISODES, MAX_STEPS_PER_EPISODE, 0.001f, 1000, Action.class);

        processor.addObserver(new SysoutObserver());
        processor.addObserver(new ChartObserver("\"15\" RL (" + PatnactEnvironment.BOARD_SIZE + "x" + PatnactEnvironment.BOARD_SIZE + ")"));

        processor.process();
    }
}
