package vazkii.rem.command;

import java.util.List;

import org.jibble.pircbot.Colors;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;

import vazkii.rem.Rem;

public class CommandHelp extends Command {

	public CommandHelp() {
		super(Rem.config.ircInfo.helpCommand, false);
	}

	@Override
	public void trigger(PircBot bot, String channel, String sender, String login, String hostname, List<String> tokens) {
		boolean op = false;
		for(User user : bot.getUsers(Rem.config.ircInfo.homeChannel))
			if(user.getNick().equals(sender) && user.isOp())
				op = true;
		
		bot.sendMessage(sender, Colors.MAGENTA + Colors.BOLD + Colors.UNDERLINE + Rem.config.ircInfo.helpMessage);

		StringBuilder b = new StringBuilder();
		b.append(Colors.BOLD);
		for(Command command : Rem.commands)
			if(command != this && (!command.requiresOp || op)) {
				b.append(command.name);
				if(command.requiresOp && op) {
					b.append(Colors.RED);
					b.append(" (op)");
					b.append(Colors.NORMAL);
					b.append(Colors.BOLD);
				}

				b.append(", ");
			}
		bot.sendMessage(sender, b.toString());
		bot.sendMessage(sender, Colors.CYAN + Colors.BOLD + Colors.UNDERLINE + "PM me with any command in this list and it'll tell you what it does!");
	}

}
