package vazkii.rem;

import java.util.List;

public class Config {

	public boolean verboseLogging;
	public IRCInfo ircInfo;
	public Features features;
	public TwitterOAuth twitterOAuth;
	public List<String> otherChannels;
	public List<CommandInfo> customCommands;

	public static class IRCInfo {
		public String name, login, display, password, server, homeChannel, encoding, commandPrefix, 
		missingDescMessage, partMessage, quitMessage, helpCommand, helpMessage;
		public int delay;
	}

	public static class Features {
		public boolean twitter, otherChannels, slowMode, findAndReplace, tableUnflip;
		public Commands builtInCommands;

		public static class Commands {
			public boolean consult, lmgtfy, roll, coinflip, dongers, riot, flip, mcstatus;
		}
	}

	public static class TwitterOAuth {
		public String consumerKey, consumerKeySecret, accessToken, accessTokenSecret;
	}

	public static class CommandInfo {
		public String command, description;
		public List<String> replies = null;
		public boolean opOnly;
	}

}
