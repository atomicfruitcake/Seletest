package AllTests;

import AllTests.TestConfig;

public class TestConfigImpl extends TestConfig {

	static TestConfig testConfig;

	public static void TestConfigImplObj() {
		testConfig.setEnvironment("ENV");
		testConfig.setBrowser("browser");
		testConfig.setUsername("username");
		testConfig.setPassword("password");
		testConfig.setTestSuite("testSuite");
	}
}