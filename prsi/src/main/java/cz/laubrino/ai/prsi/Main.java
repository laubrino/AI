package cz.laubrino.ai.prsi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

/**
 * @author tomas.laubr on 17.10.2019.
 */
public class Main {
    public static final float ALPHA = 0.4f;
    public static final float GAMMA = 0.9f;

    public static void main(String[] args) throws FileNotFoundException {
        QTable qTableKarel = new QTable();
        QTable qTableFranta = new QTable();

        Player karel = new Player("Karel", qTableKarel);
        Player franta = new Player("Franta", qTableFranta);

        Environment env = new Environment(new Players(karel, franta), karel);

        for (int i=0;i<1000;i++) {
            env.reset(env.currentPlayer);

            karel.setPrevObservedState(null);
            karel.setPrevAction(null);
            franta.setPrevAction(null);
            franta.setPrevObservedState(null);
            boolean finished = false;

            for (int n=0;n<1000;n++) {
                try {
//                System.out.println(env.toString());

                    Player currentPlayer = env.getCurrentPlayer();

                    Action action = currentPlayer.chooseAction(env.getAvailableActions());

                    StepResult stepResult = env.step(action);

                    currentPlayer.getPrevObservedState().ifPresent(os -> {
                        float qOld = currentPlayer.getqTable().get(os.toString(), currentPlayer.getPrevAction().get());
                        float qNew = qOld;

                        if (stepResult.done) {
                            qNew += ALPHA * (stepResult.getReward() - qOld);
                        } else {
                            qNew += ALPHA * (stepResult.getReward() + GAMMA * currentPlayer.getqTable().get(stepResult.getObservedState().toString(), action) - qOld);
                        }

                        currentPlayer.getqTable().put(os.toString(), currentPlayer.getPrevAction().get(), qNew);
                    });

                    currentPlayer.setPrevObservedState(stepResult.getObservedState());
                    currentPlayer.setPrevAction(action);

                    if (stepResult.done) {
                        System.out.println("Konec po " + n + " krocich");
                        finished = true;
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }

            if (!finished) {
                System.out.println("Nedohrana hra");
            }

            System.out.println(env.toString());
        }

        System.out.print("Saving qTables.....");
        qTableFranta.print(new PrintStream(new File("c:/x/prsi-franta.qtable")));
        System.out.print("......");
        qTableKarel.print(new PrintStream(new File("c:/x/prsi-karel.qtable")));
        System.out.println("done.");

    }
}
