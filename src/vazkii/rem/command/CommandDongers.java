package vazkii.rem.command;

import java.util.List;

import org.jibble.pircbot.PircBot;

public class CommandDongers extends Command {

	public CommandDongers() {
		super("dongers", false);
	}

	@Override
	public void trigger(PircBot bot, String channel, String sender, String login, String hostname, List<String> tokens) {
		String guy = "\u30FD\u0F3C\u0E88\u0644\u035C\u0E88\u0F3D\uFF89";

		String dongerBuilder = new StringBuilder();
		for(String s : tokens)
			dongerBuilder.append(s).append(" ");

		String dongermsg = dongerBuilder.toString();
		String riot = guy + " RAISE YOUR " + (dongermsg.isEmpty() ? "DONGERS" : dongermsg.toUpperCase()) + guy;
		bot.sendMessage(channel, riot);
	}

	@Override
	public String getDescription() {
		return "RAISE YOUR <thing>";
	}

	@Override
	public String getUsage() {
		return "<thingtoraise (o)>";
	}

}
