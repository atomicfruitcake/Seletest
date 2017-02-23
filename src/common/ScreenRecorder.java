package common;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;


public class ScreenRecorder {

    private ScreenRecorder screenRecorder;

    public void startRecording() throws Exception {

	GraphicsConfiguration graphicsConfiguration = GraphicsEnvironment
		.getLocalGraphicsEnvironment().getDefaultScreenDevice()
		.getDefaultConfiguration();
	
	screenRecorder = new ScreenRecorder();
	
	screenRecorder.startRecording();
    }

    public void stopRecording() throws Exception {
	screenRecorder.stopRecording();
    }
}
