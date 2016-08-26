package vazkii.rem.command;

import java.util.List;

import org.jibble.pircbot.PircBot;

public class CommandRiot extends Command {

	public CommandRiot() {
		super("riot", false);
	}

	@Override
	public void trigger(PircBot bot, String channel, String sender, String login, String hostname, List<String> tokens) {
		String guy = "\u0F3C \u3064 \u25D5_\u25D5 \u0F3D\u3064";

		StringBuilder riotBuilder = new StringBuilder();
		for(String s : tokens)
			riotBuilder.append(s).append(" ");

		String riotmsg = riotBuilder.toString();
		String riot = guy + " " + (riotmsg.isEmpty() ? "RIOT" : (riotmsg.toUpperCase() + "OR RIOT")) + " " + guy;
		bot.sendMessage(channel, riot);
	}

	@Override
	public String getDescription() {
		return "RIOT";
	}

	@Override
	public String getUsage() {
		return "<riot (o)>";
	}

}
