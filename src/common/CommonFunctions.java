package common;

import static common.Properties.*;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import net.lightbody.bmp.core.har.Har;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
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
import org.testng.TestNG;
import org.testng.collections.Lists;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * @author atomicfruitcake
 *
 */
public class CommonFunctions {

    private static final Logger LOGGER = Logger.getLogger(CommonFunctions.class
	    .getName());

    // Reads Data from the settings.txt file
    public static String readSettings(int lineNumber) throws IOException {
	try {
	    return Files.readAllLines(Paths.get("settings.txt"))
		    .get(lineNumber);
	} catch (Exception e) {
	    e.printStackTrace();
	    return null;
	}
    }

    // Get settings from the settings.txt file
    public static String getSettings(int entryNo) {
	try {
	    return readSettings(entryNo);
	} catch (IOException e) {
	    throw new RuntimeException("Error fetching data from settings.txt",
		    e);
	}
    }

    // Get environment name from settings file
    public static String getEnvironment() {
	return getSettings(0);
    }

    // Gets the username from the settings file
    public static String getUsername() {
	return getSettings(1);
    }

    // Gets the password from the settings file
    public static String getPassword() {
	return getSettings(2);
    }

    // Gets the browser from the settings file
    public static String getBrowser() {
	return getSettings(3);
    }

    // Gets the browser from the settings file
    public static String getUpdateJira() {
	return getSettings(4);
    }

    // Start the browser at a given URL
    public static void startBrowser(WebDriver driver, String url) {
	LOGGER.info("Starting browser with URL: " + url);
	driver.get(url);
	driver.manage().window().maximize();

    }

    // Gets the URL for a given server
    public static String environmentSelector() {
	Map<String, String> environmentSelect = new HashMap<String, String>();
	environmentSelect.put("ENV_NAME", "ENV_URL");
	return environmentSelect.get(getEnvironment());
    }

    // Gets IP address for server based on ENV
    public static String getIpAddressServer() throws IOException {
	Map<String, String> ipSelect = new HashMap<String, String>();
	ipSelect.put("ENV_NAME, ", "ENV_IP");
	// FIll with all required app servers for server side tests
	return ipSelect.get(getEnvironment());
    }

    // Sets the parameters for the TestNG XML
    public static Map<String, String> setTestNGParameters() {
	Map<String, String> TESTPARAMETERS = new HashMap<String, String>();
	TESTPARAMETERS.put("name", "FN Test Suite");
	TESTPARAMETERS.put("preserve-order", "true");
	TESTPARAMETERS.put("parallel", "false");
	TESTPARAMETERS.put("verbose", "10");
	return TESTPARAMETERS;
    }

    static JavascriptExecutor js;

    // Sets an item in local storage using JavaScript. E.G.
    public static void setItemInLocalStorage(String item, String value,
	    WebDriver driver) {
	js = (JavascriptExecutor) driver;
	js.executeScript(String.format(
		"window.sessionStorage.setItem('%s','%s');", item, value));
    }

    // Clicks and element using the javascript on a webpage
    public static void javascriptClick(WebDriver driver, WebElement element) {
	js = (JavascriptExecutor) driver;
	js.executeScript("arguments[0].click();", element);
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

    public static void waitForUrl(WebDriver driver, String url) {
	driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
	driver.getCurrentUrl();
	for (int i = 0; i < 60; i++) {
	    if (getCurrentUrl(driver) == url) {
		i = 60;
		break;
	    } else {
		threadSleep(1);
	    }
	}
    }

    // Clicks an element on a webpage if it is present
    public static void clickElement(WebDriver driver, String cssSelector) {
	waitForElement(driver, cssSelector);
	CommonFunctions.threadSleep(1);
	waitForElement(driver, cssSelector);
	WebElement element = driver.findElement(By.cssSelector(cssSelector));
	element.click();
    }

    // Clicks an element on a webpage with javascript if it is present
    public static void clickElementWithJs(WebDriver driver, String cssSelector) {
	waitForElement(driver, cssSelector);
	WebElement element = driver.findElement(By.cssSelector(cssSelector));
	javascriptClick(driver, element);
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
	threadSleep(3);
	Point point = driver.findElement(By.cssSelector(cssSelector))
		.getLocation();
	Robot robot = new Robot();
	robot.mouseMove(point.getX(), point.getY());
    }

    // Gets the current URL from the browser
    public static String getCurrentUrl(WebDriver driver) {
	return driver.getCurrentUrl();
    }

    // Puts the test to sleep for 'x' seconds
    public static void threadSleep(int x) {
	int time = x * 1000;
	try {
	    Thread.sleep(time);
	} catch (InterruptedException a) {
	    a.printStackTrace();
	}
    }

    // Select an option from a drop down menu
    public static void selectFromDropDown(WebDriver driver, String cssSelector,
	    int index) {
	CommonFunctions.waitForElement(driver, cssSelector);
	WebElement dropDownListBox = driver.findElement(By
		.cssSelector(cssSelector));
	Select clickProvider = new Select(dropDownListBox);
	clickProvider.selectByIndex(index);
    }

    // Takes a screenshot of the browser
    public static void screenshot(String name, WebDriver driver) {

	Date dDate = new Date();
	SimpleDateFormat fullFormat = new SimpleDateFormat("yyyy.MM.dd_HHmmss");
	File screenshot = ((TakesScreenshot) driver)
		.getScreenshotAs(OutputType.FILE);

	try {
	    FileUtils.copyFile(screenshot, new File("screenshots\\" + name
		    + "_" + fullFormat.format(dDate) + ".png"));
	}

	catch (Exception ex) {
	    LOGGER.severe("Error creating screenshot");
	    System.out.println(ex.getMessage());
	    System.out.println(ex.getStackTrace());
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
	boolean testPass;
	if (driver.getPageSource().contains(text)) {
	    testPass = true;
	} else {
	    testPass = false;
	    LOGGER.info("Did not find text: " + text + " in page source");
	}
	Assert.assertTrue(testPass);
    }

    public static void assertElementNotClickable(WebDriver driver,
	    String cssSelector) {
	boolean testPass;
	if (driver.findElement(By.cssSelector(cssSelector)).isEnabled()) {
	    testPass = false;
	} else {
	    testPass = true;
	}
	Assert.assertTrue(testPass);
    }

    // Verify that that a web element exists
    public static void verifyExists(WebDriver driver, String cssSelector) {
	waitForElement(driver, cssSelector);
	Assert.assertNotEquals(driver.findElement(By.cssSelector(cssSelector)),
		null);
    }

    // Verify the text on a given web element
    public static void verifyText(WebDriver driver, String cssSelector,
	    String text) {
	waitForElement(driver, cssSelector);
	Assert.assertEquals(driver.findElement(By.cssSelector(cssSelector))
		.getText(), null);
    }

    // Verify that a web element does not exist
    public static void verifyDoesNotExist(WebDriver driver, String cssSelector) {
	Assert.assertEquals(driver.findElement(By.cssSelector(cssSelector)),
		null);
    }

    // Verify that an element is clickable
    public static void verifyClickable(WebDriver driver, String cssSelector) {
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

    // Build TestNG XML file. Replace hardcoded values to programmatically build
    // different testing configurations
    public static void buildTestngXml() throws IOException {
	LOGGER.info("Building TestNG XML file");
	Document document = DocumentHelper.createDocument();
	document.addDocType("suite", "SYSTEM",
		"http://testng.org/testng-1.0.dtd");
	Element suite = document.addElement("suite")
		.addAttribute("name", "testSuite")
		.addAttribute("preserve-order", "true")
		.addAttribute("parallel", "false")
		.addAttribute("verbose", "10");
	Element listeners = suite.addElement("listeners");
	listeners.addElement("listener").addAttribute("class-name",
		"org.uncommons.reportng.HTMLReporter");
	listeners.addElement("listener").addAttribute("class-name",
		"org.uncommons.reportng.JUnitXMLReporter");
	Element test = suite.addElement("test").addAttribute("name", "tests");
	Element packages = test.addElement("packages");
	for (int i = 0; i < TEST_PACKAGES.length; i++) {
	    packages.addElement("package").addAttribute("name",
		    TEST_PACKAGES[i]);
	}
	FileOutputStream fos = new FileOutputStream("testng.xml");
	OutputFormat format = OutputFormat.createPrettyPrint();
	XMLWriter writer = new XMLWriter(fos, format);
	writer.write(document);
	writer.flush();
    }

    // Runs the TestNG.xml
    public static void runTestngXml() throws InterruptedException {
	LOGGER.info("Running TestNG XML file");
	TestNG testng = new TestNG();
	List<String> suites = Lists.newArrayList();
	suites.add("testng.xml");
	testng.setTestSuites(suites);
	testng.run();
    }

    // Returns a random string 'i' characters long
    public static String getRandomString(int i) {
	return RandomStringUtils.randomAlphanumeric(i);
    }

    // Generates a random mailinator username
    public static String getRandomUsername() {
	return getRandomString(20).toLowerCase() + "@mailinator.com";
    }

    // Runs a terminal command from string input
    public static void runTerminalCommand(String curlCommand)
	    throws InterruptedException {
	StringBuffer output = new StringBuffer();
	try {
	    Process process = Runtime.getRuntime().exec(curlCommand);
	    process.waitFor();
	    BufferedReader reader = new BufferedReader(new InputStreamReader(
		    process.getInputStream()));
	    String line = "";
	    while ((line = reader.readLine()) != null) {
		output.append(line + "\n");
	    }
	    System.out.println(output);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    // Tunnels into server to run shell Commands
    public static void tunnelIntoServer(String shellCommand, int port)
	    throws JSchException, IOException {
	LOGGER.info("Tunneling into app server to run command: " + shellCommand);
	Properties config = new Properties();
	JSch jsch = new JSch();
	Session session = jsch.getSession(SERVERUSERNAME, SERVERIP, port);
	session.setPassword(SERVERPASSWORD);
	config.put("StrictHostKeyChecking", "no");
	session.setConfig(config);
	session.connect();
	ChannelExec channel = (ChannelExec) session.openChannel("exec");
	BufferedReader in = new BufferedReader(new InputStreamReader(
		channel.getInputStream()));
	channel.setCommand(shellCommand);
	channel.connect();
	String msg = null;
	while ((msg = in.readLine()) != null) {
	    System.out.println(msg);
	}
	LOGGER.info("Disconnecting from " + SERVERIP);
	channel.disconnect();
	session.disconnect();
    }

    // Gets the current operating system
    public static String getOS() {
	return System.getProperty("os.name").toLowerCase();
    }

    // Returns that name of the current operating system
    public static String operatingSystem() {
	String operatingSystem = null;
	try {
	    if (getOS().startsWith("windows")) {
		operatingSystem = "windows";
	    }
	    if (getOS().startsWith("mac")) {
		operatingSystem = "osx";
	    }
	    if (getOS().startsWith("unix")) {
		operatingSystem = "unix";
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	    LOGGER.warning("Unable to determine native Operating System");
	}
	return operatingSystem;
    }

    // Updates Slack channel as Testbot
    private static String slackWebhookAPI = SLACK_WEBHOOK_API;
    private static String curlPOSTFlags = "curl -X POST --data-urlencode ";

    // Send content to a slack channel
    public static void updateSlackTestBot(String content)
	    throws InterruptedException {

	String testReport = "'text': 'Test Report: ";
	String slackChannel = "";
	String payload = "\"payload={" + slackChannel + testReport + content
		+ "'}\" ";

	String testBotCurl = curlPOSTFlags + payload + slackWebhookAPI;
	System.out.println(testBotCurl);
	CommonFunctions.runTerminalCommand(testBotCurl);
    }

    // Create text file if it does not exist and append text
    public static void writeToTextFile(String text, String filename) {
	try {
	    File file = new File(filename);
	    if (!file.exists()) {
		file.createNewFile();
		file.setReadable(true);
		file.setWritable(true);
	    }
	    FileWriter fw = new FileWriter(filename, true);
	    fw.write(text + "\n");
	    fw.close();

	} catch (IOException e) {
	    LOGGER.info("Error creating text file: " + filename);
	    e.printStackTrace();
	}
    }

    // Scans a text file for a matching string
    public static void scanTextFileForMatch(File file, String matchText) {
	LOGGER.info("Scanning " + file.getName() + " to check if is contains: "
		+ matchText);
	boolean isMatch = false;
	try {
	    Scanner scanner = new Scanner(file);
	    int lineNum = 0;
	    while (scanner.hasNextLine()) {
		String line = scanner.nextLine();
		lineNum++;
		if (line.contains(matchText)) {
		    isMatch = true;
		} else {
		    isMatch = false;
		}
	    }
	    scanner.close();
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	    isMatch = false;
	}
	Assert.assertEquals(isMatch, true);

    }

    // Scans a har file for a matching string
    public static void scanHarFileForMatch(Har har, String matchText) {
	File harFile = new File("test.har");
	File harAsText = new File("harAsText.txt");
	try {
	    har.writeTo(harFile);
	    harFile.renameTo(harAsText);
	    CommonFunctions.scanTextFileForMatch(harAsText, matchText);
	    harAsText.delete();
	} catch (IOException e) {
	    harFile.delete();
	    harAsText.delete();
	    e.printStackTrace();
	}
    }

    // Gets the Http response code for a given url
    public static int getHttpResponseCode(String url) {
	int response = 0;
	try {
	    HttpURLConnection.setFollowRedirects(false);
	    HttpURLConnection connection = (HttpURLConnection) new URL(url)
		    .openConnection();
	    connection.setRequestMethod("HEAD");
	    response = connection.getResponseCode();
	    connection.disconnect();
	    return response;
	} catch (Exception e) {
	    e.printStackTrace();
	    return response;
	}
    }

    // Verifies the Http Response code for a given url
    public static void verifyHttpResponseCode(String url,
	    int responseCodeExpected) {
	Assert.assertEquals(getHttpResponseCode(url), responseCodeExpected);
    }
}
