package common;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
//import org.monte.screenrecorder.ScreenRecorder;

public class ScreenRecorder {
    

    public void startRecording() throws Exception
    {
        GraphicsConfiguration gc = GraphicsEnvironment
            .getLocalGraphicsEnvironment()
            .getDefaultScreenDevice()
            .getDefaultConfiguration();
    }
}
