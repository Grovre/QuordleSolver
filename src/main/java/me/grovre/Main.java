package me.grovre;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.managers.ChromeDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        WebDriverManager.chromedriver().setup();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter path of your Chrome or Chrome fork installation (including .exe): ");
        File binLocation = new File(scanner.nextLine());
        scanner.close();
        ChromeOptions co = new ChromeOptions();
        co.setBinary(binLocation);
        ChromeDriver driver = new ChromeDriver(co);

        driver.get("https://www.nytimes.com/games/wordle/index.html");
        WebElement closeHelpPopup;
        do {
            closeHelpPopup = driver.findElement(By.id("close"));
        } while(closeHelpPopup == null);
        closeHelpPopup.click();
    }
}
