package AllTests;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

import javax.xml.bind.DatatypeConverter;

import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;

/**
 * @author sambass
 *
 */
public class JIRAUpdater {

	private static final Logger LOGGER = Logger.getLogger(JIRAUpdater.class
			.getName());

	// Strings to concatenate for cURL command
	static String curlFlags = "curl -D- -X POST -H ";
	static String curlFlagsQuery = "curl -D- -X GET -H ";
	static String curlAuth = "\"Authorization: Basic ";
	static String usernameJIRA = "JIRA username";
	static String passwordJIRA = "JIRA password"; 
	static String dataFlag = "\" --data ";
	static String contentType = " -H \"Content-Type: application/json\" ";
	static String baseURL = "jiraBaseUrl";
	static String apiLatest = "rest/api/latest/issue/";
	static String apiIssue = "rest/api/2/issue/";
	static String transitionOption = "/transitions?expand=transitions.fields";
	static String commentOption = "/comment";
	static String testbot = "testbot";

	public static String b64EncodedCredentials() {
		return (DatatypeConverter
				.printBase64Binary((usernameJIRA + ":" + passwordJIRA)
						.getBytes()));
	}

	public static void getLatestJIRA(String jiraID) throws IOException,
			InterruptedException {
		// cURL command to get overview of ticket as a JSON
		String getLatestCurl = curlFlagsQuery + curlAuth + b64EncodedCredentials()
				+ contentType + baseURL + apiLatest + jiraID;

		System.out.println(getLatestCurl);
		CommonFunctions.runTerminalCommand(getLatestCurl);
	}

	public static void updateTicketJIRA(String comment, String jiraID)
			throws IOException, InterruptedException {
		String commentJSON = "\"{\\\"body\\\":\\\"" + comment + "\\\"}\"";
		String commentCurl = curlFlags + curlAuth + b64EncodedCredentials()
				+ dataFlag + commentJSON + contentType + baseURL + apiIssue
				+ jiraID + commentOption;
		System.out.println(commentCurl);
		CommonFunctions.runTerminalCommand(commentCurl);
	}

	public static void transitionTicketJIRA(int moveToID, String jiraID)
			throws IOException, InterruptedException {
		String transitionJSON = "\"{\\\"transition\\\":{\\\"id\\\":\\\"";
		Integer.toString(moveToID);
		String updateJSON = transitionJSON + moveToID + "\\\"}}\"";
		String transitionCurl = curlFlags + curlAuth + b64EncodedCredentials()
				+ dataFlag + updateJSON + contentType + baseURL + apiLatest
				+ jiraID + transitionOption;

		System.out.println(transitionCurl);
		CommonFunctions.runTerminalCommand(transitionCurl);

		/*
		 * These settings will changes based upon JIRA configuration They can be
		 * found by using the getLatestJIRA curl.
		 * 
		 * moveToID notes 11014 = Ready for Execution 41 = In Progress 51 =
		 * Passed 61 = Failed 81 = Retest
		 */

	}

	public static void assignToTestBot(String jiraID) throws IOException,
			InterruptedException {
		String assignJSON = "\"{\\\"fields\\\":{\\\"assignee\\\":{\\\"name\\\":\\\""
				+ testbot + "\\\"}}}\"";
		String assignCurl = curlFlags + curlAuth + b64EncodedCredentials()
				+ dataFlag + assignJSON + contentType + baseURL + apiLatest
				+ jiraID;

		System.out.println(assignCurl);
		CommonFunctions.runTerminalCommand(assignCurl);
	}

	public static void resetTicket(String jiraID) throws IOException,
			InterruptedException {
		try {
			transitionTicketJIRA(81, jiraID);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			transitionTicketJIRA(11041, jiraID);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void PassTicket(String jiraID) throws IOException,
			InterruptedException {
		resetTicket(jiraID);
		transitionTicketJIRA(41, jiraID);
		transitionTicketJIRA(51, jiraID);
		updateTicketJIRA("Tested by Automation and passed on "
				+ TestConfigImpl.testConfig.getEnvironment(), jiraID);
	}

	public static void FailTicket(String jiraID) throws IOException,
			InterruptedException {
		resetTicket(jiraID);
		transitionTicketJIRA(41, jiraID);
		transitionTicketJIRA(61, jiraID);
		updateTicketJIRA("Tested by Automation and failed on "
				+ TestConfigImpl.testConfig.getEnvironment(), jiraID);
	}

	/*
	 * Requires test method name to be equal to the JIRA ID
	 */
	public static void updateJiraTicket(ITestResult result, Method method,
			WebDriver driver) throws Exception, IOException,
			InterruptedException {
		String testName = method.getName();
		@SuppressWarnings(value = {})
		String jiraTicketFull = testName.toString();

		switch (CommonFunctions.getJiraUpdateSetting()) {
		case "Yes":
			if (result.getStatus() == ITestResult.FAILURE) {
				JIRAUpdater.FailTicket(jiraTicketFull);
				LOGGER.info(jiraTicketFull + " : FAILED");
				CommonFunctions.screenshot(jiraTicketFull + " Fail", driver);

			} else if (result.getStatus() == ITestResult.SUCCESS) {
				JIRAUpdater.PassTicket(jiraTicketFull);
				LOGGER.info(jiraTicketFull + " : PASSED");
			}
		case "No":
			if (result.getStatus() == ITestResult.FAILURE) {
				LOGGER.info(jiraTicketFull + " : FAILED");
				CommonFunctions.screenshot(jiraTicketFull + " Fail", driver);

			} else if (result.getStatus() == ITestResult.SUCCESS) {
				LOGGER.info(jiraTicketFull + " : PASSED");
			}
		}
	}
}
