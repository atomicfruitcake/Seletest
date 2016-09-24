package AllTests;

/**
 * @author atomicfruitcake
 *
 */
public class Properties {

	// Login Details
	public static final String USERNAME = TestConfigImpl.testConfig
			.getUsername();
	public String NEWUSERNAME = CommonFunctions.getRandomUsername();
	public final String NEWUSERNAMEEMAIL = NEWUSERNAME + "@mailinator.com";
	public static final String PASSWORD = TestConfigImpl.testConfig
			.getPassword();

	// URL list
	public static final String URL = CommonFunctions.environmentSelector();
	
	// Jira Instance URL for automated updating of test tickets
	public static final String JIRA = "";

	// Browser driver paths
	public static final String PHANTOMJS = "WebDrivers\\phantomjs.exe";
	public static final String IE11 = "WebDrivers\\IEDriverServer.exe";
	public static final String CHROME = "WebDrivers\\chromedriver.exe";
	public static final String CHROMEOSX = "WebDrivers\\chromedriver";
	public static final String FIREFOX = "";
	
	//Jira Bot login details
	public static final String JIRABOTUSERNAME = "";
	public static final String JIRABOTPASSWORD = "";

	// Test Packages
	public static final String[] R1_REGRESSION_PACKAGES = new String[] {
			"Package1", "Package2" };
}
