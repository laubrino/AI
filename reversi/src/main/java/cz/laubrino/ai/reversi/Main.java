package cz.laubrino.ai.reversi;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * @author tomas.laubr on 5.11.2019.
 */
public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        QTable<State, Action> qTableWhite = new QTableMap();
        QTable<State, Action> qTableBlack = new QTableMap();
        Agent whiteAgent = new Agent("byly", qTableWhite);
        Agent blackAgent = new Agent("cerny", qTableBlack);

        AtomicLong lastTimePrint = new AtomicLong();
        Environment environment = new Environment();
        environment.addObserver(Policko.WHITE, whiteAgent);
        environment.addObserver(Policko.BLACK, blackAgent);

        Policko currentColor;
        Agent currentAgent;

        for (int n=0;n<1_000;n++) {
            environment.reset();
            currentColor = Policko.BLACK;
            environment.notifyObservers(currentColor, new StepResult(new State(environment.getBoard()), 0f, false, StepResult.Status.CONTINUE));

            for (long i = 0; (i< Environment.BOARD_SIZE * Environment.BOARD_SIZE + 3) && !environment.isGameOver(); i++) {
                if (currentColor == Policko.BLACK) {
                    currentAgent = blackAgent;
                } else {
                    currentAgent = whiteAgent;
                }

                Policko finalCurrentColor = currentColor;
                Action action = currentAgent.chooseAction(environment.getState(), environment.getAvailableActions().stream().filter(action1 -> action1.getP() == finalCurrentColor).collect(Collectors.toSet()));

                environment.step(action);

                currentColor = currentColor == Policko.BLACK ? Policko.WHITE : Policko.BLACK;
            }

            if (System.currentTimeMillis() > (lastTimePrint.get() + 1_000)) {
                System.out.println("Iteration n = " + n);
                lastTimePrint.set(System.currentTimeMillis());
                System.out.println(environment);
            }

        }

        qTableBlack.print(new PrintStream(new BufferedOutputStream(new FileOutputStream("c:\\x\\reverziQTable.txt"))));
        //qTableBlack.print(System.out);
    }
}
