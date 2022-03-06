package me.grovre;

import io.github.bonigarcia.wdm.WebDriverManager;
import me.grovre.board.Board;
import me.grovre.board.Cell;
import me.grovre.board.Row;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static ChromeDriver driver;

    public static void main(String[] args) {
        WebDriverManager.chromedriver().setup();
        
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter path of your Chrome or Chrome fork installation (including .exe): ");
        //File binLocation = new File(scanner.nextLine());
        File binLocation = new File("C:\\Program Files\\BraveSoftware\\Brave-Browser\\Application\\brave.exe");
        scanner.close();

        ChromeOptions co = new ChromeOptions();
        co.setBinary(binLocation);
        driver = new ChromeDriver(co);

        driver.get("https://www.quordle.com/#/");
        driver.manage().window().maximize();

        Keyboard kb = new Keyboard();

        kb.enterWord("Audio");
        kb.enterWord("Slate");
        kb.enterWord("Later");
        kb.enterWord("Crane");

        ArrayList<Board> boards = Board.getAllBoards();
        for(Board board : boards) {
            for(Row row : board.getRows()) {
                for(Cell cell : row.getCells()) {
                    if(cell.getLetter().length() == 0) continue;
                    System.out.printf("Letter: %s, Color: %s%n", cell.getLetter(), cell.getColor().toString());
                }
            }
        }
    }
}
