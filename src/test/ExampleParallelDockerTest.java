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
/*
 * Allows TestNGParallel.xml to run Firefox and Chrome tests
 * in parallel inside docker containers
 */
public class ExampleParallelDockerTest {
    
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

	    // Wait for 8 seconds
	    CommonFunctions.threadSleep(8);
	}
	
	// Quit browser
	if(driver!=null){
	    driver.quit();
	}
    }
    
    public WebDriver dockerizedChromeBrowser() {
	DesiredCapabilities capabilities = DesiredCapabilities.chrome();
	capabilities.setBrowserName("chrome");
	capabilities.setPlatform(Platform.LINUX);
	try {
	    return new RemoteWebDriver(new URL(DOCKER_SELENIUM), capabilities);
	} catch (MalformedURLException e) {
	    e.printStackTrace();
	    return null;
	}
	
    }

    public WebDriver dockerizedFirefoxBrowser() {
	DesiredCapabilities capabilities = DesiredCapabilities.firefox();
	capabilities.setBrowserName("firefox");
	capabilities.setPlatform(Platform.LINUX);
	try {
	    return new RemoteWebDriver(new URL(DOCKER_SELENIUM), capabilities);
	} catch (MalformedURLException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    @Test
    public void parallelDockerTestChrome() {
	googleEasterEggs(dockerizedChromeBrowser());
    }

    @Test
    public void parallelDockerTestFirefox() {

	googleEasterEggs(dockerizedFirefoxBrowser());
    }
}
