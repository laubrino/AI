package cz.laubrino.ai.prsi;

import cz.laubrino.ai.prsi.karty.Card;
import cz.laubrino.ai.prsi.karty.Value;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author tomas.laubr on 16.10.2019.
 */
public class Environment {
    private static final int NUMBER_OF_CARDS = 4;       // pocet karet v ruce

    Talon talon;
    List<Player> players;
    Player currentPlayer;
    Map<Player, EnumSet<Card>> kartyVRukach;
    OdhazovaciBalicek odhazovaciBalicek;

    public Environment(Player currentPlayer, Player... players) {
        this.players = new ArrayList<>(Arrays.asList(players));
        resetEnvironment(currentPlayer);
    }

    public void resetEnvironment(Player currentPlayer) {
        List<Card> cards = new LinkedList<>(Arrays.asList(Card.values()));

        // zamichej karty karty
        Collections.shuffle(cards);

        // rozdej karty hracum
        kartyVRukach = new HashMap<>();
        for (Player p : players) {
            EnumSet<Card> cardsToHand = EnumSet.noneOf(Card.class);
            for (int i=0;i<NUMBER_OF_CARDS;i++) {
                cardsToHand.add(cards.remove(0));        // intentional 0
            }
            kartyVRukach.put(p, cardsToHand);
        }

        odhazovaciBalicek = new OdhazovaciBalicek(cards.remove(0));

        this.currentPlayer = currentPlayer;

        talon = new Talon(cards);
        if (talon.size() != (32 - NUMBER_OF_CARDS * kartyVRukach.size() - 1)) {
            throw new AssertionError(talon.size());
        }

        distributeObservedState();
    }

    void nextPlayer(){
        int i;
        for (i=0;i<players.size();i++) {
            if(players.get(i).equals(currentPlayer)) {
                break;
            }
        }

        currentPlayer = players.get(++i%players.size());
    }

    /**
     *  vsem hracum nasetuj observed state, tak jak ho vidi po akci
     */
    private void distributeObservedState() {
        // vsem hracum nasetuj observed state, tak jak ho vidi po akci
        for (Player player : kartyVRukach.keySet()) {
            player.setObservedState(new ObservedState(odhazovaciBalicek, kartyVRukach.get(player), countPlayersCards(player)));
        }
    }

    StepResult step(Action action) {
        StepResult stepResult;

        if (action == Action.LIZNI) {        // lizni si
            if (!takeCardFromTalon(currentPlayer)) {
                stepResult = new StepResult(new ObservedState(odhazovaciBalicek, kartyVRukach.get(currentPlayer), countPlayersCards(currentPlayer)), -100f, true, "Lize z prazdneho talonu");
            } else {
                stepResult = new StepResult(new ObservedState(odhazovaciBalicek, kartyVRukach.get(currentPlayer), countPlayersCards(currentPlayer)), -1f, false, "Lize");
            }
        } else {
            Card card = action.getCard();     // budeme hrat tuto kartu z ruky
            kartyVRukach.get(currentPlayer).remove(card);
            odhazovaciBalicek.putCard(card);

            ObservedState observedState = new ObservedState(odhazovaciBalicek, kartyVRukach.get(currentPlayer), countPlayersCards(currentPlayer));
            if (kartyVRukach.get(currentPlayer).isEmpty()) {
                stepResult = new StepResult(observedState, 100f, true, "Odhozena posledni karta, vyhrali jsme");
            } else {
                stepResult = new StepResult(observedState, 1f, false, "Hra pokracuje");
            }
        }

        // vsem hracum nasetuj observed state, tak jak ho vidi po akci
        distributeObservedState();

        nextPlayer();

        return stepResult;
    }

    EnumSet<Action> getAvailableActions() {
        EnumSet<Action> availableActions = EnumSet.noneOf(Action.class);

        // bud si muze liznout, kdyz jsou karty
        if (!talon.isEmpty() || odhazovaciBalicek.size() > 1) {
            availableActions.add(Action.LIZNI);
        }

        // nebo muze zahrat kartu, kdyz pasuje na odhazovaci balicek
        for (Card cardInHand : kartyVRukach.get(currentPlayer)) {
            if (odhazovaciBalicek.peekTopCard().getColor() == cardInHand.getColor()
                    || odhazovaciBalicek.peekTopCard().getValue() == cardInHand.getValue() || cardInHand.getValue() == Value.SVRSEK) {
                availableActions.add(Action.getActionByCard(cardInHand));
            }
        }

        return availableActions;
    }

    /**
     * Dej hraci kartu z talonu
     * @param player
     * @return true if successful, false if talon is empty
     */
    private boolean takeCardFromTalon(Player player) {
        if (talon.isEmpty()) {                  // prazdny talon, musime otocit odhazovaci balicek
            if (odhazovaciBalicek.size() <= 1) {
                return false;
            } else {
                talon = odhazovaciBalicek.toTalon();
            }
        }

        kartyVRukach.get(player).add(talon.takeCard());
        return true;
    }

    /**
     * List of other players cards count
     * @return
     */
    private List<Integer> countPlayersCards(Player currentPlayer) {
        return players.stream()
                .filter(player -> !player.equals(currentPlayer))
                .map(player -> kartyVRukach.get(player).size())
                .collect(Collectors.toList());
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("==============").append("\n");
        kartyVRukach.forEach((player, cards) -> {
            if (player.equals(currentPlayer)) {
                sb.append("*");
            }
            sb.append(player.getName()).append(": ")
                            .append(cards.stream().map(Card::name).collect(Collectors.joining(", ")))
                            .append("\n");
                }
        );
        sb.append(odhazovaciBalicek.peekTopCard().name()).append("[").append(odhazovaciBalicek.size()).append("] ");
        sb.append("[").append(talon.size()).append("]\n");
        sb.append("--");

        return sb.toString();
    }
}
