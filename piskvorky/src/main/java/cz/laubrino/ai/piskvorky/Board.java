package cz.laubrino.ai.piskvorky;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Only square board supported.
 * @author tomas.laubr on 10.10.2019.
 */
public class Board {
    private Field[][] board;

    public Board(int dimension) {
        board = new Field[dimension][dimension];
        fillBoard(Field.NIC);
    }

    void fillBoard(Field field) {
        for (int i=0;i<board.length;i++) {
            Arrays.fill(board[i], field);
        }
    }

    public void put(int x, int y, Field field) {
        board[x][y] = field;
    }

    public Field[][] get() {
        return board;
    }

    public Field get(int x, int y) {
        return board[x][y];
    }

    public int size() {
        return board.length;
    }

    /**
     * Return all positions of a filed
     * @return
     */
    public List<Position> getFieldPositions(Field field) {
        List<Position> availableActions = new ArrayList<>();
        for (int i=0;i<board.length;i++) {
            for (int j=0; j<board.length;j++) {
                if(board[i][j] == field) {
                    availableActions.add(new Position(i,j));
                }
            }
        }
        return availableActions;

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(board.length * board.length * 2);   // approx.

        sb.append("+");
        for (int i=0;i<board.length;i++) {
            sb.append("-");
        }
        sb.append("+\n");

        for (int i=0;i<board.length;i++) {
            sb.append("|");
            for (int j=0; j<board.length;j++) {
                sb.append(board[j][i].toString());
            }
            sb.append("|\n");
        }

        sb.append("+");
        for (int i=0;i<board.length;i++) {
            sb.append("-");
        }
        sb.append("+");

        return sb.toString();
    }

    @Override
    protected Board clone() {
        Board b = new Board(board.length);
        for (int i=0;i<board.length;i++) {
            System.arraycopy(board[i],0, b.board[i], 0, board.length);
        }

        return b;
    }
}
