package test;

import static common.Properties.GOOGLE;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import common.CommonFunctions;
import common.basepage.DockerBasePage;

/**
 * @author atomicfruitcake
 *
 */
/*
 * This is a simple test to demonstrate running tests inside docker containers
 * Ensure the docker containers are running. This can be done by running 'sh
 * DockerRun.sh' from a terminal For more information on installing and running
 * docker, see https://www.docker.com
 */
@Test
public class ExampleDockerTest extends DockerBasePage {
    public void DockerTest() {
	
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
		
		// Wait for 10 seconds
		CommonFunctions.threadSleep(8);
	}	
    }
}
