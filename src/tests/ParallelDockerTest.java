package tests;

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

public class ParallelDockerTest  {
    
    public static WebDriver chromeDriver;
    public static WebDriver firefoxDriver;

    // Simple Google Easter egg to demonstrate automation
    public void googleEasterEggs(WebDriver driver) {
	// Start the browser at google homepage
	CommonFunctions.startBrowser(driver, GOOGLE);

	List<String> googleSearches = new ArrayList<String>();
	googleSearches.add("do a barrel roll");
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
    
    public void setDockerizedBrowser(WebDriver driver, String browserName){
	DesiredCapabilities capabilities = DesiredCapabilities.chrome();
	    capabilities.setBrowserName(browserName);
	    capabilities.setPlatform(Platform.LINUX);

	    // Try and send remoteWebDriver to Selenium hub
	    try {
		driver = new RemoteWebDriver(new URL(DOCKER_SELENIUM), capabilities);
	    } catch (MalformedURLException e) {
		e.printStackTrace();
	    }
    }
 

    @Test
    public void parallelDockerTestChrome() {
	setDockerizedBrowser(chromeDriver, "chrome");
	googleEasterEggs(chromeDriver);
	if (chromeDriver != null) {
	    chromeDriver.quit();
	}
    }

    @Test
    public void parallelDockerTestFirefox() {
	setDockerizedBrowser(firefoxDriver, "firefox");
	googleEasterEggs(firefoxDriver);
	if (firefoxDriver != null) {
	    firefoxDriver.quit();
	}
    }
}
