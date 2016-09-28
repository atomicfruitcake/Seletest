package Tests;

import org.testng.annotations.Test;

import AllTests.BasePage;
import AllTests.CommonFunctions;

@Test
public class ExampleTest extends BasePage {
	public void JIRAID() {
		
		// Open a webpage
		CommonFunctions.startBrowser(driver, "http://www.example.com");

		// Assert the URL
		CommonFunctions.pageAssert(driver, "http://www.example.com");
	}
}
