package tests;

import net.lightbody.bmp.core.har.Har;

import org.testng.annotations.Test;

import common.CommonFunctions;
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
	
	server.newHar("test.har");
	
	CommonFunctions.startBrowser(driver, "http://www.google.com");
	
	Har har = server.getHar();
	
	// Test if "foo" can be found in the network traffic
	CommonFunctions.scanHarFileForMatch(har, "foo");
    }
}
