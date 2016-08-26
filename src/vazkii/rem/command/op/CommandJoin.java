package vazkii.rem.command.op;

import java.util.List;

import org.jibble.pircbot.PircBot;

import vazkii.rem.Rem;
import vazkii.rem.command.Command;

public class CommandJoin extends Command {

	public CommandJoin() {
		super("join", true);
	}

	@Override
	public void trigger(PircBot bot, String channel, String sender, String login, String hostname, List<String> tokens) {
		if(tokens.size() > 0) {
			String c = tokens.get(0);
			c = c.startsWith("#") ? c : "#" + c;
			bot.joinChannel(c);

			if(Rem.config.features.otherChannels && tokens.size() > 1 && tokens.get(1).equals("autojoin") && !Rem.config.otherChannels.contains(c)) {
				Rem.config.otherChannels.add(c);
				Rem.saveConfig();
			}
		}
	}

	@Override
	public String getDescription() {
		return "Joins a Channel. Add autojoin to the end for autojoin.";
	}

	@Override
	public String getUsage() {
		return "<channel>";
	}
}
