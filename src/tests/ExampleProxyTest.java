package tests;

import static common.Properties.GOOGLE_SEARCH_TERM;

import org.testng.annotations.Test;

import common.CommonFunctions;
import common.CommonMethods;
import common.ProxyBasePage;

/**
 * @author atomicfruitcake
 *
 */
/*
 * This test demonstrates how to run tests through a proxy and capture a har file of network traffic.
 * We will see if a given string can be found in the har file to pass/fail the test 
 */
@Test
public class ExampleProxyTest extends ProxyBasePage{
    public void proxyTest() {
	
	// Start reading network traffic from the Proxy Server
	server.newHar("test.har");
	
	// Search for Search Term
	CommonMethods.doAGoogleSearch(driver, GOOGLE_SEARCH_TERM);	
	
	// Wait so that traffic will have been sent and read by the proxy server
	CommonFunctions.threadSleep(5);

	// Test if "foo" can be found in the network traffic
	CommonFunctions.scanHarFileForMatch(server.getHar(), GOOGLE_SEARCH_TERM);
    }
}
