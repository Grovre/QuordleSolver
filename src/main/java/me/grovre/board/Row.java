package me.grovre.board;

import me.grovre.Main;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class Row {

    private final ArrayList<Cell> cells;
    private final Board parentBoard;
    private final String xpath;
    private final String word;

    public Row(Board parentBoard, String xpath) {
        this.parentBoard = parentBoard;
        this.xpath = xpath;
        this.cells = Cell.generateCellsFromRow(this);
        this.word = this.getRowLettersAsString();
    }

    public static ArrayList<Row> generateRowsFromBoard(Board board) {
        ArrayList<Row> rows = new ArrayList<>(9);
        List<WebElement> rowElements = Main.driver.findElements(By.xpath(board.getXpath() + "//*[@role=\"row\"]"));

        for(int i = 0; i < rowElements.size(); i++) {
            Row r = new Row(
                    board,
                    board.getXpath() + ("//*[@role=\"row\"][%d]".formatted(i+1))
            );
            rows.add(r);
            System.out.println(r.xpath);
        }

        return rows;
    }

    public LinkedHashMap<String, Color> getRowLettersAndColors() {
        LinkedHashMap<String, Color> wordWithColors = new LinkedHashMap<>(5);
        for(Cell cell : this.cells) {
            wordWithColors.put(
                    cell.getLetter().toUpperCase(), cell.getColor()
            );
        }
        return wordWithColors;
    }

    public String getRowLettersAsString() {
        var map = this.getRowLettersAndColors();
        StringBuilder s = new StringBuilder();
        for(var entry : map.entrySet()) {
            String letter = entry.getKey();
            letter = entry.getValue() == Color.GRAY ? "_" : entry.getValue() == Color.YELLOW ? letter.toLowerCase() : letter.toUpperCase();
            s.append(letter);
        }
        return s.toString();
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

    public String getWord() {
        return this.word;
    }
}
