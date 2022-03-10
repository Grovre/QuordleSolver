package me.grovre;

import me.grovre.board.Board;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class GuessAnalyzer {

    private Board board;
    private ArrayList<String> possibleAnswers;

    public GuessAnalyzer() {
        this.possibleAnswers = new FileUtil(
                new File("C:\\Users\\lando\\IdeaProjects\\QuordleSolver\\src\\main\\resources\\words.txt")
        ).readFileLines();

        this.board = this.determineBestBoard();
        assert this.board != null;
    }

    public void refreshPossibleAnswers() {
        this.possibleAnswers = new FileUtil(
                new File("C:\\Users\\lando\\IdeaProjects\\QuordleSolver\\src\\main\\resources\\words.txt")
        ).readFileLines();
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
        if(this.board.isComplete()) {
            this.board = this.determineBestBoard();
            this.refreshPossibleAnswers();
        }
        this.board.refreshBoard();

        ArrayList<String> newPossibleAnswers;
        newPossibleAnswers = this.removeWordsWithoutGreenChars(this.possibleAnswers, this.board.getMostKnowledgableGuess());
        System.out.println(newPossibleAnswers);
        newPossibleAnswers = this.removeWordsWithUnavailableLetters(newPossibleAnswers, this.board.getUnavailableLetters());
        System.out.println(newPossibleAnswers);
        newPossibleAnswers = this.removeWordsWithoutYellowLetters(newPossibleAnswers, this.board.getYellowLetters());
        System.out.println(newPossibleAnswers);

        // FIXME: 3/10/2022 Remove words that have more or less letters than the requried amount
        // newPossibleAnswers = this.removeWordsAlreadyGuessed(newPossibleAnswers, this.board.getAllRawGuesses());
        // System.out.println(newPossibleAnswers);

        // TODO: 3/9/2022 Weight the guess based on score of letter appearance

        this.possibleAnswers = newPossibleAnswers;
        return newPossibleAnswers.get(0);
    }

    public ArrayList<String> removeWordsWithoutGreenChars(ArrayList<String> words, String formattedWord) {
        ArrayList<String> passingWords = new ArrayList<>();

        char[] formattedWordChars = formattedWord.toCharArray();
        for (String word : words) {
            boolean flag = true;
            char[] wordChars = word.toCharArray();

            for (int letterLoc = 0; letterLoc < wordChars.length; letterLoc++) {
                char formattedWordChar = formattedWordChars[letterLoc];
                char possibleWordChar = wordChars[letterLoc];
                if (!Character.isUpperCase(formattedWordChar)) continue;
                if (Character.toUpperCase(possibleWordChar) != formattedWordChar) {
                    flag = false;
                    break;
                }
            }

            if (flag) passingWords.add(word);
        }

        return passingWords;
    }

    public ArrayList<String> removeWordsWithUnavailableLetters(ArrayList<String> words, ArrayList<String> unavLetters) {
        ArrayList<String> newWords = new ArrayList<>();
        for(String w : words) {
            boolean flag = true;

            for(String l : unavLetters) {
                if(w.toLowerCase().contains(l.toLowerCase()) && l.length() != 0) {
                    flag = false;
                    break;
                }
            }

            if(flag) newWords.add(w);
        }

        return newWords;
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
