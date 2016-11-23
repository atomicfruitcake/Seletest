package slackbot;

import static common.Properties.DEFAULT_SETTINGS;
import static common.Properties.SETTINGS_FILE;
import common.CommonFunctions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.testng.TestNG;
import org.testng.collections.Lists;

/**
 * @author sambass
 *
 */
public class SlackBotTestRunner {

    public static boolean testStop;
    static String slackTestPack;
    static String slackTestEnvironment;

    private static final Logger LOGGER = Logger
	    .getLogger(SlackBotTestRunner.class.getName());

    // Builds a TestNG XML with settings from Slack testbot
    public static void buildTestngXmlSlack(String testPack) throws IOException {
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

	// Build test suite based on selected options
	Element packages = test.addElement("packages");
	packages.addElement("package").addAttribute("name", testPack);

	FileOutputStream fos = new FileOutputStream("testng.xml");
	OutputFormat format = OutputFormat.createPrettyPrint();
	XMLWriter writer = new XMLWriter(fos, format);
	writer.write(document);
	writer.flush();
    }

    // Runs a TestNG XML with settings from Slack testbot
    public static void runTestngXmlSlack() throws InterruptedException {
	LOGGER.info("Running TestNG XML file");
	TestNG testng = new TestNG();
	List<String> suites = Lists.newArrayList();
	suites.add("testng.xml");
	testng.setTestSuites(suites);
	testng.run();
    }

    // Builds a TestNG XML with settings from Slack testbot
    public static void executeSlackRunCommand(String slackResults)
	    throws IOException, InterruptedException {
	testStop = false;
	LOGGER.info("Executing slack Run command to run tests");
	buildTestngXmlSlack(slackResults);
	while (testStop == false) {
	    runTestngXmlSlack();
	}
    }

    // Stops testbot from executing tests
    public static void executeSlackStopCommand(String[] slackResults) {
	testStop = true;
    }

    // Resets the consoleout.txt settings
    public static void resetSlackTestBot() {
	LOGGER.info("Resetting testbot settings");
	String filename = SETTINGS_FILE;
	File file = new File(filename);
	file.delete();
	CommonFunctions.writeToTextFile(DEFAULT_SETTINGS, filename);
    }
}
