package vazkii.rem.command.op;

import java.util.List;

import org.jibble.pircbot.PircBot;

import vazkii.rem.Rem;
import vazkii.rem.command.Command;

public class CommandPart extends Command {

	public CommandPart() {
		super("part", true);
	}

	@Override
	public void trigger(PircBot bot, String channel, String sender, String login, String hostname, List<String> tokens) {
		if(tokens.size() > 0) {
			String c = tokens.get(0);
			c = c.startsWith("#") ? c : "#" + c;
			if(!c.equals(Rem.config.ircInfo.homeChannel))
				bot.partChannel(c, Rem.config.ircInfo.partMessage);

			if(Rem.config.features.otherChannels && tokens.size() > 1 && tokens.get(1).equals("noautojoin") && Rem.config.otherChannels.contains(c)) {
				Rem.config.otherChannels.remove(c);
				Rem.saveConfig();
			}
		}
	}

	@Override
	public String getDescription() {
		return "Parts from a channel. Add noautojoin to the end to remove it from the autojoin list.";
	}

	@Override
	public String getUsage() {
		return "<channel> <message (o)>";
	}
}
