package org.example;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class RemoteChromeControl {
    private static final Logger LOGGER = Logger.getLogger(RemoteChromeControl.class.getName());

    static {
        try {
            FileHandler fileHandler = new FileHandler("RemoteChromeControl.log", true);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            LOGGER.addHandler(fileHandler);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error setting up log file", e);
        }
    }

    private static void randomWait(WebDriver driver) {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        LOGGER.info("Waiting for the page to load completely");
        wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete'"));
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(3000, 7000)); // randomly wait between 3 and 7 seconds
        } catch (InterruptedException e) {
            LOGGER.log(Level.WARNING, "Thread interrupted", e);
        }
    }

    public static void main(String[] args) {
        String os = System.getProperty("os.name").toLowerCase();
        String chromeDriverPath;

        if (os.contains("win")) {
            chromeDriverPath = "windows/chromedriver.exe";
        } else if (os.contains("linux")) {
            chromeDriverPath = "chromedriver_linux64/chromedriver";
        } else {
            throw new RuntimeException("Unsupported OS: " + os);
        }

        // Get login credentials from user
        GUI login = new GUI();
        String[] credentials = login.getLogin();
        String email = credentials[0];
        String password = credentials[1];
        LOGGER.info("Starting Chrome driver");
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);

        ChromeOptions options = new ChromeOptions();
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_setting_values.notifications", 2); //Page notifications: 1-Allow, 2-Block, 0-default
        options.setExperimentalOption("prefs", prefs);
        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        LOGGER.info("Navigating to Facebook login page");
        driver.get("https://www.facebook.com");
        // Wait until the page is fully loaded
        randomWait(driver);
        LOGGER.info("Attempting to accept cookies");
        driver.manage().addCookie(new Cookie("cookiesAgreed", "true"));
        // Wait for the cookies popup to become visible
        LOGGER.info("Waiting for the cookies popup to become visible");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(., 'alla cookies') or contains(., 'all cookies')]")));
        // Click the cookie button
        LOGGER.info("Clicking the 'Accept' button in the cookies popup");
        driver.findElement(By.xpath("//button[contains(., 'alla cookies') or contains(., 'all cookies')]")).click();
        randomWait(driver);
        // Find the email and password fields and fill in the values
        driver.findElement(By.id("email")).sendKeys(email);
        driver.findElement(By.id("pass")).sendKeys(password);
        randomWait(driver);
        // Click the login
        driver.findElement(By.name("login")).click();
        LOGGER.info("Logged in");
        randomWait(driver);
        WebElement element = driver.findElement(By.xpath("/html/body/div[1]/div/div[1]/div/div[3]/div/div/div/div[1]/div[1]/div/div[2]/div/div/div/div[3]/div/div[2]/div/div/div/div[1]/div/div[1]/span"));

        // Klicka på elementet
        element.click();
        randomWait(driver);
        element = driver.switchTo().activeElement();
        element.sendKeys("Hej");
        randomWait(driver);

        driver.findElement(By.xpath("//span[text()='Publicera'] | //span[text()='Publish']")).click();
        randomWait(driver);
        driver.findElement(By.cssSelector("svg[aria-label='Din profil']")).click();

    randomWait(driver);

        driver.findElement(By.xpath("//*[contains(text(),'Logga ut')][1]")).click();

     /*   element = driver.findElement(By.xpath("/html/body/div[1]/div/div[1]/div/form"));
        element.click();*/
       // LOGGER.info('Post published successfully!')
        //driver.quit();
    }
}
