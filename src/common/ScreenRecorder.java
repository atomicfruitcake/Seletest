package common;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import org.monte.screenrecorder.ScreenRecorder;
import org.monte.media.math.Rational;
import org.monte.media.Format;
import static org.monte.media.AudioFormatKeys.*;
import static org.monte.media.VideoFormatKeys.*;

public class ScreenRecorder {

    private ScreenRecorder screenRecorder;

    public void startRecording() throws Exception {

	GraphicsConfiguration graphicsConfiguration = GraphicsEnvironment
		.getLocalGraphicsEnvironment().getDefaultScreenDevice()
		.getDefaultConfiguration();
	
	screenRecorder = new ScreenRecorder(graphicsConfiguration, new Format(MediaTypeKey,
		MediaType.FILE, MimeTypeKey, MIME_QUICKTIME), new Format(
		MediaTypeKey, MediaType.VIDEO, EncodingKey,
		ENCODING_QUICKTIME_JPEG, CompressorNameKey,
		ENCODING_QUICKTIME_JPEG, DepthKey, (int) 24, FrameRateKey,
		Rational.valueOf(15), QualityKey, 1.0f, KeyFrameIntervalKey,
		(int) (15 * 60)), new Format(MediaTypeKey, MediaType.VIDEO,
		EncodingKey, "black", FrameRateKey, Rational.valueOf(30)), null);
	
	screenRecorder.startRecording();
    }

    public void stopRecording() throws Exception {
	screenRecorder.stopRecording();
    }
}
