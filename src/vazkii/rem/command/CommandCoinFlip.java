package vazkii.rem.command;

import java.util.List;
import java.util.Random;

import org.jibble.pircbot.PircBot;

import vazkii.rem.Rem;

public class CommandCoinFlip extends Command {

	Random rand = new Random();

	public CommandCoinFlip() {
		super("coinflip", false);
	}

	@Override
	public void trigger(PircBot bot, String channel, String sender, String login, String hostname, List<String> tokens) {
		long amount = 1;

		if(!tokens.isEmpty())
			try {
				amount = Integer.parseInt(tokens.get(0));
				if(amount < 0)
					amount = -amount;
			} catch(Exception e) { }

		amount = Math.max(1, Math.min(1000, amount));

		int heads = 0, tails = 0;
		for(int i = 0; i < amount; i++) {
			if(Rem.rand.nextBoolean())
				heads++;
			else tails++;
		}

		if(amount == 1)
			bot.sendMessage(channel, sender + " flipped " + (heads == 0 ? "Tails" : "Heads") + ".");
		else bot.sendMessage(channel, sender + " flipped " + heads + " Heads and " + tails + " Tails.");
	} 

	@Override
	public String getUsage() {
		return "<amount of coins>";
	}

	@Override
	public String getDescription() {
		return "Flips coins.";
	}

}
