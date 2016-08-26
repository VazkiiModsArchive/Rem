package vazkii.rem.command;

import java.util.List;
import java.util.Random;

import org.jibble.pircbot.PircBot;

public class CommandRoll extends Command {

	Random rand = new Random();

	public CommandRoll() {
		super("roll", false);
	}

	@Override
	public void trigger(PircBot bot, String channel, String sender, String login, String hostname, List<String> tokens) {
		long size = 100;
		boolean neg = false;

		if(!tokens.isEmpty())
			try {
				size = Long.parseLong(tokens.get(0));
				if(size < 0) {
					size = -size;
					neg = true;
				}
			} catch(Exception e) { }

		int amount = 1;
		if(tokens.size() > 1)
			try {
				amount = Integer.parseInt(tokens.get(1));
				if(amount < 0)
					amount = -amount;
			} catch(Exception e) { }

		amount = Math.min(20, amount);

		StringBuilder rolls = new StringBuilder();
		for(int i = 0; i < amount; i++) {
			int roll = size == 0 ? 0 : ((neg ? -1 : 1) * rand.nextInt((int) size) + 1);

			if(i == 0)
				rolls.append(roll);
			else {
				rolls.append(", ");
				rolls.append(roll);
			}
		}

		bot.sendMessage(channel, sender + " rolled " + rolls + ".");
	}

	@Override
	public String getUsage() {
		return "<max (o, defaults to 1000)> <amount of rolls (o)>";
	}

	@Override
	public String getDescription() {
		return "Rolls a number.";
	}

}
