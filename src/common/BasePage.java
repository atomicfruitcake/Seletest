package common;

import static common.Properties.CHROME;
import static common.Properties.CHROMEOSX;
import static common.Properties.FIREFOX;
import static common.Properties.IE11;
import static common.Properties.PHANTOMJS;

import java.io.IOException;
import java.util.logging.Logger;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * @author atomicfruitcake
 *
 */
public class BasePage {

	private static final Logger LOGGER = Logger.getLogger(BasePage.class
			.getName());

	public static WebDriver driver;

	@BeforeMethod(alwaysRun = true)
	public void startUp() throws IOException {
		LOGGER.info("Starting browser: "
				+ TestConfigImpl.testConfig.getBrowser());

		switch (TestConfigImpl.testConfig.getBrowser()) {
		case "Chrome": {
			switch (CommonFunctions.operatingSystem()) {
			case "Windows": {
				System.setProperty("webdriver.chrome.driver", CHROME);
			}
			case "OSX": {
				System.setProperty("webdriver.chrome.driver", CHROMEOSX);
			}
			}
		}

		case "Firefox": {
			System.setProperty("webdriver.firefox.driver", FIREFOX);
			driver = new FirefoxDriver();
			break;
		}

		case "Internet Explorer": {
			System.setProperty("webdriver.ie.driver", IE11);
			driver = new InternetExplorerDriver();
			break;
		}

		case "Phantom": {
			System.setProperty("phantomjs.binary.path", PHANTOMJS);
			driver = new PhantomJSDriver();
			break;
		}
		}
	}

	@AfterMethod(alwaysRun = true)
	public void tearDown() {
		driver.quit();
	}
}
