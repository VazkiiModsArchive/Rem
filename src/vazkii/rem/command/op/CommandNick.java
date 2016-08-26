package vazkii.rem.command.op;

import java.util.List;

import org.jibble.pircbot.PircBot;

import vazkii.rem.command.Command;

public class CommandNick extends Command {

	public CommandNick() {
		super("nick", true);
	}

	@Override
	public void trigger(PircBot bot, String channel, String sender, String login, String hostname, List<String> tokens) {
		if(tokens.size() > 0)
			bot.changeNick(tokens.get(0));
	}

	@Override
	public String getDescription() {
		return "Sets the nickname.";
	}

	@Override
	public String getUsage() {
		return "<nick>";
	}

}
