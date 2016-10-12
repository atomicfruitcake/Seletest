package common;

import org.openqa.selenium.WebDriver;
import common.CommonFunctions;
public class CommonMethods {
    public static void doAGoogleSearch(WebDriver driver, String searchTerm){
	CommonFunctions.sendKeysToElement(driver, "#lst-ib", searchTerm);
    }
}
