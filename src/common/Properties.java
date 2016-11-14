package common;

/**
 * @author atomicfruitcake
 *
 */
public class Properties {

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
	public static final String PHANTOMJS = "";
	public static final String IE11 = "";
	public static final String CHROME = "WebDrivers\\chromedriver.exe";
	public static final String CHROMEOSX = "WebDrivers/chromedriver";
	public static final String FIREFOX = "";
	
	// Docker Selenium Hub Access
	public static final String DOCKER_SELENIUM = "http://192.168.99.100:4444/wd/hub";
	
	// Jira Bot login details
	public static final String JIRABOTUSERNAME = "";
	public static final String JIRABOTPASSWORD = "";

	// Slack details
	public static final String SLACK_WEBHOOK_API = "";

	// Test Packages
	public static final String[] TEST_PACKAGES = new String[] {"Tests"};

	// Google Test Data
	public static final String GOOGLE_SEARCH_TERM = "foo";
}
