package common.basepage;

import static common.Properties.CHROME;
import static common.Properties.FIREFOX;
import static common.Properties.IE11;
import static common.Properties.PHANTOMJS;

import java.io.IOException;
import java.lang.reflect.Method;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
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
public abstract class BrowserBasePage implements Basepage {

    public static WebDriver driver;

    @BeforeSuite
    public void beforeSuite() {
	CommonFunctions.createTestBot();
    }

    @BeforeMethod(alwaysRun = true)
    public void startUp() throws IOException {

	switch (CommonFunctions.getBrowser().toLowerCase()) {
	case "chrome": {
	    System.setProperty("webdriver.chrome.driver", CHROME);
	    driver = new ChromeDriver();
	    break;
	}

	case "firefox": {
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

	case "htmlunit": {
	    driver = new HtmlUnitDriver();
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
	CommonFunctions.updateSlackAfterSuite(testContext);
    }
}
