package tests;

import org.testng.annotations.Test;

import common.BasePage;
import common.CommonFunctions;

@Test
public class ExampleTest extends BasePage {
	public void JIRAID() {
		
		// Open a webpage
		CommonFunctions.startBrowser(driver, "http://www.example.com");

		// Assert the URL
		CommonFunctions.pageAssert(driver, "http://www.example.com");
	}
}
