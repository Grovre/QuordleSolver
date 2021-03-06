package me.grovre;

import io.github.bonigarcia.wdm.WebDriverManager;
import me.grovre.board.Board;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.util.Scanner;

public class Main {

    // TODO: 3/8/2022 Comment on things 

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

        GuessAnalyzer ga = new GuessAnalyzer();

        while(!Board.allBoardsComplete()) {
            ga.determineBestBoard();
            System.out.println("Working with board that has " + ga.getBoard().getRows().get(0).getGuess() + " on first row");
            kb.enterWord(ga.determineBestWord());
        }
    }
}
