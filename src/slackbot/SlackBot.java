package slackbot;

import static common.Properties.SLACK_BOT_CHANNEL;
import static common.Properties.SLACK_BOT_CONFIRMATION;
import static common.Properties.SLACK_BOT_ERROR;
import static common.Properties.SLACK_BOT_HELP;
import static common.Properties.SLACK_BOT_TOKEN;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

import com.ullink.slack.simpleslackapi.SlackAttachment;
import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackPreparedMessage;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;
import common.CommonFunctions;

/**
 * @author sambass
 *
 */
public class SlackBot {
    private static final Logger LOGGER = Logger.getLogger(SlackBot.class
	    .getName());

    static String command;
    static String testPack;
    static String env;
    static SlackSession session;
    static SlackChannel slackChannel;
    static String[] slackResults;

    // Creates a webSocket connection to the Slack instance
    public static void slackSession() throws IOException {
	LOGGER.info("Creating websocket connection to slack");
	session = SlackSessionFactory
		.createWebSocketSlackSession(SLACK_BOT_TOKEN);
	session.connect();
	LOGGER.info("Successfully created connection to slack");
	CommonFunctions.threadSleep(5);
    }

    // Registered a listener to slack channels to see if @testbot is called
    public static void registerAListener() throws IOException {
	SlackMessagePostedListener messagePostedListener = new SlackMessagePostedListener() {
	    @Override
	    public void onEvent(SlackMessagePosted event, SlackSession session) {
		try {
		    slackChannel = event.getChannel();
		} catch (Exception e) {
		    LOGGER.info("ERROR GETTING SLACK CHANNEL");
		    e.printStackTrace();
		}
		if (event.getMessageContent().contains("testbotID")) {
		    runTestbotSlack(event);
		}
	    }
	};
	session.addMessagePostedListener(messagePostedListener);
    }

    // disconnects the websocket connection to slack
    public static void disconnectSlackSession() {
	try {
	    LOGGER.info("Closing connection to Slack");
	    session.disconnect();
	} catch (IOException e) {
	    LOGGER.info("Error closing connection to Slack");
	    e.printStackTrace();
	}
    }

    // Sends a message to a slack channel
    public static void sendMessageToAChannel(String message, String channel)
	    throws IOException {
	LOGGER.info("Sending " + message + " to slack channel " + channel);
	try {
	    session.sendMessageOverWebSocket(slackChannel, message);
	} catch (Exception e) {
	    LOGGER.info("Failed to send slack Message");
	    e.printStackTrace();
	}

    }

    // Sends a message to a user
    public static void sendDirectMessageToAUser(String message, String user)
	    throws IOException {
	LOGGER.info("Sending " + message + " to slack user " + user);
	SlackUser slackUser = session.findUserByUserName(user);
	session.sendMessageToUser(slackUser, message, null);
    }

    // Sends a message with an attachment to channel
    public static void sendUsingPreparedMessage(String channel, String message)
	    throws IOException {
	SlackPreparedMessage preparedMessage = new SlackPreparedMessage.Builder()
		.withMessage(message).withUnfurl(false)
		.addAttachment(new SlackAttachment())
		.addAttachment(new SlackAttachment()).build();
	session.sendMessage(session.findChannelByName(channel), preparedMessage);
    }

    // Fetches the most recent message from a channel
    public static String fetchMostRecentMessageFromChannelHistory(
	    SlackMessagePosted event, String channel) throws IOException {
	LOGGER.info("Fetching most recent message from channel " + channel);
	return event.getMessageContent();

    }

    // Splits a slack message by spaces
    public static String[] splitSlackMessageForTestbot(String slackMessage) {
	try {
	    return slackMessage.split("\\s+");
	} catch (Exception e) {
	    LOGGER.info("Slack command did not include sufficient information");
	    e.printStackTrace();
	    return null;
	}
    }

    public static void readSlackMessage(String slackMessage) {
	String[] splitMessage = splitSlackMessageForTestbot(slackMessage);
	try {
	    command = splitMessage[1].toLowerCase();
	} catch (Exception e) {
	    command = null;
	}
	try {
	    testPack = splitMessage[2];
	} catch (Exception e) {
	    testPack = null;
	}
	try {
	    env = splitMessage[4].toUpperCase();
	    Path path = Paths.get("consoleout.txt");
	    Charset charset = StandardCharsets.UTF_8;
	    String content = new String(Files.readAllBytes(path), charset);
	    content = content.replaceAll("ENV_NAME", env);
	    content = content.replaceAll("ENV2_NAME", env);
	    Files.write(path, content.getBytes(charset));
	} catch (Exception e) {
	    env = null;
	}
    }

    // Responds to a user who talks to @testbot
    public static void sendResponseToSlack(String slackMessage)
	    throws IOException {
	LOGGER.info("Responding to Slack Message : " + slackMessage);
	readSlackMessage(slackMessage);
	switch (command) {
	case "run": {
	    sendMessageToAChannel(SLACK_BOT_CONFIRMATION[(int) Math.random()
		    * SLACK_BOT_CONFIRMATION.length]
		    + "attempting to :arrow_forward: "
		    + testPack
		    + " in "
		    + env, SLACK_BOT_CHANNEL);
	    slackMessage = testPack;
	    try {
		SlackBotTestRunner.executeSlackRunCommand(testPack);
		sendMessageToAChannel(
			testPack + "successfully exectued :peng:",
			SLACK_BOT_CHANNEL);
	    } catch (InterruptedException e) {
		sendMessageToAChannel("Something went wrong :scream:",
			SLACK_BOT_CHANNEL);
		sendMessageToAChannel(SLACK_BOT_ERROR[(int) Math.random()
			* SLACK_BOT_ERROR.length], SLACK_BOT_CHANNEL);
		LOGGER.info("Failed to execute Slack Run command to testbot");
		e.printStackTrace();
	    }
	    break;
	}

	case "status": {
	    sendMessageToAChannel(SLACK_BOT_CONFIRMATION[(int) Math.random()
		    * SLACK_BOT_CONFIRMATION.length]
		    + "currently there are +"
		    + "N "
		    + "passed, "
		    + "N "
		    + "failed, "
		    + "N "
		    + "blocked and "
		    + "N "
		    + "not executed tests in " + env + " for " + testPack,
		    SLACK_BOT_CHANNEL);
	    break;
	}

	case "stop": {
	    sendMessageToAChannel(SLACK_BOT_CONFIRMATION[(int) Math.random()
		    * SLACK_BOT_CONFIRMATION.length]
		    + ":black_square_for_stop: stopping tests" + " in " + env,
		    SLACK_BOT_CHANNEL);
	    SlackBotTestRunner.executeSlackStopCommand(slackResults);
	    break;
	}
	case "hi": {
	    sendMessageToAChannel("Hi ", SLACK_BOT_CHANNEL);
	    break;
	}

	case "foo": {
	    sendMessageToAChannel("bar ", SLACK_BOT_CHANNEL);
	    break;
	}

	case "doabarrelroll": {
	    sendMessageToAChannel("YOU MUST CONSTRUCT ADDITIONAL PYLONS",
		    SLACK_BOT_CHANNEL);
	    break;
	}

	case "hello": {
	    sendMessageToAChannel("Well hello to you too ", SLACK_BOT_CHANNEL);
	    break;
	}

	case "help": {
	    sendMessageToAChannel(SLACK_BOT_HELP, SLACK_BOT_CHANNEL);
	    break;
	}

	case "reset": {
	    sendMessageToAChannel(
		    "Resetting my settings, let's hope this fixes me",
		    SLACK_BOT_CHANNEL);
	    SlackBotTestRunner.resetSlackTestBot();
	    sendMessageToAChannel("I've successfully reset myself",
		    SLACK_BOT_CHANNEL);
	    break;
	}
	default: {
	}
	    sendMessageToAChannel(SLACK_BOT_ERROR[(int) Math.random()
		    * SLACK_BOT_ERROR.length], SLACK_BOT_CHANNEL);
	    break;
	}
    }

    // Runs the testbot when a listener hears that @testbot is called
    public static void runTestbotSlack(SlackMessagePosted event) {
	try {
	    LOGGER.info("Responding to message on Slack");
	    sendResponseToSlack(fetchMostRecentMessageFromChannelHistory(event,
		    event.getChannel().getName()));
	} catch (IOException e) {
	    e.printStackTrace();
	    try {
		sendResponseToSlack(SLACK_BOT_HELP);
	    } catch (IOException f) {
		LOGGER.info("Test bot did not understand command, sending help message");
		f.printStackTrace();
	    }
	    LOGGER.severe("Unable to run the testbot");

	}
    }
}
