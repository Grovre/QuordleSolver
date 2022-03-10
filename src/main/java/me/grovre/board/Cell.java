package me.grovre.board;

import me.grovre.Main;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Cell {

    private final Row parentRow;
    private final String letter;
    private final Color color;
    private final String xpath;

    public Cell(Row parentRow, Color color, String xpath) {
        this.parentRow = parentRow;
        this.xpath = xpath;
        // TODO: 3/6/2022 Fix the getLetterFromCellXpath method, takes 17-19ms to finish and increases wait time by a lot
        var temp = this.getLetterFromCellXpath();
        this.color = temp.length() != 1 ? Color.GRAY : color;
        this.letter = color == Color.GRAY ? "_" : color == Color.YELLOW ?
                        this.getLetterFromCellXpath().toLowerCase() :
                        this.getLetterFromCellXpath().toUpperCase();
    }

    public static ArrayList<Cell> generateCellsFromRow(Row row) {
        List<WebElement> cellElements = Main.driver.findElements(By.xpath(row.getXpath() + "//*[@role=\"cell\"]"));
        ArrayList<Cell> cells = new ArrayList<>(cellElements.size());

        for (int i = 0; i < cellElements.size(); i++) {
            WebElement el = cellElements.get(i);
            List<String> splitLabel = Arrays.asList(el.getAttribute("aria-label").split(" "));
            Color color = splitLabel.contains("incorrect.") ? Color.GRAY : splitLabel.contains("different") ? Color.YELLOW : Color.GREEN;
            Cell c = new Cell(row, color, row.getXpath() + ("//*[@role=\"cell\"][%d]".formatted(i+1)));
            cells.add(c);
        }
        return cells;
    }

    // FIXME: 3/6/2022 ariaLabel assignment and el retrieval both take around 10ms to complete
    private String getLetterFromCellXpath() {
        WebElement el = Main.driver.findElement(By.xpath(this.getXpath()));
        String ariaLabel = el.getAttribute("aria-label");
        if(ariaLabel.split(" ")[0].equalsIgnoreCase("Blank")) return "";
        return ariaLabel.split("")[1];
    }

    private String getXpath() {
        return this.xpath;
    }

    public Row getParentRow() {
        return this.parentRow;
    }

    public String getLetter() {
        return this.letter;
    }

    public Color getColor() {
        return this.color;
    }
}
