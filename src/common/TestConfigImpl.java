package common;

import common.TestConfig;
/**
 * @author atomicfruitcake
 *
 */
public class TestConfigImpl extends TestConfig {

	static TestConfig testConfig;

	public static void TestConfigImplObj() {
		testConfig.setEnvironment("ENV");
		testConfig.setBrowser("browser");
		testConfig.setUsername("username");
		testConfig.setPassword("password");
		testConfig.setTestSuite("testSuite");
		testConfig.setJiraUpdate(true);
	}
}