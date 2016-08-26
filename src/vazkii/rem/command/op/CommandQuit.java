package vazkii.rem.command.op;

import java.util.List;

import org.jibble.pircbot.PircBot;

import vazkii.rem.Rem;
import vazkii.rem.command.Command;

public class CommandQuit extends Command {

	public CommandQuit() {
		super("quit", true);
	}

	@Override
	public void trigger(PircBot bot, String channel, String sender, String login, String hostname, List<String> tokens) {
		bot.quitServer(Rem.config.ircInfo.quitMessage);
		System.exit(0);
	}

	@Override
	public String getDescription() {
		return "Stops the bot.";
	}

	@Override
	public String getUsage() {
		return "<message (o)>";
	}

}
