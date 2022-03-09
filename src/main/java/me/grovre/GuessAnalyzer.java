package me.grovre;

import me.grovre.board.Board;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;

public class GuessAnalyzer {

    private final Board board;
    private final ArrayList<String> possibleAnswers;

    public GuessAnalyzer() {
        this.possibleAnswers = new FileUtil(
                new File("C:\\Users\\lando\\IdeaProjects\\QuordleSolver\\src\\main\\resources\\words.txt")
        ).readFileLines();

        this.board = this.determineBestBoard();
        assert this.board != null;
    }

    public Board getBoard() {
        return this.board;
    }

    public Board determineBestBoard() {
        if(this.board == null) return Board.getNewBoards().get(0);
        if(!this.board.isComplete()) return this.board;

        Board.refreshAllBoards();
        Board bestBoard = Board.allBoards.stream()
                .max(Comparator.comparingInt(Board::getScore))
                .orElseThrow();

        System.out.println("Best board: " + bestBoard + "(" + bestBoard.getRows().get(0).getFullGuess() + ")");
        return bestBoard;
    }

    public ArrayList<String> removeWordsWithoutGreenChars(ArrayList<String> words, String formattedWord) {
        ArrayList<String> passingWords = new ArrayList<>();
        for(String word : words) {
            char[] wordChars = word.toUpperCase().toCharArray();
            char[] formattedWordChars = formattedWord.toCharArray();

            boolean matchFlag = true;
            for (int j = 0; j < formattedWordChars.length; j++) {
                char formattedWordChar = formattedWordChars[j];
                char wordChar = wordChars[j];
                if (!Character.isUpperCase(formattedWordChar)) continue;
                if (wordChar != formattedWordChar) {
                    matchFlag = false;
                    break;
                }
            }

            if (matchFlag) passingWords.add(word);
        }

        return passingWords;
    }

    public ArrayList<String> removeWordswithoutGreenChars(ArrayList<String> words, Board board) {
        return this.removeWordsWithoutGreenChars(this.possibleAnswers, board.getMostKnowledgableGuess());
    }
}
