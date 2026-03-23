package manager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.events.WebDriverListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import utils.WDListener;

import java.time.Duration;

public class AppManager {
    private WebDriver driver;
    Logger logger = LoggerFactory.getLogger(AppManager.class);
    static String browser = System.getProperty("browser", "chrome");

    public WebDriver getDriver() {
        return driver;
    }

    @BeforeMethod(alwaysRun = true)
    public void setup() {
        //driver = new ChromeDriver();
        switch (browser.toLowerCase()){
            case "firefox":
                driver = new FirefoxDriver();
                System.out.println("Use FireFox");
                break;
            case "edge":
                driver = new EdgeDriver();
                System.out.println("Use Edge");
                break;
            case "chrome":
                driver = new ChromeDriver();
                System.out.println("Use Chrome");
                break;
        }
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
        WebDriverListener webDriverListener = new WDListener();
        driver = new EventFiringDecorator<>(webDriverListener).decorate(driver);
        //driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    // (@BeforeMethod) setup --> (@Test) testName -->(@AfterMethod) tearDown

    @AfterMethod(enabled = true, alwaysRun = true)
    public void tearDown() {
        if (driver != null)
            driver.quit();
    }
}