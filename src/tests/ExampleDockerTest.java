package tests;

import org.testng.annotations.Test;

import static common.Properties.GOOGLE;
import common.CommonFunctions;
import common.basepage.DockerBasePage;

/**
 * @author atomicfruitcake
 *
 */
/*
 * This is a simple test to demonstrate running tests inside docker containers
 * Ensure the docker containers are running. This can be done by running 'sh DockerRun.sh' from a terminal
 * For more information on installing and running docker, see https://www.docker.com
 */
@Test
public class ExampleDockerTest extends DockerBasePage {
    public void DockerTest() {
	
	// Start the browser at google homepage
	CommonFunctions.startBrowser(driver, GOOGLE);
	
	// Wait for 10 seconds
	CommonFunctions.threadSleep(10);
	
	// Assert that the user is still on google homepage
	CommonFunctions.pageAssert(driver, GOOGLE);
    }
}
