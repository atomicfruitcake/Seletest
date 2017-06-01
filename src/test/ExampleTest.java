package test;

import static common.Properties.EXAMPLE;

import org.testng.annotations.Test;

import common.CommonFunctions;
import common.driverHandler.LocalDriverHandlerImpl;

/**
 * @author atomicfruitcake
 *
 */
/*
 * This is a simple example test. Note that all functions used are from
 * CommonFunctions. No data or functions are hardcoded ensuring the test is
 * maintainable.
 */
@Test
public class ExampleTest extends LocalDriverHandlerImpl {
    public void JIRAID() {

	// Open a webpage
	CommonFunctions.startBrowser(driver, EXAMPLE);

	// Assert the URL
	CommonFunctions.pageAssert(driver, EXAMPLE);
    }
}
