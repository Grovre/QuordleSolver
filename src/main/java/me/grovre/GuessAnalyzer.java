package me.grovre;

import me.grovre.board.Board;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class GuessAnalyzer {

    private final HashMap<Board, Integer> boardScores;
    private final Board board;

    public GuessAnalyzer() {
        this.boardScores = new HashMap<>(4);

        for(Board board : Board.allBoards) {
            int score = board.generateScore();
            this.boardScores.put(board, score);
        }

        this.board = this.determineBestBoard();
        assert this.board != null;
    }

    public Board getBoard() {
        return this.board;
    }

    public Board determineBestBoard() {
        if(this.board != null && !this.board.isComplete()) return this.board;
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
        if(this.board.isComplete()) this.determineBestBoard();
        System.out.println("Word being determined on board with row 1 as: " + this.board.getRows().get(0).getCorrectWordParts());
        LinkedList<String> possibleWords = new FileUtil(new File("C:\\Users\\lando\\IdeaProjects\\QuordleSolver\\src\\main\\resources\\words.txt")).readFileLines();

        this.removeWordsWithUnavailableChars(possibleWords, this.board.getUnavailableLetters());
        this.removeWordsWithoutGreenLetters(possibleWords, this.board);
        this.removeWordsAlreadyGuessed(possibleWords, this.board);

        System.out.println("Possible words in best board: " + possibleWords);
        return possibleWords.getFirst();
    }

    public void removeWordsWithUnavailableChars(LinkedList<String> words, ArrayList<String> unavailableLetters) {
        for(int i = 0; i < words.size(); i++) {
            String word = words.get(i).toLowerCase();

            for(String unavLetter : unavailableLetters) {
                if(word.contains(unavLetter.toLowerCase())) {
                    words.remove(word);
                    i--;
                    break;
                }
            }
        }
    }

    public void removeWordsWithoutGreenLetters(LinkedList<String> words, Board board) {
        for(int i = 0; i < words.size(); i++) {
            String word = words.get(i);
            char[] wordLetters = word.toUpperCase().toCharArray();
            char[] greenLetters = board.getMostKnowledgableGuess().toUpperCase().toCharArray();

            for (int j = 0; j < wordLetters.length; j++) {
                char wordLetter = wordLetters[j];
                char greenLetter = greenLetters[j];
                if(greenLetter == '_') continue;
                if(wordLetter != greenLetter) {
                    words.remove(word);
                    i--;
                    break;
                }
            }
        }
    }

    public void removeWordsAlreadyGuessed(LinkedList<String> words, Board board) {
        Board.refreshBoard(board);
        for(String guess : board.getAllFullGuesses()) {
            words.remove(guess.toLowerCase());
        }
    }
}
