package common;

import java.util.logging.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import io.selendroid.SelendroidCapabilities;
import io.selendroid.SelendroidConfiguration;
import io.selendroid.SelendroidDriver;
import io.selendroid.SelendroidLauncher;

/**
 * @author atomicfruitcake
 *
 */
public class SelendroidBasePage {
    
    private static final Logger LOGGER = Logger.getLogger(SelendroidBasePage.class
		.getName());
    
    private SelendroidLauncher selendroidServer = null;
    private WebDriver driver = null;
    
    @BeforeMethod
    public void startUp() throws Exception{
	LOGGER.info("Starting selendroid driver");
	if (selendroidServer != null) {
	    selendroidServer.stopSelendroid();
	}
	SelendroidConfiguration config = new SelendroidConfiguration();
	selendroidServer = new SelendroidLauncher(config);
	selendroidServer.launchSelendroid
	DesiredCapabilities desiredCapabilities = SelendroidCapabilities.android();
	driver = new SelendroidDriver(desiredCapabilities);
    }
    
    @AfterMethod
    public void tearDown(){
	if(driver != null){
	    driver.quit();
	}
	if (selendroidServer != null) {
	    selendroidServer.stopSelendroid();
	}
    }
}
