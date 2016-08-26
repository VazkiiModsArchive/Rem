package vazkii.rem.command;

import java.util.List;
import java.util.Random;

import org.jibble.pircbot.PircBot;

public class CommandConsult extends Command {

	private static final String[] RESULTS = new String[] {
			"Yes", "No", "Always", "Never", "Perhaps", "Possibly", "Most Likely", "Unlikely", "Doubtfully", "Without a Doubt"
	};

	Random rand = new Random();

	public CommandConsult() {
		super("consult", false);
	}

	@Override
	public void trigger(PircBot bot, String channel, String sender, String login, String hostname, List<String> tokens) {
		String msg = (rand.nextInt(1337) == 0 ? "Mom's Spaghetti" : RESULTS[rand.nextInt(RESULTS.length)]) + ".";

		if(tokens.size() == 3 && tokens.get(0).equalsIgnoreCase("what") && tokens.get(1).equalsIgnoreCase("is") && tokens.get(2).equalsIgnoreCase("love?"))
			msg = "Baby don't hurt me...";

		bot.sendMessage(channel, sender + ": " + msg);
	}

	@Override
	public String getDescription() {
		return "Gets a cosultation.";
	}

	@Override
	public String getUsage() {
		return "<question>";
	}

}
