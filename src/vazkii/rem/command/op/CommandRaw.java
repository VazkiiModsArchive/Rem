package vazkii.rem.command.op;

import java.util.List;

import org.jibble.pircbot.PircBot;

import vazkii.rem.command.Command;

public class CommandRaw extends Command {

	public CommandRaw() {
		super("raw", true);
	}

	@Override
	public void trigger(PircBot bot, String channel, String sender, String login, String hostname, List<String> tokens) {
		String message = "";
		for(String s : tokens)
			message = message + s + " ";

		bot.sendRawLine(message);
	}

	@Override
	public String getDescription() {
		return "Sends a raw message to the IRC server.";
	}

	@Override
	public String getUsage() {
		return "<message>";
	}

}
