package me.grovre.board;

import me.grovre.Main;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.HashMap;

public class Board {

    public static ArrayList<Board> allBoards = new ArrayList<>(4);
    private ArrayList<Row> rows;
    private final String xpath;
    private final WebElement element;
    private int score;
    private boolean isComplete;
    private String constructedGreenGuess;

    public Board(WebElement boardElement, String xpath) {
        this.xpath = xpath;
        this.element = boardElement;
        this.rows = Row.generateRowsFromBoard(this);
        this.score = this.generateScore();
        this.isComplete = false;
    }

    public String getMostKnowledgableGuess() {
        ArrayList<String> allGuesses = this.getAllGuesses();
        char[] knowledgableGuess = {'_', '_', '_', '_', '_'};
        for(String guess : allGuesses) {
            char[] splitGuess = guess.toCharArray();

            for (int i = 0; i < splitGuess.length; i++) {
                char letter = splitGuess[i];
                if (Character.isUpperCase(letter)) {
                    knowledgableGuess[i] = letter;
                }
            }
        }

        this.constructedGreenGuess = new String(knowledgableGuess);
        return this.constructedGreenGuess;
    }

    public ArrayList<String> getAllGuesses() {
        ArrayList<String> allGuesses = new ArrayList<>();
        for(Row row : this.rows) {
            if(row.getGuess().length() == 5) {
                allGuesses.add(row.getGuess());
            }
        }
        return allGuesses;
    }

    public boolean isComplete() {
        return this.isComplete;
    }

    public void checkIfBoardComplete() {
        for(Row row : this.rows) {
            StringBuilder s = new StringBuilder();
            for(Cell cell : row.getCells()) {
                if(cell.getColor() == Color.GRAY) {
                    s.append("_");
                } else if(cell.getColor() == Color.YELLOW) {
                    s.append(cell.getLetter().toLowerCase());
                } else {
                    s.append(cell.getLetter().toUpperCase());
                }
            }

            boolean flag = false;
            for(char letter : s.toString().toCharArray()) {
                if(!Character.isUpperCase(letter)) {
                    flag = true;
                    break;
                }
            }
            if(flag) continue;
            this.isComplete = true;
            return;
        }
        this.isComplete = false;
    }

    public void refreshBoard() {
        this.rows = Row.generateRowsFromBoard(this);
        this.constructedGreenGuess = this.getMostKnowledgableGuess();
        this.checkIfBoardComplete();
    }

    public static void refreshBoard(Board board) {
        board.rows = Row.generateRowsFromBoard(board);
        board.constructedGreenGuess = board.getMostKnowledgableGuess();
        board.checkIfBoardComplete();
        board.generateScore();
    }

    public static void refreshAllBoards() {
        if(Board.allBoards.size() < 4) {
            Board.getNewBoards();
            return;
        }
        for(Board board : allBoards) {
            refreshBoard(board);
        }
    }

    public ArrayList<Cell> getAllCellsOnBoard() {
        ArrayList<Cell> cells = new ArrayList<>();
        for(Row r : this.rows) cells.addAll(r.getCells());
        return cells;
    }

    public int generateScore() {
        this.score = 0;
        for(Row row : this.rows) {
            for(Cell cell : row.getCells()) {
                Color color = cell.getColor();
                if(color == Color.YELLOW) {
                    this.score += 3;
                } else if(color == Color.GREEN) {
                    this.score += 4;
                }
            }

        }
        return this.score;
    }

    public static boolean allBoardsComplete() {
        for(Board b : allBoards) {
            if(!b.isComplete()) return false;
        }
        return true;
    }

    // TODO: 3/6/2022 Call this method less 
    public static ArrayList<Board> getNewBoards() {
        HashMap<WebElement, String> boardElements = new HashMap<>(4);
        boardElements.put(Main.driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div/div[1]/div[1]")),
                "//*[@id=\"root\"]/div/div[2]/div/div[1]/div[1]");
        boardElements.put(Main.driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div/div[1]/div[2]")),
                "//*[@id=\"root\"]/div/div[2]/div/div[1]/div[2]");
        boardElements.put(Main.driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div/div[2]/div[1]")),
                "//*[@id=\"root\"]/div/div[2]/div/div[2]/div[1]");
        boardElements.put(Main.driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div/div[2]/div[2]")),
                "//*[@id=\"root\"]/div/div[2]/div/div[2]/div[2]");

        ArrayList<Board> boards = new ArrayList<>(boardElements.size());

        for(WebElement k : boardElements.keySet()) {
            Board b = new Board(k, boardElements.get(k));
            boards.add(b);
        }
        allBoards = boards;
        return boards;
    }

    public ArrayList<Row> getRows() {
        return this.rows;
    }

    public String getXpath() {
        return this.xpath;
    }

    public WebElement getElement() {
        return this.element;
    }

    public int getScore() {
        return this.score;
    }

    public ArrayList<String> getUnavailableLetters() {
        ArrayList<String> unavailableLetters = new ArrayList<>();
        for(Row row : this.rows) {
            for(Cell cell : row.getCells()) {
                if(cell.getColor() == Color.GRAY) {
                    unavailableLetters.add(cell.getLetter().toUpperCase());
                }
            }
        }

        return unavailableLetters;
    }

    public ArrayList<String> getYellowLetters() {
        ArrayList<String> yellowLetters = new ArrayList<>();
        for(Row row : this.rows) {
            char[] rowWordAsCharArray = row.getGuess().toCharArray();
            for(char c : rowWordAsCharArray) {
                if(Character.isLowerCase(c)) {
                    yellowLetters.add(Character.toString(c));
                }
            }
        }

        return yellowLetters;
    }
}
