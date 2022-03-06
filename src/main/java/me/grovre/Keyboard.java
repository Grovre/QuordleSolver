package me.grovre;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

public class Keyboard {

    private final HashMap<String, WebElement> letterElements;
    private final WebElement enterKey;
    private WebElement backspaceKey;

    public Keyboard() {
        this.letterElements = new HashMap<>(26);
        System.out.println("All found usable letters: ");
        System.out.println(this.verifyKeys());
        System.out.println(this.letterElements);
        this.enterKey = Main.driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[3]/div/div[3]/button[9]/div"));
    }

    public boolean verifyKeys() {
        List<WebElement> quordleBoxContents = Main.driver.findElements(By.className("quordle-box-content"));
        ArrayList<String> charsNotCounted = new ArrayList<>(IntStream.range(Character.codePointAt("A", 0), Character.codePointAt("Z", 0) + 1).mapToObj(n -> new String(String.valueOf((char) n))).toList());
        System.out.println(charsNotCounted);
        for(WebElement el : quordleBoxContents) {
            String elementText = el.getText();
            if(charsNotCounted.remove(elementText)) this.letterElements.put(elementText, el);
            if(charsNotCounted.size() <= 0) return true;
        }
        return false;
    }

    public boolean enterKey(String key) {
        if(key.length() != 1) {
            System.out.println("Entered key MUST be 1 character length! " +
                    "Use method 'enterWord' to type 5 characters at once instead.");
            return false;
        }
        WebElement elementToPush = this.letterElements.get(key.toUpperCase());
        System.out.println("Typing " + key.toUpperCase());
        elementToPush.click();
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean enterWord(String word) {
        if(word.length() != 5) {
            System.out.println("Entered key MUST be 5 characters long!");
            return false;
        }
        for(String key : word.split("")) {
            if(!this.enterKey(key)) return false;
        }
        this.pressEnter();
        return true;
    }

    public void pressEnter() {
        this.enterKey.click();
    }
}
