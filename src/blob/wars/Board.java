/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blob.wars;

import java.util.ArrayList;
import java.util.Objects;
import javafx.util.Pair;

/**
 *
 * @author MHD
 */
public class Board {

    public Board(int height, int width, ArrayList<ArrayList<Integer>> board) {
        this.height = height;
        this.width = width;
        this.grid = board;
    }

    public static int player1 = 1, player2 = 2, empty = -1, obstacle = -2;
    public int height, width;
    public ArrayList<ArrayList<Integer>> grid;

    public Board() {
        height = 6;
        width = 6;
        grid = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            grid.add(new ArrayList<>());
            for (int j = 0; j < width; j++) {
                grid.get(i).add(empty);
            }
        }

        grid.get(0).set(0, player1);
        grid.get(0).set(width - 1, player1);
        grid.get(height - 1).set(0, player2);
        grid.get(height - 1).set(width - 1, player2);
    }

    private boolean isValidPosition(int i, int j) {
        return i >= 0 && i < height && j >= 0 && j < width && grid.get(i).get(j) != obstacle;
    }

    private boolean isValidMove(int i, int j) {
        return isValidPosition(i, j) && grid.get(i).get(j) == empty;
    }

    public ArrayList<Pair<Integer, Integer>> getValidJump(int i, int j) {
        ArrayList<Pair<Integer, Integer>> result = new ArrayList<>();
        for (int di = -2; di <= 2; di += 2) {
            for (int dj = -2; dj <= 2; dj += 2) {
                if (di != 0 || dj != 0) {
                    if (isValidMove(i + di, j + dj)) {
                        result.add(new Pair(i + di, j + dj));
                    }
                }
            }
        }
        return result;
    }

    public ArrayList<Pair<Integer, Integer>> getValidSpawn(int i, int j) {
        ArrayList<Pair<Integer, Integer>> result = new ArrayList<>();
        for (int di = -1; di <= 1; di += 1) {
            for (int dj = -1; dj <= 1; dj += 1) {
                if (di != 0 || dj != 0) {
                    if (isValidMove(i + di, j + dj)) {
                        result.add(new Pair(i + di, j + dj));
                    }
                }
            }
        }
        return result;
    }

    public boolean isFinished() {
        return noMoreMove(player1) && noMoreMove(player2);
    }

    private boolean noMoreMove(int player) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (grid.get(i).get(j) == player) {
                    if (!getValidJump(i, j).isEmpty()) {
                        return false;
                    }
                    if (!getValidSpawn(i, j).isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public Pair<Integer, Integer> getScore() {
        int scorePlayer1 = 0, scorePlayer2 = 0;
        for (ArrayList<Integer> row : grid) {
            for (int cell : row) {
                if (cell == player1) {
                    scorePlayer1 += 1;
                } else if (cell == player2) {
                    scorePlayer2 += 1;
                }
            }
        }
        return new Pair(scorePlayer1, scorePlayer2);
    }

    private int getNextPlayer(int player) {
        if (player == player1) {
            return player2;
        } else {
            return player1;
        }
    }

    public Board makeMove(int curI, int curJ, int nextI, int nextJ) {
        ArrayList<ArrayList<Integer>> newGrid = new ArrayList<>();
        for (ArrayList<Integer> row : grid) {
            newGrid.add(new ArrayList<Integer>(row));
        }
        int player = grid.get(curI).get(curJ);
        int otherPlayer = getNextPlayer(player);
        if (Math.abs(curI - nextI) > 1 || Math.abs(curJ - nextJ) > 1) {
            newGrid.get(curI).set(curJ, empty);
        }
        newGrid.get(nextI).set(nextJ, player);
        for (int di = -1; di <= 1; di++) {
            for (int dj = -1; dj <= 1; dj++) {
                int i = nextI + di;
                int j = nextJ + dj;
                if (isValidPosition(i, j)) {
                    if (newGrid.get(i).get(j) == otherPlayer) {
                        newGrid.get(i).set(j, player);
                    }
                }
            }
        }
        return new Board(height, width, newGrid);
    }

    public ArrayList<Board> getAllPossibleMoves1(int player, int i, int j) {
        if (grid.get(i).get(j) != player) {
            return new ArrayList<>();
        }
        ArrayList<Board> possible = new ArrayList<>();
        
        for (Pair<Integer, Integer> nextPosition : getValidSpawn(i, j)) {
            possible.add(makeMove(i, j, nextPosition.getKey(), nextPosition.getValue()));
        }
        return possible;
    }
    public ArrayList<Board> getAllPossibleMoves2(int player, int i, int j) {
        if (grid.get(i).get(j) != player) {
            return new ArrayList<>();
        }
        ArrayList<Board> possible = new ArrayList<>();
        for (Pair<Integer, Integer> nextPosition : getValidJump(i, j)) {
            possible.add(makeMove(i, j, nextPosition.getKey(), nextPosition.getValue()));
        }
        return possible;
    }

    public ArrayList<Board> getAllPossibleMoves(int player) {
        ArrayList<Board> possible = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (grid.get(i).get(j) == player) {
                    for (Pair<Integer, Integer> nextPosition : getValidJump(i, j)) {
                        possible.add(makeMove(i, j, nextPosition.getKey(), nextPosition.getValue()));
                    }
                    for (Pair<Integer, Integer> nextPosition : getValidSpawn(i, j)) {
                        possible.add(makeMove(i, j, nextPosition.getKey(), nextPosition.getValue()));
                    }
                }
            }
        }
        return possible;
    }

    public int evaluate() {
        return getScore().getKey() - getScore().getValue();
    }

    @Override
    public boolean equals(Object otherObject) {
        if (!(otherObject instanceof Board)) {
            return false;
        }
        return grid.equals(((Board) otherObject).grid);
    }

    @Override
    public int hashCode() {
        return grid.hashCode();
    }

    @Override
    public String toString() {
        String result = "";
        for (ArrayList<Integer> row : grid) {
            for (Integer cell : row) {
                if (cell == player1 || cell == player2) {
                    result = result + cell + "";
                } else {
                    result = result + ".";
                }
            }
            result = result + "\n";
        }
        return result;
    }
}
