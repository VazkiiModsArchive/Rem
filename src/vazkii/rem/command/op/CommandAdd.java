package vazkii.rem.command.op;

import java.util.List;

import org.jibble.pircbot.PircBot;

import vazkii.rem.Config.CommandInfo;
import vazkii.rem.Rem;
import vazkii.rem.command.Command;
import vazkii.rem.command.CommandCustom;

public class CommandAdd extends Command {

	public CommandAdd() {
		super("addcom", true);
	}

	@Override
	public void trigger(PircBot bot, String channel, String sender, String login, String hostname, List<String> tokens) {
		if(tokens.size() >= 3) {	
			String command = tokens.get(0);
			boolean op = command.equals("op");
			if(op) {
				tokens.remove(0);
				command = tokens.get(0);

				if(tokens.size() < 3)
					return;
			}

			command = command.replaceAll(" ", "");

			boolean newCommand = true;
			CommandInfo info = null;

			for(CommandCustom c : Rem.customCommands)
				if(c.info.command.equals(command)) {
					newCommand = false;
					info = c.info;
					break;
				}

			if(info == null)
				info = new CommandInfo();

			info.command = command;
			info.description = tokens.get(1);
			info.replies = tokens.subList(2, tokens.size());
			info.opOnly = op;

			if(newCommand) {
				Rem.instance.addCommand(info);
				Rem.config.customCommands.add(info);
			}
			Rem.saveConfig();
		}
	}

	@Override
	public String getDescription() {
		return "Adds a command.";
	}

	@Override
	public String getUsage() {
		return "(op) <command> <description> [<replies>]";
	}

}