package test;

import org.testng.annotations.Test;

import common.CommonFunctions;
import common.CommonMethods;
import common.basepage.BrowserBasePage;
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
public class ExampleGoogleSearch extends BrowserBasePage {
    public void googleSearch() {

	// Start browser on Google
	CommonFunctions.startBrowser(driver, GOOGLE);

	// Search for Search Term
	CommonMethods.doAGoogleSearch(driver, GOOGLE_SEARCH_TERM);

	// Assert that user has searched for the given term
	CommonFunctions.pageAssert(driver, GOOGLE + "#q=" + GOOGLE_SEARCH_TERM);
    }

}
