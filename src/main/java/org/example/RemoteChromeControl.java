package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class RemoteChromeControl {

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

        System.setProperty("webdriver.chrome.driver", chromeDriverPath);

        ChromeOptions options = new ChromeOptions();
        Map prefs=new HashMap();
        prefs.put("profile.default_content_setting_values.notifications", 2); //Page notifications: 1-Allow, 2-Block, 0-default
        options.setExperimentalOption("prefs",prefs);
        WebDriver driver = new ChromeDriver(options);

        driver.get("https://www.facebook.com");
        // Wait until the page is fully loaded
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete'"));
        driver.manage().addCookie(new Cookie("cookiesAgreed", "true"));
        // Wait for the cookies popup to become visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/div[2]/div/div/div/div/div[3]/button[2]")));


        // Click the cookie button
        driver.findElement(By.cssSelector("button[data-cookiebanner='accept_button']")).click();


        // Find the email and password fields and fill in the values
        driver.findElement(By.id("email")).sendKeys(email);
        driver.findElement(By.id("pass")).sendKeys(password);

        // Click the login
       driver.findElement(By.name("login")).click();
    }
}
