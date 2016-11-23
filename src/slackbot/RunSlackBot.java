package slackbot;

import java.io.IOException;

import org.testng.annotations.Test;

/**
 * @author sambass
 *
 */
@Test
public class RunSlackBot {
	public void slackbotTest() throws IOException {
		SlackBot.slackSession();
		SlackBot.registerAListener();		
		while (true) {
			Thread.yield();
		}
	}
}
