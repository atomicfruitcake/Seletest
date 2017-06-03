package common;

import static common.Properties.SERVERIP;
import static common.Properties.SERVERPASSWORD;
import static common.Properties.SERVERUSERNAME;
import static common.Properties.SLACK_WEBHOOK;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Logger;

import org.apache.commons.lang3.RandomStringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.TestNG;
import org.testng.collections.Lists;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import in.ashwanthkumar.slack.webhook.Slack;
import in.ashwanthkumar.slack.webhook.SlackMessage;
import net.lightbody.bmp.core.har.Har;

/**
 * @author atomicfruitcake
 *
 */
public abstract class CommonFunctions {

    private static final Logger LOGGER = Logger.getLogger(CommonFunctions.class
	    .getName());

    // Gets the URL for a given server
    public static String environmentSelector() {
	Map<String, String> environmentSelect = new HashMap<String, String>();
	environmentSelect.put("ENV_NAME", "ENV_URL");
	environmentSelect.put("ENV2_NAME", "ENV2_URL");
	return environmentSelect.get(SettingsReader.getEnvironment());
    }

    // Gets IP address for server based on ENV
    public String getIpAddressServer() throws IOException {
	Map<String, String> ipSelect = new HashMap<String, String>();
	ipSelect.put("ENV_NAME, ", "ENV_IP");
	ipSelect.put("ENV2_NAME", "ENV2_IP");
	// FIll with all required app servers for server side tests
	return ipSelect.get(SettingsReader.getEnvironment());
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

    // Sets the parameters for the TestNG XML
    public Map<String, String> setTestNGParameters() {
	Map<String, String> TESTPARAMETERS = new HashMap<String, String>();
	TESTPARAMETERS.put("name", "FN Test Suite");
	TESTPARAMETERS.put("preserve-order", "true");
	TESTPARAMETERS.put("parallel", "false");
	TESTPARAMETERS.put("verbose", "10");
	return TESTPARAMETERS;
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
	packages.addElement("package").addAttribute("name", SettingsReader.getTestSuite());
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
     * order is entered and separated by ';' e.g.
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
    public int getHttpResponseCode(String url) {
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
    public void verifyHttpResponseCode(String url,
	    int responseCodeExpected) {
	LOGGER.info("Asserting reponse code for " + url + " is equal to "
		+ responseCodeExpected);
	Assert.assertEquals(getHttpResponseCode(url), responseCodeExpected);
    }

    // Updates Slack if required after running a test suite
    public static void updateSlackAfterSuite(ITestContext testContext)
	    throws IOException {
	if (SettingsReader.getUpdateSlack().equals("yesUpdateSlack")) {
	    LOGGER.info("Updating slack with test suite results");
	    updateSlackTestBot(SettingsReader.getTestSuite() + " has been run on "
		    + SettingsReader.getEnvironment() + "\n" + "\n Passed: "
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
