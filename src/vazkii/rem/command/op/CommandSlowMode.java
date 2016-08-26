package vazkii.rem.command.op;

import java.util.List;

import org.jibble.pircbot.Colors;
import org.jibble.pircbot.PircBot;

import vazkii.rem.Rem;
import vazkii.rem.command.Command;

public class CommandSlowMode extends Command {

	public CommandSlowMode() {
		super("slowmode", true);
	}

	@Override
	public void trigger(PircBot bot, String channel, String sender, String login, String hostname, List<String> tokens) {
		if(tokens.size() > 0) try {
			Rem.slowMode = Integer.parseInt(tokens.get(0));
		} catch (NumberFormatException e) { }
		bot.sendMessage(channel, Colors.RED + "Slow Mode is set to " + ((double) Rem.slowMode / 1000.0) + " seconds.");
	}

	@Override
	public String getDescription() {
		return "Gets/sets the Slow Mode. (Minimum interval between usage of commands and/or replaces per user)";
	}

	@Override
	public String getUsage() {
		return "<time (o, gets if not present)>";
	}

}
