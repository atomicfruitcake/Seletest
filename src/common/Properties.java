package common;

/**
 * @author atomicfruitcake
 *
 */
public class Properties {
    
    // Default settings
    public static final String DEFAULT_SETTINGS = "IAT \n"
	    + "env"
	    + "username"
	    + "password"
	    + "Phantom"
	    + "NoUpdateJira"
	    + "YesScreenshot"
	    + "test";
    public static final String SETTINGS_FILE = "settings.txt";

    // Login Details
    public static final String USERNAME = CommonFunctions.getUsername();
    public String NEWUSERNAME = CommonFunctions.getRandomUsername();
    public final String NEWUSERNAMEEMAIL = NEWUSERNAME + "@somewhere.com";
    public static final String PASSWORD = CommonFunctions.getPassword();

    // Server Details
    public static final String SERVERIP = "";
    public static final String SERVERUSERNAME = "";
    public static final String SERVERPASSWORD = "";

    // URL list
    public static final String BASE_URL = CommonFunctions.environmentSelector();
    public static final String GOOGLE = "https://www.google.co.uk/";
    public static final String EXAMPLE = "http://www.example.com/";

    // Jira Instance URL for automated updating of test tickets
    public static final String JIRA = "";

    // Browser driver paths
    public static final String PHANTOMJS = "WebDrivers/phantomjs.exe";
    public static final String IE11 = "WebDrivers/IEDriverServer.exe";
    public static final String CHROME = "WebDrivers\\chromedriver.exe";
    public static final String CHROMEOSX = "WebDrivers/chromedriver";
    public static final String FIREFOX = "";

    // Docker Selenium Hub Access
    public static final String DOCKER_SELENIUM = "http://192.168.99.100:4444/wd/hub";

    // Jira Bot login details
    public static final String JIRABOTUSERNAME = "";
    public static final String JIRABOTPASSWORD = "";

    // Mailinator details
    public static final String MAILINATOR_LOGIN = "https://www.mailinator.com/";
    public static final String MAILINATOR_USERNAME = "";
    public static final String MAILINATOR_PASSWORD = "";

    // Slack details
    public static final String SLACK_WEBHOOK = "";

    // Test Packages
    public static final String[] TEST_PACKAGES = new String[] { "Tests" };

    // Google Test Data
    public static final String GOOGLE_SEARCH_TERM = "foo";

    // Slackbot
    public static final String SLACK_BOT_TOKEN = "";
    public static final String SLACK_BOT_CHANNEL = "testbot";
    public static final String SLACK_BOT_ID = "";
    public static final String[] SLACK_BOT_CONFIRMATION = new String[] {
	    "Sure thing, ", "I'm on it, ", "No problem, " };
    public static final String[] SLACK_BOT_ERROR = new String[] {
	    "I'm sorry, I don't understand that, try _testbot help_",
	    "I don't get that. Maybe try _testbot help_" };
    public static final String SLACK_BOT_HELP = "Hi, try something like this \n"
	    + "_@testbot start {TEST SUITE} in {env}_ \n"
	    + "_@testbot stop {TEST SUITE} in {env}_ \n"
	    + "_@testbot status {TEST SUITE} in {env}_ \n \n"
	    + "_@testbot reset_ \n \n"
	    + " The following test suites are supported: \n"

	    + "The following environments are supported: \n";
}
