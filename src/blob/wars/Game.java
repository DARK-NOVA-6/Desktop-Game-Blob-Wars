/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blob.wars;

import static blob.wars.Board.player1;
import static blob.wars.Board.player2;
import java.util.Scanner;
import javafx.util.Pair;

/**
 *
 * @author MHD
 */
public class Game {

    public Board board;
    public int depth = 3;

    Game() {
        board = new Board();
        play();
    }

    public void play() {
        while (!board.isFinished()) {
            Scanner scanner = new Scanner(System.in);
            System.out.println(board);
            String temp = scanner.nextLine();
            int i = Integer.parseInt(temp);
            temp = scanner.nextLine();
            int j = Integer.parseInt(temp);
            temp = scanner.nextLine();
            int ii = Integer.parseInt(temp);
            temp = scanner.nextLine();
            int jj = Integer.parseInt(temp);
            System.out.println(" int " + i + " " + j + " " + ii + "h +" + jj);
            board = board.makeMove(i, j, ii, jj);
            if (!board.isFinished()) {
                board = AIPlay().getValue();
            }
        }
    }

    public Pair<Integer, Board> AIPlay() {
        int alpha = -11111111;
        int beta = 1111111;
        Board bestMove = null;
        for (Board nextBoard : board.getAllPossibleMoves(player1)) {
            if (alpha >= beta) {
                break;
            }

            int alphaFromChild = getMin(nextBoard, alpha, beta, depth).getValue();
            if (alphaFromChild > alpha) {
                bestMove = nextBoard;
                alpha = alphaFromChild;
                System.out.println("?!?!?!?!?!?!??");
            }
        }
        if (bestMove == null) {
            System.out.println("WTF???");
        }
        return new Pair(alpha, bestMove);
    }

    public Pair<Integer, Integer> getMax(Board board, int alpha, int beta, int depth) {
        if (depth == 0 || board.isFinished()) {
            alpha = Integer.max(alpha, board.evaluate());
            beta = Integer.min(beta, board.evaluate());
            return new Pair(alpha, beta);
        }
        for (int i = 0; i < board.height && alpha < beta; i++) {
            for (int j = 0; j < board.width && alpha < beta; j++) {
                for (Board b : board.getAllPossibleMoves1(player2, i, j)) {
                    alpha = Integer.max(alpha, getMin(b, alpha, beta, depth - 1).getValue());
                    if (alpha >= beta) {
                        break;
                    }
                }
            }
        }
        for (int i = 0; i < board.height && alpha < beta; i++) {
            for (int j = 0; j < board.width && alpha < beta; j++) {
                for (Board b : board.getAllPossibleMoves2(player2, i, j)) {
                    alpha = Integer.max(alpha, getMin(b, alpha, beta, depth - 1).getValue());
                    if (alpha >= beta) {
                        break;
                    }
                }
            }
        }

        return new Pair(alpha, beta);
    }

    public Pair<Integer, Integer> getMin(Board board, int alpha, int beta, int depth) {
        if (depth == 0 || board.isFinished()) {
            alpha = Integer.max(alpha, board.evaluate());
            beta = Integer.min(beta, board.evaluate());
            return new Pair(alpha, beta);
        }
        for (int i = 0; i < board.height && alpha < beta; i++) {
            for (int j = 0; j < board.width && alpha < beta; j++) {
                for (Board b : board.getAllPossibleMoves1(player1, i, j)) {
                    beta = Integer.min(beta, getMax(b, alpha, beta, depth - 1).getKey());
                    if (alpha >= beta) {
                        break;
                    }
                }
            }
        }
        for (int i = 0; i < board.height && alpha < beta; i++) {
            for (int j = 0; j < board.width && alpha < beta; j++) {
                for (Board b : board.getAllPossibleMoves2(player1, i, j)) {
                    beta = Integer.min(beta, getMax(b, alpha, beta, depth - 1).getKey());
                    if (alpha >= beta) {
                        break;
                    }
                }
            }
        }
        return new Pair(alpha, beta);
    }
//    public Pair<Integer, Integer> getMax(Board board, int alpha, int beta, int depth) {
//        if (depth == 0 || board.isFinished()) {
//            alpha = Integer.max (alpha, board.evaluate()) ;
//            beta = Integer.min (beta, board.evaluate()) ;
//            return new Pair (alpha, beta) ;
//        }
//        for (Board nextBoard: board.getAllPossibleMoves(player1)) {
//            if (alpha >= beta)
//                break ;
//            Pair <Integer,Integer> nxtEvaluate = getMin(nextBoard, alpha, beta, depth - 1) ;
//            alpha = Integer.max(alpha, nxtEvaluate.getValue()) ;
//        }
//        return new Pair (alpha, beta) ;
//    }
//
//    public Pair<Integer, Integer> getMin(Board board, int alpha, int beta, int depth) {
//        if (depth == 0 || board.isFinished()) {
//            alpha = Integer.max (alpha, board.evaluate()) ;
//            beta = Integer.min (beta, board.evaluate()) ;
//            return new Pair (alpha, beta) ;
//        }
//        for (Board nextBoard: board.getAllPossibleMoves(player2)) {
//            if (alpha >= beta)
//                break ;
//            Pair <Integer,Integer> nxtEvaluate = getMax(nextBoard, alpha, beta, depth - 1) ;
//            beta = Integer.min(beta, nxtEvaluate.getKey()) ;
//        }
//        return new Pair (alpha, beta) ;
//    }

    public Pair<Integer, Board> mx(Board board, int dep) {
        if (board.isFinished() || dep == 0) {
            return new Pair(board.evaluate(), board);
        }
        Pair<Integer, Board> result = new Pair(Integer.MIN_VALUE, board);
        for (Board all : board.getAllPossibleMoves(player1)) {
            Pair<Integer, Board> nxt = new Pair(mn(all, dep - 1).getKey(), all);
            if ((int) nxt.getKey() > (int) result.getKey()) {
                result = nxt;
            }
        }
        return result;
    }

    public Pair<Integer, Board> mn(Board board, int dep) {
        if (board.isFinished() || dep == 0) {
            return new Pair(board.evaluate(), board);
        }
        Pair<Integer, Board> result = new Pair(Integer.MAX_VALUE, board);
        for (Board all : board.getAllPossibleMoves(player2)) {
            Pair<Integer, Board> nxt = new Pair(mx(all, dep - 1).getKey(), all);
            if ((int) nxt.getKey() < (int) result.getKey()) {
                result = nxt;
            }
        }
        return result;
    }
}
