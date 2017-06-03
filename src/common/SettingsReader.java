package common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SettingsReader {
    
    public static String readLineFromFile(String filename, int lineNumber)
	    throws IOException {
	try {
	    return Files.readAllLines(Paths.get(filename)).get(lineNumber);
	} catch (Exception e) {
	    e.printStackTrace();
	    return null;
	}
    }

    public static String getSettings(int entryNo) {
	try {
	    return readLineFromFile("settings.txt", entryNo);
	} catch (IOException e) {
	    throw new RuntimeException(
		    "Error fetching data from settings.txt for line " + entryNo,
		    e);
	}
    }

    public static String getEnvironment() {
	return getSettings(0).replaceAll("\\s+", "");
    }

    public static String getUsername() {
	return getSettings(1).replaceAll("\\s+", "");
    }

    public static String getPassword() {
	return getSettings(2).replaceAll("\\s+", "");
    }

    public static String getBrowser() {
	return getSettings(3).replaceAll("\\s+", "");
    }

    public static String getUpdateJira() {
	return getSettings(4).replaceAll("\\s+", "");
    }

    public static String getScreenshotToggle() {
	return getSettings(5).replaceAll("\\s+", "");
    }

    public static String getTestSuite() {
	return getSettings(6).replaceAll("\\s+", "");
    }

    public static String getUpdateSlack() {
	return getSettings(7).replaceAll("\\s+", "");
    }
}
