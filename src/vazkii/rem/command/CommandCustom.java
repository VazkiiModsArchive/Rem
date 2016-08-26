package vazkii.rem.command;

import java.util.ArrayList;
import java.util.List;

import org.jibble.pircbot.PircBot;

import vazkii.rem.Config.CommandInfo;
import vazkii.rem.Rem;

public class CommandCustom extends Command {

	public CommandInfo info;

	public CommandCustom(CommandInfo info) {
		super(info.command, info.opOnly);
		this.info = info;
	}

	@Override
	public void trigger(PircBot bot, String channel, String sender, String login, String hostname, List<String> tokens) {
		String reply = info.replies == null || info.replies.isEmpty() ? "N/A" : info.replies.get(Rem.rand.nextInt(info.replies.size()));

		int maxlen = 400;

		List<String> messages = new ArrayList<String>();
		while(reply.length() > maxlen) {
			messages.add(reply.substring(0, maxlen));
			reply = reply.substring(maxlen);
		}
		messages.add(reply);

		for(int i = 0; i < messages.size(); i++) {
			String s = messages.get(i);
			if(i == 0)
				bot.sendMessage(channel, tokens.size() == 0 ? s : tokens.get(0) + ", " + s);
			else bot.sendMessage(channel, s);
		}
	}

	@Override
	public String getDescription() {
		return info.description;
	}

}
