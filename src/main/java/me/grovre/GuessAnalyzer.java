package me.grovre;

import me.grovre.board.Board;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class GuessAnalyzer {

    private Board board;
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

    public void setBoard(Board board) {
        this.board = board;
    }

    public Board determineBestBoard() {
        if(this.board == null) return Board.getNewBoards().get(0);
        if(!this.board.isComplete()) return this.board;

        Board.refreshAllBoards();
        Board bestBoard = Board.allBoards.stream()
                .filter(b -> !b.isComplete())
                .max(Comparator.comparingInt(Board::getScore))
                .orElseThrow();

        System.out.println("Best board: " + bestBoard + "(" + bestBoard.getRows().get(0).getGuess() + ")");
        return bestBoard;
    }

    public String determineBestWord() {
        if(this.board == null) return null;
        if(this.board.isComplete()) this.board = this.determineBestBoard();

        ArrayList<String> newPossibleAnswers;
        newPossibleAnswers = this.removeWordsWithoutGreenChars(this.possibleAnswers, this.board.getMostKnowledgableGuess());
        System.out.println(newPossibleAnswers);
        newPossibleAnswers = this.removeWordsWithUnavailableLetters(newPossibleAnswers, this.board.getUnavailableLetters());
        System.out.println(newPossibleAnswers);
        newPossibleAnswers = this.removeWordsWithoutYellowLetters(newPossibleAnswers, this.board.getYellowLetters());
        System.out.println(newPossibleAnswers);
        newPossibleAnswers = this.removeWordsAlreadyGuessed(newPossibleAnswers, this.board.getAllGuesses());
        System.out.println(newPossibleAnswers);

        // TODO: 3/9/2022 Weight the guess based on score of letter appearance

        return newPossibleAnswers.get(0);
    }

    // FIXME: 3/9/2022 Green char check removes all elements
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

    public ArrayList<String> removeWordsWithUnavailableLetters(ArrayList<String> words, ArrayList<String> unavLetters) {
        return words.stream()
                .filter(w -> {
            for(String l : unavLetters) {
                if(w.contains(l)) return false;
            }
            return true;
        }).collect(Collectors.toCollection(ArrayList<String>::new));
    }

    public ArrayList<String> removeWordsWithoutYellowLetters(ArrayList<String> words, ArrayList<String> yellowLetters) {
        return words.stream()
                .filter(w -> {
                    for(String l : yellowLetters) {
                        if(!w.contains(l)) return false;
                    }
                    return true;
                }).collect(Collectors.toCollection(ArrayList<String>::new));
    }

    public ArrayList<String> removeWordsAlreadyGuessed(ArrayList<String> words, ArrayList<String> alreadyGuessedWords) {
        ArrayList<String> newWords = new ArrayList<>();
        for(String w : words) {
            if(!alreadyGuessedWords.contains(w)) newWords.add(w);
        }
        return newWords;
    }
}
