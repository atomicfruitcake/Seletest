package common;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;


public class ScreenRecorder {

    private static ScreenRecorder screenRecorder;

    public static void startRecording() throws Exception {

	GraphicsConfiguration graphicsConfiguration = GraphicsEnvironment
		.getLocalGraphicsEnvironment().getDefaultScreenDevice()
		.getDefaultConfiguration();

	screenRecorder = new ScreenRecorder();
	ScreenRecorder.startRecording();
    }

    public static void stopRecording() throws Exception {
	ScreenRecorder.stopRecording();
    }
}
