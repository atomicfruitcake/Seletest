package common.basepage;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import common.CommonFunctions;
import common.JIRAUpdater;
import io.selendroid.client.SelendroidDriver;
import io.selendroid.common.SelendroidCapabilities;
import io.selendroid.common.device.DeviceTargetPlatform;
import io.selendroid.standalone.SelendroidConfiguration;
import io.selendroid.standalone.SelendroidLauncher;

/**
 * @author atomicfruitcake
 *
 */
public abstract class SelendroidBasePage implements Basepage{
    private static final Logger LOGGER = Logger
	    .getLogger(SelendroidBasePage.class.getName());

    public static WebDriver driver;
    public static SelendroidLauncher selendroidServer;

    @BeforeSuite
    public void beforeSuite() {
	CommonFunctions.createTestBot();
    }

    @BeforeMethod(alwaysRun = true)
    public void startup() throws Exception {
	LOGGER.info("Starting browser: Android");

	// Start Selendroid server
	if (selendroidServer != null) {
	    selendroidServer.stopSelendroid();
	}
	SelendroidConfiguration config = new SelendroidConfiguration();
	selendroidServer = new SelendroidLauncher(config);
	selendroidServer.launchSelendroid();

	// Start the Selendroid driver
	SelendroidCapabilities capabilities = SelendroidCapabilities.emulator(
		DeviceTargetPlatform.ANDROID17,
		"io.selendroid.androiddriver:0.17.0");
	capabilities.setEmulator(true);
	driver = new SelendroidDriver(capabilities);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result, Method method) throws Exception,
	    IOException, InterruptedException {
	JIRAUpdater.updateJiraTicket(result, method, driver);
	selendroidServer.stopSelendroid();
	if (driver != null) {
	    driver.quit();
	}
	if (selendroidServer != null) {
	    selendroidServer.stopSelendroid();
	}
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite(ITestContext testContext) throws IOException {
	CommonFunctions.updateSlackAfterSuite(testContext);
    }
}
