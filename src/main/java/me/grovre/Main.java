package me.grovre;

import io.github.bonigarcia.wdm.WebDriverManager;
import me.grovre.board.Board;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
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

        Board.allBoards = Board.getAllBoards();

        GuessAnalyzer ga = new GuessAnalyzer();
        for(int i = 0; i < 8; i++) {
            Board.refreshBoard(ga.getBoard());
            if(ga.getBoard().isComplete()) ga.determineBestBoard();
            String bestGuess = ga.determineBestWord();
            kb.enterWord(bestGuess);
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
