package vazkii.rem.command;

import java.util.List;

import org.jibble.pircbot.Colors;
import org.jibble.pircbot.PircBot;

public class CommandLmgtfy extends Command {

	public CommandLmgtfy() {
		super("lmgtfy", false);
	}

	@Override
	public void trigger(PircBot bot, String channel, String sender, String login, String hostname, List<String> tokens) {
		if(tokens.size() == 0) {
			bot.sendNotice(sender, Colors.RED + "Invalid Usage: " + Colors.NORMAL + name + " " + getUsage());
			return;
		}

		String link = "http://lmgtfy.com/?q=" + tokens.get(0).replaceAll(" ", "+");
		bot.sendMessage(channel, tokens.size() == 1 ? link : tokens.get(1) + ": " + link);
	}

	@Override
	public String getDescription() {
		return "Gets a Let Me Google That For You link.";
	}

	@Override
	public String getUsage() {
		return "<query> <receiver (o)>";
	}

}
