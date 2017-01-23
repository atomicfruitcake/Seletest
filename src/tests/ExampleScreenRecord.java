package tests;

import org.testng.annotations.Test;

import common.CommonFunctions;
import common.CommonMethods;
import common.basepage.BasePage;
import common.ScreenRecorder;
import static common.Properties.GOOGLE;
import static common.Properties.GOOGLE_SEARCH_TERM;

/**
 * @author atomicfruitcake
 *
 */
/**
 * Records a test of google search functionality
 *
 */
@Test
public class ExampleScreenRecord extends BasePage {
    public void recordGoogleSearch() {
	// Start the recording
	ScreenRecorder.startRecording();

	// Start browser on Google
	CommonFunctions.startBrowser(driver, GOOGLE);

	// Search for Search Term
	CommonMethods.doAGoogleSearch(driver, GOOGLE_SEARCH_TERM);

	// Assert that user has searched for the given term
	CommonFunctions.pageAssert(driver, GOOGLE + "#q=" + GOOGLE_SEARCH_TERM);

	// Stop the recording
	ScreenRecorder.stopRecording();
    }
}