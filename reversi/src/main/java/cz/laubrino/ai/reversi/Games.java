package cz.laubrino.ai.reversi;

import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static cz.laubrino.ai.reversi.Policko.BLACK;
import static cz.laubrino.ai.reversi.Policko.WHITE;

public class Games {
    private Random random = new Random();

    void playShortestGame(Environment environment) {
        environment.step(new Action(3,2,BLACK));
        environment.step(new Action(2,2,WHITE));
        environment.step(new Action(1,2,BLACK));
        environment.step(new Action(3,1,WHITE));
        environment.step(new Action(4,0,BLACK));
        environment.step(new Action(3,5,WHITE));
        environment.step(new Action(3,6,BLACK));
        environment.step(new Action(4,2,WHITE));
        environment.step(new Action(5,3,BLACK));
    }

    public void playRandomGame(Environment environment, int steps) {
        Policko onTurn = BLACK;

        while (steps > 0) {
            Policko finalOnTurn = onTurn;
//            Optional<Action> optionalAction = environment.findAvailableActions().stream()
//                    .filter(action -> action.getP() == finalOnTurn)
//                    .findAny();

            Set<Action> availableActions = environment.findAvailableActions().stream()
                    .filter(action -> action.getP() == finalOnTurn)
                    .collect(Collectors.toSet());

            Optional<Action> optionalAction = Optional.empty();
            if (!availableActions.isEmpty()) {
                optionalAction = availableActions.stream()
                        .skip(random.nextInt(availableActions.size()))
                        .findFirst();
            }

            Action action = optionalAction.orElse(Action.getPassAction(onTurn));
            environment.step(action);

            steps--;
            onTurn = onTurn == BLACK ? WHITE : BLACK;
        }
    }
}
