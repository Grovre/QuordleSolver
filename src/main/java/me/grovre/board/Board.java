package me.grovre.board;

import me.grovre.Main;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.HashMap;

public class Board {

    private ArrayList<Row> rows;
    private final String xpath;
    private final WebElement element;
    private int score;

    public Board(WebElement boardElement, String xpath) {
        this.xpath = xpath;
        this.element = boardElement;
        this.rows = Row.generateRowsFromBoard(this);
        this.generateScore();
    }

    public void refreshBoard() {
        this.rows = Row.generateRowsFromBoard(this);
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

    public void generateScore() {
        this.score = 0;
        for(Color c : this.letterColorFrequency().values()) {
            if(c == Color.YELLOW) {
                this.score += 1;
            } else if(c == Color.GREEN) {
                this.score += 2;
            }
        }
    }

    public static ArrayList<Board> getAllBoards() {
        HashMap<WebElement, String> boardElements = new HashMap<>(4);
        boardElements.put(Main.driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div/div[1]/div[1]")), "//*[@id=\"root\"]/div/div[2]/div/div[1]/div[1]");
        boardElements.put(Main.driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div/div[1]/div[2]")), "//*[@id=\"root\"]/div/div[2]/div/div[1]/div[2]");
        boardElements.put(Main.driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div/div[2]/div[1]")), "//*[@id=\"root\"]/div/div[2]/div/div[2]/div[1]");
        boardElements.put(Main.driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div/div[2]/div[2]")), "//*[@id=\"root\"]/div/div[2]/div/div[2]/div[2]");

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
}
