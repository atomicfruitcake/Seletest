package test;

import static common.Properties.GOOGLE;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import common.CommonFunctions;
import common.basepage.BrowserBasePage;

/**
 * @author atomicfruitcake
 *
 */
/**
 * Tests google easter eggs
 *
 */
@Test
public class ExampleGoogleSearchTestTwo extends BrowserBasePage {
    public void GooglerEasterEggTest() {

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
    }
}
