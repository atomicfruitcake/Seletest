package common;

import static common.Properties.DOCKER_SELENIUM;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.logging.Logger;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

/**
 * @author sambass
 *
 */
public class DockerBasePage {

	private static final Logger LOGGER = Logger.getLogger(DockerBasePage.class
			.getName());

	public static WebDriver driver;

	@BeforeSuite
	public void beforeSuite() {
		// Add Before Suite Methods here is required
	}

	@BeforeMethod(alwaysRun = true)
	public void beforeMethod() throws IOException {
		String browser = CommonFunctions.getBrowser();
		LOGGER.info("Starting dockerised browser: " + browser);

		if (browser == "") {
			browser = "chrome";
		}

		switch (browser) {
		case "chrome": {
			DesiredCapabilities capabilities = DesiredCapabilities.chrome();
			capabilities.setBrowserName("chrome");
			capabilities.setPlatform(Platform.LINUX);

			// Send removeWebDriver to dockerised selenium hub
			driver = new RemoteWebDriver(new URL(DOCKER_SELENIUM), capabilities);
			break;
		}

		case "firefox": {
			DesiredCapabilities capabilities = DesiredCapabilities.firefox();
			capabilities.setBrowserName("firefox");
			capabilities.setPlatform(Platform.LINUX);

			// Send removeWebDriver to selenium hub
			driver = new RemoteWebDriver(new URL(DOCKER_SELENIUM), capabilities);
			break;
		}
		}
	}

	@AfterMethod(alwaysRun = true)
	public void afterMethod(ITestResult result, Method method)
			throws Exception, IOException, InterruptedException {
		JIRAUpdater.updateJiraTicket(result, method, driver);
		driver.quit();

	}
}
