package common;

import static common.Properties.*;
import in.ashwanthkumar.slack.webhook.Slack;
import in.ashwanthkumar.slack.webhook.SlackMessage;

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
import org.testng.ITestContext;
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
public abstract class CommonFunctions {

    private static final Logger LOGGER = Logger.getLogger(CommonFunctions.class
	    .getName());

    static JavascriptExecutor js;

    // Reads Data from the settings.txt file
    public static String readLineFromFile(String filename, int lineNumber)
	    throws IOException {
	try {
	    return Files.readAllLines(Paths.get(filename)).get(lineNumber);
	} catch (Exception e) {
	    e.printStackTrace();
	    return null;
	}
    }

    // Get settings from the settings.txt file
    public static String getSettings(int entryNo) {
	try {
	    return readLineFromFile("settings.txt", entryNo);
	} catch (IOException e) {
	    throw new RuntimeException(
		    "Error fetching data from settings.txt for line " + entryNo,
		    e);
	}
    }

    // Get environment name from settings file
    public static String getEnvironment() {
	return getSettings(0).replaceAll("\\s+", "");
    }

    // Gets the username from the settings file
    public static String getUsername() {
	return getSettings(1).replaceAll("\\s+", "");
    }

    // Gets the password from the settings file
    public static String getPassword() {
	return getSettings(2).replaceAll("\\s+", "");
    }

    // Gets the browser from the settings file
    public static String getBrowser() {
	return getSettings(3).replaceAll("\\s+", "");
    }

    // Gets the update jira setting from the settings file
    public static String getUpdateJira() {
	return getSettings(4).replaceAll("\\s+", "");
    }

    // Gets the screenshot setting from the settings file
    public static String getScreenshotToggle() {
	return getSettings(5).replaceAll("\\s+", "");
    }

    // Gets the test suite setting from the settings file
    public static String getTestSuite() {
	return getSettings(6).replaceAll("\\s+", "");
    }

    // Gets the slack update setting from the settings file
    public static String getUpdateSlack() {
	return getSettings(7).replaceAll("\\s+", "");
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
	environmentSelect.put("ENV2_NAME", "ENV2_URL");
	return environmentSelect.get(getEnvironment());
    }

    // Gets IP address for server based on ENV
    public static String getIpAddressServer() throws IOException {
	Map<String, String> ipSelect = new HashMap<String, String>();
	ipSelect.put("ENV_NAME, ", "ENV_IP");
	ipSelect.put("ENV2_NAME", "ENV2_IP");
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

    public static void defaultStringReturner(String string) {
	if ((string == null) || string.isEmpty() == true) {
	    string = "Filled by default to prevent null pointer exception";
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
	if (getScreenshotToggle().toLowerCase() == "yesscreenshot") {
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

    // Builds the TestNG XML file
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
	packages.addElement("package").addAttribute("name", getTestSuite());
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

    public static void updateSlackTestBot(String content) {
	LOGGER.info("Updating Slack " + content);
	try {
	    new Slack(SLACK_WEBHOOK).icon(":smile:").sendToChannel("")
		    .displayName("testbot").push(new SlackMessage(content));
	} catch (Exception e) {
	    LOGGER.warning("unable to connect to slack");
	    e.printStackTrace();
	}
    }

    /*
     * Runs a terminal command from string input. Can run multiple commands in
     * order is entered and seperated by ';' e.g.
     * "cd Documents;touch newfile;rm -f newfile"
     */
    public static void runTerminalCommand(String curlCommand)
	    throws InterruptedException {
	LOGGER.info("Running " + curlCommand + " in console");
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

    // Tunnels into server to run Commands
    public static void tunnelIntoServer(String shellCommand, int port)
	    throws JSchException, IOException {
	LOGGER.info("Tunneling into server to run command: " + shellCommand);
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
	LOGGER.info("Checking Operating System");
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
	LOGGER.info("Operating System is " + operatingSystem);
	return operatingSystem;
    }

    // Create text file if it does not exist and append text
    public static void writeToTextFile(String text, String filename) {
	LOGGER.info("Writing " + text + " to " + filename);
	try {
	    File file = new File(filename);
	    if (!file.exists()) {
		LOGGER.info(filename + " does not exist. Building file "
			+ filename);
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
	    while (scanner.hasNextLine()) {
		String line = scanner.nextLine();
		if (line.contains(matchText)) {
		    isMatch = true;
		} else {
		    isMatch = false;
		}
	    }
	    scanner.close();
	} catch (FileNotFoundException e) {
	    LOGGER.info("Could not find file " + file.getName());
	    e.printStackTrace();
	    isMatch = false;
	}
	Assert.assertEquals(isMatch, true);

    }

    // Scans a har file for a matching string
    public static void scanHarFileForMatch(Har har, String matchText) {
	LOGGER.info("Scanning har file for text " + matchText);
	File harFile = new File("test.har");
	File harAsText = new File("harAsText.txt");
	try {
	    har.writeTo(harFile);
	    harFile.renameTo(harAsText);
	    CommonFunctions.scanTextFileForMatch(harAsText, matchText);
	    harAsText.delete();
	} catch (IOException e) {
	    LOGGER.info("Unable to find har");
	    harFile.delete();
	    harAsText.delete();
	    e.printStackTrace();
	}
    }

    // Gets the Http response code for a given url
    public static int getHttpResponseCode(String url) {
	LOGGER.info("Getting URL response code for " + url);
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
	    LOGGER.info("Unable to establish HTTP URL connection to " + url);
	    e.printStackTrace();
	    return response;
	}
    }

    // Verifies the Http Response code for a given url
    public static void verifyHttpResponseCode(String url,
	    int responseCodeExpected) {
	LOGGER.info("Asserting reponse code for " + url + " is equal to "
		+ responseCodeExpected);
	Assert.assertEquals(getHttpResponseCode(url), responseCodeExpected);
    }

    // Updates Slack if required after running a test suite
    public static void updateSlackAfterSuite(ITestContext testContext)
	    throws IOException {
	if (getUpdateSlack().equals("yesUpdateSlack")) {
	    LOGGER.info("Updating slack with test suite results");
	    updateSlackTestBot(getTestSuite() + " has been run on "
		    + getEnvironment() + "\n" + "\n Passed: "
		    + testContext.getPassedTests().size() + "\n Failed: "
		    + testContext.getFailedTests().size() + "\n Skipped: "
		    + testContext.getSkippedTests().size());
	} else {
	    LOGGER.info("Slack updater switched off");
	}
    }

    public static void createTestBot() {
	System.out.println("###########################################");
	System.out.println("###########################################");
	System.out.println("#                                         #");
	System.out.println("#            Building TestBot             #");
	System.out.println("#                                         #");
	System.out.println("###########################################");
	System.out.println("###########################################");
	System.out.println("   _______                   ________    |");
	System.out.println("  |ooooooo|      ____       | __  __ |   |");
	System.out.println("  |[]+++[]|     [____]      |/  \\/  \\|   |");
	System.out.println("  |+ ___ +|     ]()()[      |\\__/\\__/|   |");
	System.out.println("  |:|   |:|   ___\\__/___    |[][][][]|   |");
	System.out.println("  |:|___|:|  |__|    |__|   |++++++++|   |");
	System.out.println("  |[]===[]|   |_|_/\\_|_|    | ______ |   |");
	System.out.println("_ ||||||||| _ | | __ | | __ ||______|| __|");
	System.out.println("  |_______|   |_|[::]|_|    |________|   \\");
	System.out.println("              \\_|_||_|_/                  \\");
	System.out.println("	        |_||_|                     \\");
	System.out.println("               _|_||_|_                     \\");
	System.out.println("	  ___ |___||___|                     \\");
	System.out.println("	 /  __\\          ____                 \\");
	System.out.println("	 \\( oo          (___ \\                 \\");
	System.out.println("	 _\\_o/           oo~)/");
	System.out.println("	/ \\|/ \\         _\\-_/_");
	System.out.println("       / / __\\ \\___    / \\|/  \\");
	System.out.println("       \\ \\|   |__/_)  / / .- \\ \\");
	System.out.println("	\\/_)  |       \\ \\ .  /_/");
	System.out.println("	 ||___|        \\/___(_/");
	System.out.println("	 | | |          | |  |");
	System.out.println("	 | | |          | |  |");
	System.out.println("	 |_|_|          |_|__|");
	System.out.println("	 [__)_)        (_(___]");
    }
}
