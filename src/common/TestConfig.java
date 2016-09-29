package common;
/*
 * Creates the test config class for handling settings
 */
/**
 * @author atomicfruitcake
 *
 */
public abstract class TestConfig {
	private String environment;
	private String testSuite;
	private String browser;
	private String username;
	private String password;
	private Boolean jiraUpdate;

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getTestSuite() {
		return testSuite;
	}

	public void setTestSuite(String testSuite) {
		this.testSuite = testSuite;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public Boolean getJiraUpdate() {
		return jiraUpdate;
	}

	public void setJiraUpdate(Boolean jiraUpdate) {
		this.jiraUpdate = jiraUpdate;
	}
}
