package cz.laubrino.ai.prsi;

import cz.laubrino.ai.prsi.karty.Card;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author tomas.laubr on 24.10.2019.
 */
class ActionTest {

    @Test
    void testGetActionByCard() {
        EnumSet<Action> actions = EnumSet.noneOf(Action.class);

        for (Card card : Card.values()) {
            Action actionByCard = Action.getActionByCard(card);
            assertFalse(actions.contains(actionByCard));
            actions.add(actionByCard);
            assertTrue(actions.contains(actionByCard));
        }

        assertEquals(32, actions.size());
    }
}