package common;

import static common.Properties.CHROME;
import static common.Properties.FIREFOX;
import static common.Properties.IE11;
import static common.Properties.PHANTOMJS;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

/**
 * @author atomicfruitcake
 *
 */
public class BasePage {

    private static final Logger LOGGER = Logger.getLogger(BasePage.class
	    .getName());

    public static WebDriver driver;

    @BeforeSuite
    public void beforeSuite() {
	CommonFunctions.createTestBot();
    }

    @BeforeMethod(alwaysRun = true)
    public void startUp() throws IOException {
	LOGGER.info("Starting browser: " + CommonFunctions.getBrowser());

	switch (CommonFunctions.getBrowser().toLowerCase()) {
	case "chrome": {
	    System.setProperty("webdriver.chrome.driver", CHROME);
	    driver = new ChromeDriver();
	    break;
	}

	case "firefox": {
	    System.setProperty("webdriver.firefox.driver", FIREFOX);
	    driver = new FirefoxDriver();
	    break;
	}

	case "internet explorer": {
	    System.setProperty("webdriver.ie.driver", IE11);
	    driver = new InternetExplorerDriver();
	    break;
	}

	case "phantom": {
	    System.setProperty("phantomjs.binary.path", PHANTOMJS);
	    driver = new PhantomJSDriver();
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
}
