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
        this.letter = this.getLetterFromCellXpath();
        this.color = this.letter.length() == 0 ? Color.GRAY : color;
        System.out.println(this.color);
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

    private String getLetterFromCellXpath() {
        String ariaLabel = Main.driver.findElement(By.xpath(this.getXpath())).getAttribute("aria-label");
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
