package cz.laubrino.ai.piskvorky;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author tomas.laubr on 10.10.2019.
 */
public class State {
    Board board;
    int length;

    /**
     *
     * @param board
     * @param length piskvorek length, e.g. 5
     */
    public State(Board board, int length) {
        this.board = board;
        this.length = length;
    }

    public Board getBoard() {
        return board;
    }


    public int getLength() {
        return length;
    }

    /**
     * Return list of empty positions on the board
     * @param field
     * @return
     */
    public List<Action> getAvailableActions() {
        List<Action> availableActions = new ArrayList<>();
        for (int i=0;i<board.get().length;i++) {
            for (int j=0; j<board.get()[i].length;j++) {
                if(board.get()[i][j] == Field.NIC) {
                    availableActions.add(new Action(new Position(i,j)));
                }
            }
        }
        Collections.sort(availableActions);
        return availableActions;
    }

    /**
     * State as a string
     * @return
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("'");

        for (Field[] col : board.get()) {
            for (int i=0;i<col.length;i++) {
                sb.append(col[i]);
            }
        }

        sb.append(length);
        sb.append("'");

        return sb.toString();
    }
}
