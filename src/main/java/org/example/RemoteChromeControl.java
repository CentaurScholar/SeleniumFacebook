package org.example;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.concurrent.ThreadLocalRandom;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

public class RemoteChromeControl {
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(RemoteChromeControl.class);

    private static void randomWait(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete'"));
        try { Thread.sleep(ThreadLocalRandom.current().nextInt(3000, 7000)); } catch (InterruptedException e) { LOGGER.warn("Thread interrupted", e); }
    }

    public static void main(String[] args) {
        String os = System.getProperty("os.name").toLowerCase();
        String chromeDriverPath = os.contains("win") ? "windows/chromedriver.exe" : os.contains("linux") ? "chromedriver_linux64/chromedriver" : null;
        if (chromeDriverPath == null) throw new RuntimeException("Unsupported OS: " + os);

        GUI login = new GUI();
        String[] credentials = login.getLogin();

        LOGGER.info("Starting Chrome driver");
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);

        ChromeOptions options = new ChromeOptions();
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_setting_values.notifications", 2);
        options.setExperimentalOption("prefs", prefs);
        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        LOGGER.info("Navigating to Facebook login page");
        driver.get("https://www.facebook.com");
        randomWait(driver);

        LOGGER.info("Attempting to accept cookies");
        driver.manage().addCookie(new Cookie("cookiesAgreed", "true"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(., 'alla cookies') or contains(., 'all cookies')]")));

        LOGGER.info("Clicking the 'Accept' button in the cookies popup");
        driver.findElement(By.xpath("//button[contains(., 'alla cookies') or contains(., 'all cookies')]")).click();
        randomWait(driver);

        driver.findElement(By.id("email")).sendKeys(credentials[0]);
        driver.findElement(By.id("pass")).sendKeys(credentials[1]);
        randomWait(driver);

        LOGGER.info("Logging in");
        driver.findElement(By.name("login")).click();
        randomWait(driver);

        WebElement element = driver.findElement(By.xpath("/html/body/div[1]/div/div[1]/div/div[3]/div/div/div/div[1]/div[1]/div/div[2]/div/div/div/div[3]/div/div[2]/div/div/div/div[1]/div/div[1]/span"));
        element.click();
        randomWait(driver);

        element = driver.switchTo().activeElement();
        element.sendKeys("Barabass och rakapparat?");
        randomWait(driver);

        LOGGER.info("Publishing post");
        driver.findElement(By.xpath("//span[text()='Publicera'] | //span[text()='Post']")).click();
        randomWait(driver);

        LOGGER.info("Navigating to profile");
        driver.findElement(By.cssSelector("svg[aria-label='Din profil'], svg[aria-label='Your profile']")).click();
        randomWait(driver);

        LOGGER.info("Logging out");
        driver.findElement(By.xpath("//*[contains(text(),'Logga ut') or contains(text(),'Log Out')][1]")).click();
    }
}
