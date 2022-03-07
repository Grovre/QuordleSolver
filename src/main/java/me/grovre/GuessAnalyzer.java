package me.grovre;

import me.grovre.board.Board;
import me.grovre.board.Row;

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
        LinkedList<String> possibleWords = new FileUtil(new File("C:\\Users\\lando\\IdeaProjects\\QuordleSolver\\src\\main\\resources\\words.txt")).readFileLines();
        this.removeWordsWithChar(possibleWords, this.board.getUnavailableLetters().toArray(String[]::new));
        System.out.println("Possible words without excluded chars: " + possibleWords);
        this.removeWordsWithoutMatchedGreenLetters(possibleWords, this.board);
        System.out.println("Possible words with green letters: " + possibleWords);
        this.removeWordsWithoutYellowChars(possibleWords, this.board);
        System.out.println("Possible words in best board: " + possibleWords);
        return possibleWords.getFirst();
    }

    public void removeWordsWithoutYellowChars(LinkedList<String> words, Board board) {
        ArrayList<String> yellowChars = board.getYellowLetters();
        for(int i = 0; i < words.size(); i++) {
            String word = words.get(i).toLowerCase();
            System.out.println(word + ", " + i);
            for(String yellowChar : yellowChars) {
                if(!word.contains(yellowChar.toLowerCase())) {
                    words.remove(word);
                    i--;
                }
            }
        }
    }

    public void removeWordsWithoutMatchedGreenLetters(LinkedList<String> words, Board board) {
        ArrayList<String> allGuesses = new ArrayList<>();
        for(Row row : board.getRows()) {
            if(row.getWord().length() == 0) continue;
            allGuesses.add(row.getWord());
        }

        for(int i = 0; i < words.size(); i++) {
            if(i < 0) i = 0;
            String word = words.get(i);
            for(String guess : allGuesses) {
                char[] wordChars = word.toCharArray();
                char[] guessChars = guess.toCharArray();
                for(int j = 0; j < guessChars.length; j++) {
                    if(!Character.isUpperCase(guessChars[j])) continue;
                    if(Character.toUpperCase(wordChars[j]) != guessChars[j]) {
                        words.remove(word);
                        i--;
                        break;
                    }
                }
            }
        }
    }
    
    public void removeWordsWithChar(LinkedList<String> words, String... unavailableChars) {
        for(int i = 0; i < words.size(); i++) {
            String word = words.get(i).toLowerCase();
            System.out.println(word + ", " + i);
            for(String s : unavailableChars) {
                char c = s.toLowerCase().charAt(0);
                if(word.contains(Character.toString(c).toLowerCase())) {
                    words.remove(i);
                    i--;
                    break;
                }
            }
        }
    }
}
