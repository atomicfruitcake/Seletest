package slackbot;

import java.io.IOException;

/**
 * @author sambass
 *
 */
public class RunSlackBot {
	public static void main(String args[]) throws IOException {
		SlackBot.slackSession();
		SlackBot.registerAListener();		
		while (true) {
			Thread.yield();
		}
	}
}
