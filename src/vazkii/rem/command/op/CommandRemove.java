package vazkii.rem.command.op;

import java.util.List;

import org.jibble.pircbot.PircBot;

import vazkii.rem.Rem;
import vazkii.rem.command.Command;
import vazkii.rem.command.CommandCustom;

public class CommandRemove extends Command {

	public CommandRemove() {
		super("remcom", true);
	}

	@Override
	public void trigger(PircBot bot, String channel, String sender, String login, String hostname, List<String> tokens) {
		if(tokens.size() == 1) {
			String command = tokens.get(0);
			
			for(CommandCustom c : Rem.customCommands)
				if(c.info.command.equals(command)) {
					Rem.customCommands.remove(c);
					Rem.commands.remove(c);
					Rem.config.customCommands.remove(c.info);
					Rem.saveConfig();
					return;
				}
		}
	}

	@Override
	public String getDescription() {
		return "Removes a command.";
	}

	@Override
	public String getUsage() {
		return "<command>";
	}

}
