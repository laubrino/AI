package cz.laubrino.ai.prsi;

import cz.laubrino.ai.prsi.karty.Card;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author tomas.laubr on 9.10.2019.
 */
public class ObservedState {
    OdhazovaciBalicek odhazovaciBalicek;
    Set<Card> kartyVRuce;
    List<Integer> otherPlayersCardCounts;     // pocet karet v rukach ostatnich hracu;
    String stringSnapshot;

    public ObservedState(OdhazovaciBalicek odhazovaciBalicek, Set<Card> kartyVRuce, List<Integer> otherPlayersCardCounts) {
        this.odhazovaciBalicek = new OdhazovaciBalicek(odhazovaciBalicek.getCards());

        this.kartyVRuce = EnumSet.noneOf(Card.class);
        this.kartyVRuce.addAll(kartyVRuce);

        this.otherPlayersCardCounts = otherPlayersCardCounts;
        this.stringSnapshot = asString();
    }

    private String asString() {
        StringBuilder sb = new StringBuilder();
        for (Card card : kartyVRuce) {
            sb.append(card.toString());
        }
        sb.append("|").append(odhazovaciBalicek.peekTopCard());
        sb.append("|").append(otherPlayersCardCounts.stream().map(count -> Boolean.toString(count > 1)).collect(Collectors.joining(",")));  // "true" if more than 1 card
        return sb.toString();
    }

    @Override
    public String toString() {
        return stringSnapshot;
    }
}
