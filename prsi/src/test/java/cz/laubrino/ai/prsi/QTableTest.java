package cz.laubrino.ai.prsi;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author tomas.laubr on 17.10.2019.
 */
class QTableTest {

    @Test
    void print() {
        QTable qTable = new QTable();
        qTable.put("stav1", Action.VYNES_CERVENA_DESITKA, 0f);
        qTable.put("stav2", Action.LIZNI, 10.10f);
        qTable.put("stav2", Action.VYNES_ZALUDOVY_SVRSEK, 2.5f);
        qTable.put("stav3", Action.VYNES_KULOVY_KRAL, 1.10f);

        qTable.print(System.out);
    }
}