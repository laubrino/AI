package cz.laubrino.ai.dsg;

import cz.laubrino.ai.framework.AgentConfiguration;
import cz.laubrino.ai.framework.Processor;
import cz.laubrino.ai.framework.observers.ChartObserver;
import cz.laubrino.ai.framework.observers.SysoutObserver;

/**
 * @author tomas.laubr on 12.12.2019.
 */
public class Main {
    private static final long EPISODES = 1_000_000;
    private static final int MAX_STEPS_PER_EPISODE = 2000;


    public static void main(String[] args) throws InterruptedException {

        Processor<Action> processor = new Processor<>(new AgentConfiguration(0.1f, 0.9f, 0.99995f, 1f), DSGEnvironment::new,
                EPISODES, MAX_STEPS_PER_EPISODE, 0.01f, 1000, Action.class);

        processor.addObserver(new SysoutObserver());
        processor.addObserver(new ChartObserver("David's Simple Game"));

        processor.process();
    }
}
