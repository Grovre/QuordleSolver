package me.grovre;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.managers.ChromeDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static int attempts = 1;
    public static void main(String[] args) {
        WebDriverManager.chromedriver().setup();
        
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter path of your Chrome or Chrome fork installation (including .exe): ");
        //File binLocation = new File(scanner.nextLine());
        File binLocation = new File("C:\\Program Files\\BraveSoftware\\Brave-Browser\\Application\\brave.exe");
        
        scanner.close();
        ChromeOptions co = new ChromeOptions();
        co.setBinary(binLocation);
        ChromeDriver driver = new ChromeDriver(co);

        driver.get("https://www.nytimes.com/games/wordle/index.html");
        AtomicInteger attempts = new AtomicInteger(0);
        closeHelpPopup(driver);
        System.out.println("Closed help popup");
    }

    public static void closeHelpPopup(ChromeDriver driver) {
        try {
            WebElement helpPopupExitButton = driver.findElement(By.xpath("//*[@id=\"game\"]/game-modal//div/div/div/game-icon//svg"));
            helpPopupExitButton.click();
        } catch (NoSuchElementException e) {
            System.out.printf("Couldn't close help popup on try #%,d%n", attempts);
            attempts++;
            closeHelpPopup(driver);
            attempts = 0;
        }
    }
}
