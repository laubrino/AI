package cz.laubrino.ai.reversi;

import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static cz.laubrino.ai.reversi.Policko.BLACK;
import static cz.laubrino.ai.reversi.Policko.WHITE;

public class Games {
    private Random random = new Random();

    void playShortestGame(Environment environment) {
        if (Environment.BOARD_SIZE == 8) {
            environment.step(Action.get(3,2,BLACK));
            environment.step(Action.get(2,2,WHITE));
            environment.step(Action.get(1,2,BLACK));
            environment.step(Action.get(3,1,WHITE));
            environment.step(Action.get(4,0,BLACK));
            environment.step(Action.get(3,5,WHITE));
            environment.step(Action.get(3,6,BLACK));
            environment.step(Action.get(4,2,WHITE));
            environment.step(Action.get(5,3,BLACK));
        } else if (Environment.BOARD_SIZE == 6) {
            for (String action : new String[]{"[2,4]x","[1,3]o", "[0,2]x", "[2,1]o", "[2,0]x", "[3,1]o", "[4,2]x"}) {
                environment.step(Action.get(action));
            }
        } else {
            throw new RuntimeException("Unsupp. " + Environment.BOARD_SIZE + "x" + Environment.BOARD_SIZE);
        }
    }

    public void playRandomGame(Environment environment, int steps) {
        Policko onTurn = BLACK;

        while (steps > 0 && !environment.isGameOver()) {
            Policko finalOnTurn = onTurn;

            Set<Action> availableActions = environment.getAvailableActions().stream()
                    .filter(action -> action.getP() == finalOnTurn)
                    .collect(Collectors.toSet());

            Action action = availableActions.stream()
                    .skip(random.nextInt(availableActions.size()))
                    .findFirst()
                    .get();

            environment.step(action);
            System.out.println(action + ": " + environment.getState().toString());

            steps--;
            onTurn = onTurn == BLACK ? WHITE : BLACK;
        }
        System.out.println("Game over? " + environment.isGameOver());
    }
}
