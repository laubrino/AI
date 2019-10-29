package cz.laubrino.ai.prsi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

/**
 * @author tomas.laubr on 17.10.2019.
 */
public class Main {
    public static final boolean STOP_ON_ERROR = true;

    public static void main(String[] args) throws FileNotFoundException {
        QTable qTableKarel = new QTable();
        QTable qTableFranta = new QTable();

        Player karel = new Player("Karel", qTableKarel);
        Player franta = new Player("Franta", qTableFranta);

        Environment env = new Environment(karel, karel, franta);

        for (int i=0;i<100;i++) {
            env.resetEnvironment(env.currentPlayer);

            boolean finished = false;
            boolean shitHappend = false;

            Action action = env.getCurrentPlayer().chooseAction(env.getAvailableActions());

            for (int n=0;n<1000;n++) {
                try {
//                System.out.println(env.toString());

                    StepResult stepResult = env.step(action);

                    Player currentPlayer = env.getCurrentPlayer();

                    currentPlayer.learn(stepResult.getReward());

                    env.nextPlayer();

                    Action action2 = currentPlayer.chooseAction(env.getAvailableActions());

                    currentPlayer.setAction(action2);
                    currentPlayer.setObservedState();

                    if (stepResult.done) {
                        System.out.println("Konec po " + n + " krocich");
                        finished = true;
                        break;
                    }

                    env.nextPlayer();
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println(env.toString());
                    env.players.forEach(System.err::println);
                    System.err.println("Choosen action: " + action);
                    shitHappend = true;
                    break;
                }
            }

            if (!finished) {
                System.out.println("Nedohrana hra");
            }

            System.out.println(env.toString());

            if (shitHappend && STOP_ON_ERROR) {
                break;
            }
        }

        System.out.print("Saving qTables.....");
        qTableFranta.print(new PrintStream(new File("c:/x/prsi-franta.qtable")));
        System.out.print("......");
        qTableKarel.print(new PrintStream(new File("c:/x/prsi-karel.qtable")));
        System.out.println("done.");

    }
}
