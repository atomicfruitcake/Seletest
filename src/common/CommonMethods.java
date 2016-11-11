package common;

import org.openqa.selenium.WebDriver;
import common.CommonFunctions;
public class CommonMethods {
    
    // Searches Google for 'searchTerm'
    public static void doAGoogleSearch(WebDriver driver, String searchTerm){
	
	// Enter search term in search field
	CommonFunctions.sendKeysToElement(driver, "[name='q']", searchTerm);
    
	// Search for the given search term
	CommonFunctions.clickElement(driver, "#sblsbb button span");
    }
}
