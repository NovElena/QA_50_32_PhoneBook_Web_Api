package manager;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.events.WebDriverListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import utils.WDListener;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static utils.PropertiesReader.getProperty;

public class AppManager {
    @Getter
    private WebDriver driver;
    private final Logger logger = LoggerFactory.getLogger(AppManager.class);
    private static final String browser = System.getProperty("browser", "chrome");

    public void takeScreenshot(String methodName) {
        if (getDriver() == null) {
            return;
        }

        File screenshot = new File("build/reports/tests/smoke_tests/screenshots/screenshot-" + methodName + ".png");
        File parentDir = screenshot.getParentFile();
        if (parentDir != null && !parentDir.exists() && !parentDir.mkdirs()) {
            logger.warn("Could not create directory: {}", parentDir.getAbsolutePath());
        }

        File tmp = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
        try {
            java.nio.file.Files.copy(tmp.toPath(), screenshot.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            logger.info("Screenshot saved to: {}", screenshot.getAbsolutePath());
        } catch (IOException e) {
            logger.error("Failed to save screenshot: {}", e.getMessage());
        }
    }

    @BeforeMethod(alwaysRun = true)
    public void setup() {
        String targetUrl = getProperty("base.properties", "baseUrl");
        if (targetUrl == null) {
            logger.error("URL is null! Check base.properties file.");
            throw new RuntimeException("Target baseUrl from base.properties is null");
        }

        switch (browser.toLowerCase()) {
            case "firefox":
                driver = new FirefoxDriver();
                break;
            case "edge":
                driver = new EdgeDriver();
                break;
            case "chrome":
            default:
                ChromeOptions options = new ChromeOptions();
                if (System.getenv("GITHUB_ACTIONS") != null) {
                    options.addArguments("--headless=new");
                    options.addArguments("--no-sandbox");
                    options.addArguments("--disable-dev-shm-usage");
                    options.addArguments("--window-size=1920,1080");
                    options.addArguments("--disable-gpu");
                    options.addArguments("--remote-allow-origins=*");
                }
                driver = createChromeDriver(options);
                break;
        }

        WebDriverListener webDriverListener = new WDListener();
        driver = new EventFiringDecorator<>(webDriverListener).decorate(driver);
        driver.manage().window().maximize();
        openTargetUrl(targetUrl);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private WebDriver createChromeDriver(ChromeOptions options) {
        try {
            SeleniumResolution resolution = resolveChromeDriver();
            options.setBinary(resolution.browserPath());

            File driverExecutable = new File(resolution.driverPath());
            ChromeDriverService service = new ChromeDriverService.Builder()
                    .usingDriverExecutable(driverExecutable)
                    .usingAnyFreePort()
                    .withTimeout(Duration.ofSeconds(20))
                    .build();
            return new ChromeDriver(service, options);
        } catch (IOException e) {
            throw new IllegalStateException("Could not start ChromeDriver service.", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("ChromeDriver resolution was interrupted.", e);
        }
    }

    private SeleniumResolution resolveChromeDriver() throws IOException, InterruptedException {
        JsonObject browserResolution = runSeleniumManager(List.of("--browser", "chrome", "--output", "JSON"));
        JsonObject browserResult = browserResolution.getAsJsonObject("result");
        String browserPath = browserResult.get("browser_path").getAsString();
        String browserMajorVersion = detectChromeMajorVersion(browserPath);

        JsonObject driverResolution = runSeleniumManager(List.of(
                "--browser", "chrome",
                "--browser-version", browserMajorVersion,
                "--output", "JSON"
        ));
        JsonObject driverResult = driverResolution.getAsJsonObject("result");
        String driverPath = driverResult.get("driver_path").getAsString();

        return new SeleniumResolution(driverPath, browserPath);
    }

    private JsonObject runSeleniumManager(List<String> arguments) throws IOException, InterruptedException {
        String managerPath = findSeleniumManagerPath();
        List<String> command = new java.util.ArrayList<>();
        command.add(managerPath);
        command.addAll(arguments);

        Process process = new ProcessBuilder(command)
                .redirectErrorStream(true)
                .start();
        String output = new String(process.getInputStream().readAllBytes());
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new IllegalStateException("Selenium Manager failed: " + output);
        }
        return JsonParser.parseString(output).getAsJsonObject();
    }

    private String detectChromeMajorVersion(String browserPath) throws IOException, InterruptedException {
        Process process = new ProcessBuilder(browserPath, "--version")
                .redirectErrorStream(true)
                .start();
        String versionOutput = new String(process.getInputStream().readAllBytes()).trim();
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new IllegalStateException("Could not detect Chrome version from: " + browserPath);
        }

        Matcher matcher = Pattern.compile("(\\d+)\\.").matcher(versionOutput);
        if (matcher.find()) {
            return matcher.group(1);
        }

        return readChromeMajorVersionFromMetadata();
    }

    private String readChromeMajorVersionFromMetadata() throws IOException {
        File metadataFile = new File(System.getProperty("user.home"), ".cache/selenium/se-metadata.json");
        if (!metadataFile.isFile()) {
            throw new IllegalStateException("Could not parse Chrome version from browser output and metadata file was not found.");
        }

        String metadata = java.nio.file.Files.readString(metadataFile.toPath());
        JsonObject metadataJson = JsonParser.parseString(metadata).getAsJsonObject();
        var drivers = metadataJson.getAsJsonArray("drivers");
        if (drivers == null || drivers.size() == 0) {
            throw new IllegalStateException("Could not parse Chrome version from browser output and Selenium metadata is empty.");
        }

        JsonObject latestDriver = drivers.get(drivers.size() - 1).getAsJsonObject();
        return latestDriver.get("major_browser_version").getAsString();
    }

    private String findSeleniumManagerPath() {
        File managerRoot = new File(System.getProperty("user.home"), ".cache/selenium/manager");
        File[] versionDirs = managerRoot.listFiles(File::isDirectory);
        if (versionDirs == null || versionDirs.length == 0) {
            throw new IllegalStateException("Selenium Manager binary was not found in " + managerRoot.getAbsolutePath());
        }

        java.util.Arrays.sort(versionDirs, java.util.Comparator.comparing(File::getName).reversed());
        for (File versionDir : versionDirs) {
            File managerBinary = new File(versionDir, "selenium-manager.exe");
            if (managerBinary.isFile()) {
                return managerBinary.getAbsolutePath();
            }
        }

        throw new IllegalStateException("Selenium Manager binary was not found in " + managerRoot.getAbsolutePath());
    }

    private record SeleniumResolution(String driverPath, String browserPath) {
    }

    private void openTargetUrl(String targetUrl) {
        int attempts = 3;
        for (int i = 1; i <= attempts; i++) {
            try {
                driver.get(targetUrl);
                return;
            } catch (WebDriverException e) {
                boolean isLastAttempt = i == attempts;
                boolean isConnectionReset = e.getMessage() != null && e.getMessage().contains("ERR_CONNECTION_RESET");
                if (isLastAttempt || !isConnectionReset) {
                    throw e;
                }
                logger.warn("Open URL attempt {} failed with connection reset. Retrying...", i);
                pauseBeforeRetry();
            }
        }
    }

    private void pauseBeforeRetry() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Retry before opening target URL was interrupted.", e);
        }
    }
}
