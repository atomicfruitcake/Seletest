package common;

import java.io.IOException;

/**
 * @author atomicfruitcake
 *
 */
public class RunTests {

    // Builds and runs the Test Suite
    public static void main(String[] args) throws InterruptedException,
	    IOException {

	CommonFunctions.buildTestngXml();

	CommonFunctions.runTestngXml();
    }
}