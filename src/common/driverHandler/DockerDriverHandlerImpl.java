package common.driverHandler;

import static common.Properties.DOCKER_SELENIUM;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.logging.Logger;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import common.CommonFunctions;
import common.JIRAUpdater;

/**
 * @author atomicfruitcake
 *
 */
public abstract class DockerDriverHandlerImpl implements AbstractDriverHandler {

    private static final Logger LOGGER = Logger.getLogger(DockerDriverHandlerImpl.class
	    .getName());

    public static WebDriver driver;

    @BeforeSuite
    public void beforeSuite() {
	CommonFunctions.createTestBot();
    }

    @BeforeMethod(alwaysRun = true)
    public void startup() throws IOException {
	String browser = CommonFunctions.getBrowser().toLowerCase();
	LOGGER.info("Starting dockerised browser: " + browser);

	if (browser == null) {
	    browser = "chrome";
	}

	switch (browser) {
	case "chrome": {
	    DesiredCapabilities capabilities = DesiredCapabilities.chrome();
	    capabilities.setBrowserName("chrome");
	    capabilities.setPlatform(Platform.LINUX);

	    // Send remoteWebDriver to Selenium hub
	    driver = new RemoteWebDriver(new URL(DOCKER_SELENIUM), capabilities);
	    break;
	}

	case "firefox": {
	    DesiredCapabilities capabilities = DesiredCapabilities.firefox();
	    capabilities.setBrowserName("firefox");
	    capabilities.setPlatform(Platform.LINUX);

	    // Send remoteWebDriver to Selenium hub
	    driver = new RemoteWebDriver(new URL(DOCKER_SELENIUM), capabilities);
	    break;
	}
	}
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result, Method method) throws Exception,
	    IOException, InterruptedException {
	JIRAUpdater.updateJiraTicket(result, method, driver);
	if (driver != null) {
	    driver.quit();
	}
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite(ITestContext testContext) throws IOException {
    }
}
