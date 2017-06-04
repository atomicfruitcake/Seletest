package common;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

/**
 * @author atomicfruitcake
 *
 */
public class DriverFunctions {
    
    private static final Logger LOGGER = Logger.getLogger(DriverFunctions.class
	    .getName());
    
    static JavascriptExecutor js;
    
    // Start the browser at a given URL
    public static void startBrowser(WebDriver driver, String url) {
	LOGGER.info("Starting browser with URL: " + url);
	driver.get(url);
	driver.manage().window().maximize();
    }
    
    // Sets an item in local storage using JavaScript.
    public static void setItemInLocalStorage(String item, String value,
	    WebDriver driver) {

	js = (JavascriptExecutor) driver;
	js.executeScript(String.format(
		"window.sessionStorage.setItem('%s','%s');", item, value));
    }

    // Scrolls to the bottom of a webpage
    public static void scrollToBottomOfPage(WebDriver driver) {
	js = (JavascriptExecutor) driver;
	js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    // Wait for an element to load
    public static void waitForElement(WebDriver driver, String cssSelector) {
	driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
	WebElement element = new WebDriverWait(driver, 60)
		.until(ExpectedConditions.elementToBeClickable(By
			.cssSelector(cssSelector)));
	Assert.assertTrue(element.isDisplayed());
    }

    // Clicks an element on a webpage if it is present
    public static void clickElement(WebDriver driver, String cssSelector) {
	waitForElement(driver, cssSelector);
	CommonFunctions.threadSleep(1);
	waitForElement(driver, cssSelector);
	WebElement element = driver.findElement(By.cssSelector(cssSelector));
	element.click();
    }

    // Clicks and element using the javascript on a webpage
    public static void javascriptClick(WebDriver driver, WebElement element) {
	js = (JavascriptExecutor) driver;
	js.executeScript("arguments[0].click();", element);
    }

    // Clicks an element on a webpage with javascript if it is present
    public static void clickElementWithJs(WebDriver driver, String cssSelector) {
	waitForElement(driver, cssSelector);
	javascriptClick(driver, driver.findElement(By.cssSelector(cssSelector)));
    }

    // Clicks an element using Xpath
    public static void clickElementByXpath(WebDriver driver,
	    String cssSelector, String xpath) {
	waitForElement(driver, cssSelector);
	WebElement element = driver.findElement(By.xpath(xpath));
	element.click();
    }

    // Clicks an element using ID
    public static void clickElementById(WebDriver driver, String cssSelector,
	    String id) {
	waitForElement(driver, cssSelector);
	WebElement element = driver.findElement(By.id(id));
	element.click();
    }

    // Clicks an element using Name
    public static void clickElementByName(WebDriver driver, String cssSelector,
	    String name) {
	waitForElement(driver, cssSelector);
	WebElement element = driver.findElement(By.name(name));
	element.click();
    }

    // Clicks an element using ClassName
    public static void clickElementByClassName(WebDriver driver,
	    String cssSelector, String className) {
	waitForElement(driver, cssSelector);
	WebElement element = driver.findElement(By.className(className));
	element.click();
    }

    // Send Keys to an element if it is present
    public static void sendKeysToElement(WebDriver driver, String cssSelector,
	    String keys) {
	waitForElement(driver, cssSelector);
	clearTextFromElement(driver, cssSelector);
	WebElement element = driver.findElement(By.cssSelector(cssSelector));
	element.sendKeys(keys);
    }

    // Clears the text from a web element
    public static void clearTextFromElement(WebDriver driver, String cssSelector) {
	waitForElement(driver, cssSelector);
	WebElement element = driver.findElement(By.cssSelector(cssSelector));
	element.clear();
    }

    // Clicks a link by Href
    public static void clickLinkByHref(WebDriver driver, String href) {
	List<WebElement> anchors = driver.findElements(By.tagName("a"));
	Iterator<WebElement> i = anchors.iterator();

	while (i.hasNext()) {
	    WebElement anchor = i.next();
	    if (anchor.getAttribute("href").contains(href)) {
		anchor.click();
		break;
	    }
	}
    }

    // Forces mouse to hover over an element
    public static void mouseHover(WebDriver driver, String cssSelector)
	    throws AWTException {
	CommonFunctions.threadSleep(3);
	Point point = driver.findElement(By.cssSelector(cssSelector))
		.getLocation();
	Robot robot = new Robot();
	robot.mouseMove(point.getX(), point.getY());
    }

    // Gets the current URL from the browser
    public static String getCurrentUrl(WebDriver driver) {
	return driver.getCurrentUrl();
    }

    // Select an option from a drop down menu
    public static void selectFromDropDown(WebDriver driver, String cssSelector,
	    int index) {
	waitForElement(driver, cssSelector);
	WebElement dropDownListBox = driver.findElement(By
		.cssSelector(cssSelector));
	Select clickProvider = new Select(dropDownListBox);
	clickProvider.selectByIndex(index);
    }

    // Takes a screenshot of the browser
    public static void screenshot(String name, WebDriver driver) {
	if (SettingsReader.getScreenshotToggle().toLowerCase() == "yesscreenshot") {
	    LOGGER.info("Taking Screenshot of browser");
	    Date dDate = new Date();
	    SimpleDateFormat fullFormat = new SimpleDateFormat(
		    "yyyy.MM.dd_HHmmss");
	    File screenshot = ((TakesScreenshot) driver)
		    .getScreenshotAs(OutputType.FILE);

	    try {
		FileUtils.copyFile(screenshot, new File("Screenshots\\" + name
			+ "_" + fullFormat.format(dDate) + ".png"));
	    }

	    catch (Exception ex) {
		LOGGER.severe("Error creating screenshot");
		System.out.println(ex.getMessage());
		System.out.println(ex.getStackTrace());
	    }
	}
    }

    // Assert that the browser is on a given page
    public static void pageAssert(WebDriver driver, String page) {
	LOGGER.info("Asserting current URL is equal to: " + page);
	String CurrentURL = driver.getCurrentUrl();
	Assert.assertEquals(CurrentURL, page);
    }

    // Assert that the browser is not on a given page
    public static void pageAssertFalse(WebDriver driver, String page) {
	LOGGER.info("Asserting current URL is not equal to: " + page);
	String CurrentURL = driver.getCurrentUrl();
	Assert.assertFalse(CurrentURL.equals(page));
    }

    // Assert that text is present on a page
    public static void textAssert(WebDriver driver, String text) {
	LOGGER.info("Asserting page source contains " + text);
	boolean testPass;
	if (driver.getPageSource().contains(text)) {
	    testPass = true;
	} else {
	    testPass = false;
	    LOGGER.info("Did not find text: " + text + " in page source");
	}
	Assert.assertTrue(testPass);
    }

    // Asserts that a given webelement is not clickable
    public static void assertElementNotClickable(WebDriver driver,
	    String cssSelector) {
	LOGGER.info("Asserting " + cssSelector + " is not clickable");
	boolean testPass;
	waitForElement(driver, cssSelector);
	if (driver.findElement(By.cssSelector(cssSelector)).isEnabled()) {
	    testPass = false;
	} else {
	    testPass = true;
	}
	Assert.assertTrue(testPass);
    }

    // Verify that that a web element exists
    public static void verifyExists(WebDriver driver, String cssSelector) {
	LOGGER.info("Asserting " + cssSelector + " exists");
	waitForElement(driver, cssSelector);
	Assert.assertNotEquals(driver.findElement(By.cssSelector(cssSelector)),
		null);
    }

    // Verify the text on a given web element
    public static void verifyText(WebDriver driver, String cssSelector,
	    String text) {
	LOGGER.info("Asserting " + cssSelector + " contains the text " + text);
	waitForElement(driver, cssSelector);
	Assert.assertEquals(driver.findElement(By.cssSelector(cssSelector))
		.getText(), null);
    }

    // Verify that a web element does not exist
    public static void verifyDoesNotExist(WebDriver driver, String cssSelector) {
	LOGGER.info("Asserting " + cssSelector + "does not exist");
	Assert.assertEquals(driver.findElement(By.cssSelector(cssSelector)),
		null);
    }

    // Verify that an element is clickable
    public static void verifyClickable(WebDriver driver, String cssSelector) {
	LOGGER.info("Asserting " + cssSelector + " is clickable");
	WebDriverWait wait = new WebDriverWait(driver, 10);
	wait.until(ExpectedConditions.invisibilityOfElementLocated(By
		.cssSelector(cssSelector)));
    }

    // Get text from a text field
    public static String getTextFromField(WebDriver driver, String cssSelector) {
	WebElement inputBox = driver.findElement(By.cssSelector(cssSelector));
	String textInsideInputBox = inputBox.getAttribute("value");
	return textInsideInputBox;
    }

}
