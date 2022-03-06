package me.grovre;

import org.openqa.selenium.chrome.ChromeDriver;

import java.util.HashMap;

public class GameFlow {

    private final HashMap<String, String> usedWords = new HashMap<>();
    private final ChromeDriver driver;

    public GameFlow(ChromeDriver driver) {
        this.driver = driver;
    }


}
