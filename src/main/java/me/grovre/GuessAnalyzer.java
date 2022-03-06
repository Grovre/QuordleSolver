package me.grovre;

import me.grovre.board.Board;

import java.util.HashMap;

public class GuessAnalyzer {

    private final HashMap<Board, Integer> boardScores;
    private final Board board;

    public GuessAnalyzer() {
        this.boardScores = new HashMap<>(4);

        for(Board board : Board.getAllBoards()) {
            int score = board.generateScore();
            this.boardScores.put(board, score);
        }

        this.board = this.determineBestBoard();
        assert this.board != null;
    }

    public Board determineBestBoard() {
        Board bestBoard = this.boardScores.keySet().stream().findAny().orElseThrow();
        for(Board board : this.boardScores.keySet()) {
            if(bestBoard.getScore() < board.getScore()) {
                bestBoard = board;
            }
        }

        System.out.println("Best board: " + bestBoard);
        return bestBoard;
    }

    public String determineBestWord() {
        return null;
    }
}
