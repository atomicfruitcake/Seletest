package test;

import org.testng.annotations.Test;

import common.DriverFunctions;
import common.CommonMethods;
import common.driverHandler.LocalDriverHandlerImpl;

import static common.Properties.GOOGLE;
import static common.Properties.GOOGLE_SEARCH_TERM;

/**
 * @author atomicfruitcake
 *
 */
/**
 * Tests google search functionality
 *
 */
@Test
public class ExampleGoogleSearchTestOne extends LocalDriverHandlerImpl {
    public void googleSearch() {

	// Start browser on Google
	DriverFunctions.startBrowser(driver, GOOGLE);

	// Search for Search Term
	CommonMethods.doAGoogleSearch(driver, GOOGLE_SEARCH_TERM);

	// Assert that user has searched for the given term
	DriverFunctions.pageAssert(driver, GOOGLE + "#q=" + GOOGLE_SEARCH_TERM);
    }

}
