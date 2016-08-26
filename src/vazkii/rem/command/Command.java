package vazkii.rem.command;

import java.util.ArrayList;
import java.util.List;

import org.jibble.pircbot.Colors;
import org.jibble.pircbot.PircBot;

import vazkii.rem.Rem;

public abstract class Command {

	private static final String SPLIT_REGEX = " (?=([^\"]*\"[^\"]*\")*[^\"]*$)";

	public final String name;
	public final boolean requiresOp;

	public Command(String name, boolean requiresOp) {
		this.name = Rem.config.ircInfo.commandPrefix + name;
		this.requiresOp = requiresOp;
	}

	public boolean shouldTrigger(String channel, String command, boolean op) {
		String[] tokens = command.split(SPLIT_REGEX);
		return tokens.length > 0 && tokens[0].equalsIgnoreCase(name) && (!requiresOp || op);
	}

	public void trigger(PircBot bot, String channel, String sender, String login, String hostname, String message) {
		String[] tokens = message.split(SPLIT_REGEX);
		if(tokens.length == 0)
			return;

		List<String> ctokens = new ArrayList<String>();
		for(int i = 1; i < tokens.length; i++)
			ctokens.add(tokens[i].replaceAll("\"", ""));

		trigger(bot, channel, sender, login, hostname, ctokens);
	}

	public String getDisplayDescription() {
		return getDescription() + Colors.BOLD + Colors.YELLOW + " Usage: " + Colors.NORMAL + name + " " + getUsage();
	}

	public String getDescription() {
		return Rem.config.ircInfo.missingDescMessage;
	}

	public String getUsage() {
		return "";
	}

	public boolean isHidden() {
		return false;
	}

	public abstract void trigger(PircBot bot, String channel, String sender, String login, String hostname, List<String> tokens);
}
