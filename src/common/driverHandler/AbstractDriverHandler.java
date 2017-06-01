package common.driverHandler;

import java.io.IOException;
import java.lang.reflect.Method;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

/**
 * @author sambass
 *
 */
public abstract interface AbstractDriverHandler {

    @BeforeSuite
    public void beforeSuite();

    @BeforeMethod(alwaysRun = true)
    public void startup() throws IOException, Exception;

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result, Method method)
	    throws Exception, IOException, InterruptedException;

    @AfterSuite(alwaysRun = true)
    public void afterSuite(ITestContext testContext) throws IOException;
}
