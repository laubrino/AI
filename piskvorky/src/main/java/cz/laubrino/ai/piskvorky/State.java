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
    Field field;        // who is on turn

    /**
     *
     * @param board
     * @param length piskvorek length, e.g. 5
     * @param field kdo je na rade
     */
    public State(Board board, int length, Field field) {
        this.board = board;
        this.length = length;
        this.field = field;
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

        for (int i=0;i<board.size();i++) {
            for (int j=0;j<board.size();j++) {
                sb.append(board.get(j,i));
            }
        }


        sb.append(length);
        sb.append(field);
        sb.append("'");

        return sb.toString();
    }
}
