package test;

import static common.Properties.DOCKER_SELENIUM;
import static common.Properties.GOOGLE;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Test;

import common.CommonFunctions;

/**
 * @author atomicfruitcake
 *
 */

public class ParallelDockerTest {
    
    private static WebDriver chromeDriver;
    private static WebDriver firefoxDriver;

    // Simple Google Easter egg to demonstrate automation
    public void googleEasterEggs(WebDriver driver) {
	// Start the browser at google homepage
	CommonFunctions.startBrowser(driver, GOOGLE);

	List<String> googleSearches = new ArrayList<String>();
	googleSearches.add("zerg rush");
	googleSearches.add("conway's game of life");
	googleSearches.add("fun facts");
	googleSearches.add("anagram");

	for (String search : googleSearches) {
	    // Enter search query
	    CommonFunctions.sendKeysToElement(driver, "#lst-ib", search);

	    // Click to search
	    CommonFunctions.clickElement(driver, "#_fZl > span > svg");

	    // Wait for 10 seconds
	    CommonFunctions.threadSleep(8);
	}
    }
    
    void startDockerizedChromeBrowser() {
	DesiredCapabilities capabilities = DesiredCapabilities.chrome();
	capabilities.setBrowserName("chrome");
	capabilities.setPlatform(Platform.LINUX);
	
	// Try and send remoteWebDriver to Selenium hub
	try {
	    chromeDriver = new RemoteWebDriver(new URL(DOCKER_SELENIUM), capabilities);
	} catch (MalformedURLException e) {
	    e.printStackTrace();
	}
    }

    public void startDockerizedFirefoxBrowser() {
	DesiredCapabilities capabilities = DesiredCapabilities.firefox();
	capabilities.setBrowserName("firefox");
	capabilities.setPlatform(Platform.LINUX);

	// Try and send remoteWebDriver to Selenium hub
	try {
	    firefoxDriver = new RemoteWebDriver(new URL(DOCKER_SELENIUM), capabilities);
	} catch (MalformedURLException e) {
	    e.printStackTrace();
	}
    }

    @Test
    public void parallelDockerTestChrome() {
	startDockerizedChromeBrowser();
	googleEasterEggs(chromeDriver);
	if (chromeDriver != null) {
	    chromeDriver.quit();
	}
    }

    @Test
    public void parallelDockerTestFirefox() {
	startDockerizedFirefoxBrowser();
	googleEasterEggs(firefoxDriver);
	if (firefoxDriver != null) {
	    firefoxDriver.quit();
	}
    }
}
