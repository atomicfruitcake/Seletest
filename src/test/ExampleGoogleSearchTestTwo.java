package test;

import static common.Properties.GOOGLE;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import common.CommonFunctions;
import common.DriverFunctions;
import common.driverHandler.LocalDriverHandlerImpl;

/**
 * @author atomicfruitcake
 *
 */
/**
 * Tests google easter eggs
 *
 */
@Test
public class ExampleGoogleSearchTestTwo extends LocalDriverHandlerImpl {
    public void GooglerEasterEggTest() {

	// Start the browser at google homepage
	DriverFunctions.startBrowser(driver, GOOGLE);

	List<String> googleSearches = new ArrayList<String>();
	googleSearches.add("zerg rush");
	googleSearches.add("conway's game of life");
	googleSearches.add("fun facts");
	googleSearches.add("anagram");

	for (String search : googleSearches) {
	    // Enter search query
	    DriverFunctions.sendKeysToElement(driver, "#lst-ib", search);

	    // Click to search
	    DriverFunctions.clickElement(driver, "#_fZl > span > svg");

	    // Wait for 8 seconds
	    CommonFunctions.threadSleep(8);
	}
    }
}
