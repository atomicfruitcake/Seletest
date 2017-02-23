package common.basepage;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
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
public abstract class ProxyBasePage implements Basepage{

    private static final Logger LOGGER = Logger.getLogger(ProxyBasePage.class
	    .getName());

    public static WebDriver driver;
    public static BrowserMobProxyServer server;

    @BeforeSuite
    public void beforeSuite() {
	CommonFunctions.createTestBot();
    }

    @BeforeMethod(alwaysRun = true)
    public void startup() throws IOException {
	LOGGER.info("Starting Proxy server");
	server = new BrowserMobProxyServer();
	server.start();
	Proxy proxy = ClientUtil.createSeleniumProxy(server);
	DesiredCapabilities seleniumCapabilities = new DesiredCapabilities();
	seleniumCapabilities.setCapability(CapabilityType.PROXY, proxy);
	driver = new FirefoxDriver(seleniumCapabilities);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result, Method method) throws Exception,
	    IOException, InterruptedException {
	JIRAUpdater.updateJiraTicket(result, method, driver);
	if (driver != null) {
	    driver.quit();
	}
	if (server !=null) {
	    server.stop();
	}
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite(ITestContext testContext) throws IOException {
	CommonFunctions.updateSlackAfterSuite(testContext);
    }
}
