package tests;

import org.testng.annotations.Test;
import static common.Properties.GOOGLE;
import common.CommonFunctions;
import common.DockerBasePage;

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
	CommonFunctions.startBrowser(driver, GOOGLE);
	
	CommonFunctions.threadSleep(10);
	
	CommonFunctions.pageAssert(driver, GOOGLE);
    }
}
