package common;

import static common.Properties.MAILINATOR_PASSWORD;
import static common.Properties.MAILINATOR_LOGIN;
import static common.Properties.MAILINATOR_USERNAME;
import static common.Properties.VALIDATION_EMAIL_LINK;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import common.CommonFunctions;

public abstract class CommonMethods {

    private static final Logger LOGGER = Logger.getLogger(CommonMethods.class
	    .getName());

    // Searches Google for 'searchTerm'
    public static void doAGoogleSearch(WebDriver driver, String searchTerm) {

	// Enter search term in search field
	CommonFunctions.sendKeysToElement(driver, "[name='q']", searchTerm);

	// Search for the given search term
	CommonFunctions.clickElement(driver, "#_fZl");
    }

    public static void loginToMailinator(WebDriver driver) {
	LOGGER.info("Logging in to Mailinator");

	// Start Mailinator
	CommonFunctions.startBrowser(driver, MAILINATOR_LOGIN);

	// Go to login
	CommonFunctions
		.clickElement(driver,
			"#header-1 nav div.collapse.navbar-collapse ul li:nth-child(6) a");

	// Enter username
	CommonFunctions.sendKeysToElement(driver, "#loginEmail",
		MAILINATOR_USERNAME);

	// Enter Password
	CommonFunctions.sendKeysToElement(driver, "#loginPassword",
		MAILINATOR_PASSWORD);

	// Click Sign In
	CommonFunctions.clickElement(driver,
		"#loginpane div div.modal-body div button");

    }

    public static void openMailInMailinator(WebDriver driver, String newUsername) {
	LOGGER.info("Searching for confirmation email");

	CommonFunctions.clearTextFromElement(driver, "#publicinboxfield");
	CommonFunctions.sendKeysToElement(driver, "#publicinboxfield",
		newUsername);

	// Search for emails
	LOGGER.info("Opening confirmation email");
	CommonFunctions
		.clickElement(driver,
			"#publicInboxCtrl div:nth-child(1) div:nth-child(3) div div button");
	CommonFunctions.threadSleep(3);
    }

    public static void respondToMailInMailinator(WebDriver driver) {
	LOGGER.info("Responding to validation email");

	driver.findElement(By.className("oddrow_public")).click();
	
	CommonFunctions.threadSleep(2);

	driver.switchTo().frame("publicshowmaildivcontent");

	// Click on validation link
	CommonFunctions
		.clickElement(
			driver,
			VALIDATION_EMAIL_LINK);

	// Switch window to confimation page.
	for (String winHandle : driver.getWindowHandles()) {
	    driver.switchTo().window(winHandle);
	}
    }

    public static void getMailFromMailinator(WebDriver driver, String username) {

	// Login to Mailinator
	loginToMailinator(driver);

	// Open Mailbox
	openMailInMailinator(driver, username);

	// Respond to confirmation email
	respondToMailInMailinator(driver);
    }
}
