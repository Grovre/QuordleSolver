package me.grovre.board;

import me.grovre.Main;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class Row {

    private final ArrayList<Cell> cells;
    private final Board parentBoard;
    private final String xpath;
    private final String guess;
    private final String rawGuess;

    public Row(Board parentBoard, String xpath) {
        this.parentBoard = parentBoard;
        this.xpath = xpath;
        this.cells = Cell.generateCellsFromRow(this);

        StringBuilder s = new StringBuilder();
        for(String l : this.getRowLetters()) s.append(l);
        this.guess = s.toString();

        s = new StringBuilder();
        for(String l : this.getRawRowLetters()) s.append(l);
        this.rawGuess = s.toString();
    }

    public String getGuess() {
        return this.guess;
    }

    public static ArrayList<Row> generateRowsFromBoard(Board board) {
        ArrayList<Row> rows = new ArrayList<>(9);
        List<WebElement> rowElements = Main.driver.findElements(By.xpath(board.getXpath() + "//*[@role=\"row\"]"));

        System.out.println("Reading every cell on the boards...");
        for(int i = 0; i < rowElements.size(); i++) {
            Row r = new Row(
                    board,
                    board.getXpath() + ("//*[@role=\"row\"][%d]".formatted(i+1))
            );
            rows.add(r);
        }

        return rows;
    }

    public ArrayList<String> getRowLetters() {
        ArrayList<String> letters = new ArrayList<>(5);
        for(Cell cell : this.cells) {
            letters.add(cell.getLetter());
        }
        return letters;
    }

    public ArrayList<String> getRawRowLetters() {
        ArrayList<String> letters = new ArrayList<>(5);
        for(Cell cell : this.cells) {
            letters.add(cell.getRawLetter());
        }
        return letters;
    }

    public String getRawGuess() {
        return this.rawGuess;
    }

    public ArrayList<Cell> getCells() {
        return this.cells;
    }

    public Board getParentBoard() {
        return this.parentBoard;
    }

    public String getXpath() {
        return this.xpath;
    }
}
