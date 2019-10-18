package cz.laubrino.ai.prsi;

import cz.laubrino.ai.prsi.karty.Card;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

import static cz.laubrino.ai.prsi.karty.Card.*;

/**
 * @author tomas.laubr on 17.10.2019.
 */
public enum Action {
    LIZNI(null),

    VYNES_ZELENA_SEDMA(ZELENA_SEDMA),
    VYNES_ZELENA_OSMA(ZELENA_OSMA),
    VYNES_ZELENA_DEVITKA(ZELENA_DEVITKA),
    VYNES_ZELENA_DESITKA(ZELENA_DESITKA),
    VYNES_ZELENY_SPODEK(ZELENY_SPODEK),
    VYNES_ZELENY_SVRSEK(ZELENY_SVRSEK),
    VYNES_ZELENY_KRAL(ZELENY_KRAL),
    VYNES_ZELENE_ESO(ZELENE_ESO),

    VYNES_CERVENA_SEDMA(CERVENA_SEDMA),
    VYNES_CERVENA_OSMA(CERVENA_OSMA),
    VYNES_CERVENA_DEVITKA(CERVENA_DEVITKA),
    VYNES_CERVENA_DESITKA(CERVENA_DESITKA),
    VYNES_CERVENY_SPODEK(CERVENY_SPODEK),
    VYNES_CERVENY_SVRSEK(CERVENY_SVRSEK),
    VYNES_CERVENY_KRAL(CERVENY_KRAL),
    VYNES_CERVENE_ESO(CERVENE_ESO),

    VYNES_KULOVA_SEDMA(KULOVA_SEDMA),
    VYNES_KULOVA_OSMA(KULOVA_OSMA),
    VYNES_KULOVA_DEVITKA(KULOVA_DEVITKA),
    VYNES_KULOVA_DESITKA(KULOVA_DESITKA),
    VYNES_KULOVY_SPODEK(KULOVY_SPODEK),
    VYNES_KULOVY_SVRSEK(KULOVY_SVRSEK),
    VYNES_KULOVY_KRAL(KULOVY_KRAL),
    VYNES_KULOVE_ESO(KULOVE_ESO),

    VYNES_ZALUDOVA_SEDMA(ZALUDOVA_SEDMA),
    VYNES_ZALUDOVA_OSMA(ZALUDOVA_OSMA),
    VYNES_ZALUDOVA_DEVITKA(ZALUDOVA_DEVITKA),
    VYNES_ZALUDOVA_DESITKA(ZALUDOVA_DESITKA),
    VYNES_ZALUDOVY_SPODEK(ZALUDOVY_SPODEK),
    VYNES_ZALUDOVY_SVRSEK(ZALUDOVY_SVRSEK),
    VYNES_ZALUDOVY_KRAL(ZALUDOVY_KRAL),
    VYNES_ZALUDOVE_ESO(ZALUDOVE_ESO);

    Card card;
    
    Action(Card card) {
        this.card = card;
    }

    public Card getCard() {
        return card;
    }

    static Map<Card, Action> reverseMap = new EnumMap<>(Card.class);

    static Action getActionByCard(Card card) {
        Action action = reverseMap.get(card);

        if (action == null) {
            for (Action a  : values()) {
                if (Objects.equals(a.getCard(), card)) {
                    action = a;
                    reverseMap.put(card, a);
                }
            }
        }

        if (action == null) {
            throw new AssertionError();
        }

        return action;
    }
}
