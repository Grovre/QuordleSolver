package me.grovre.board;

import me.grovre.Main;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Board {

    public static ArrayList<Board> allBoards = new ArrayList<>(4);
    private ArrayList<Row> rows;
    private final String xpath;
    private final WebElement element;
    private int score;
    private boolean isComplete;

    public Board(WebElement boardElement, String xpath) {
        this.xpath = xpath;
        this.element = boardElement;
        this.rows = Row.generateRowsFromBoard(this);
        this.score = this.generateScore();
        this.isComplete = this.checkIfBoardComplete();
    }

    public boolean isComplete() {
        return this.isComplete;
    }

    public boolean checkIfBoardComplete() {
        for(Row row : this.rows) {
            for(char letter : row.getWord().toCharArray()) {
                if(!Character.isUpperCase(letter)) {
                    this.isComplete = false;
                    return false;
                }
            }
        }
        this.isComplete = true;
        return true;
    }

    public static void refreshBoard(Board board) {
        board.rows = Row.generateRowsFromBoard(board);
    }

    public ArrayList<Cell> getAllCellsOnBoard() {
        ArrayList<Cell> cells = new ArrayList<>();
        for(Row r : this.rows) cells.addAll(r.getCells());
        return cells;
    }

    public HashMap<String, Color> letterColorFrequency() {
        ArrayList<Cell> cells = this.getAllCellsOnBoard();
        HashMap<String, Color> letterColorMap = new HashMap<>(cells.size());
        for(Cell cell : cells) letterColorMap.put(cell.getLetter(), cell.getColor());
        return letterColorMap;
    }

    public int generateScore() {
        this.score = 0;
        for(Color c : this.letterColorFrequency().values()) {
            if(c == Color.YELLOW) {
                this.score += 3;
            } else if(c == Color.GREEN) {
                this.score += 4;
            }
        }
        return this.score;
    }

    // TODO: 3/6/2022 Call this method less 
    public static ArrayList<Board> getAllBoards() {
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
            for(Map.Entry<String, Color> letter : row.getRowLettersAndColors().entrySet()) {
                if(letter.getValue() == Color.GRAY && letter.getKey().length() != 0) unavailableLetters.add(letter.getKey().toUpperCase());
            }
        }
        return unavailableLetters;
    }

    public ArrayList<String> getYellowLetters() {
        ArrayList<String> yellowLetters = new ArrayList<>();
        for(Row row : this.rows) {
            char[] rowWordAsCharArray = row.getWord().toCharArray();
            for(char c : rowWordAsCharArray) {
                if(Character.isLowerCase(c)) {
                    yellowLetters.add(Character.toString(c));
                }
            }
        }

        return yellowLetters;
    }
}
