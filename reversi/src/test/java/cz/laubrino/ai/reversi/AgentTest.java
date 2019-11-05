package cz.laubrino.ai.reversi;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author tomas.laubr on 4.11.2019.
 */
class AgentTest {
    @Test
    void testConstructor(){
        Agent agent = new Agent(new QTableMap());
    }
}