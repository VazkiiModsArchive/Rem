package vazkii.rem.command;

import java.util.HashMap;
import java.util.List;

import org.jibble.pircbot.PircBot;

public class CommandFlip extends Command {

	// Table from http://www.upsidedowntext.com/
	private static final HashMap<Character, String> CHAR_FLIPS = new HashMap<Character, String>();
	static {
		CHAR_FLIPS.put('a', "\u0250");
		CHAR_FLIPS.put('b', "q");
		CHAR_FLIPS.put('c', "\u0254");
		CHAR_FLIPS.put('d', "p");
		CHAR_FLIPS.put('e', "\u01DD");
		CHAR_FLIPS.put('f', "\u025F");
		CHAR_FLIPS.put('g', "\u0183");
		CHAR_FLIPS.put('h', "\u0265");
		CHAR_FLIPS.put('i', "\u1D09");
		CHAR_FLIPS.put('j', "\u027E");
		CHAR_FLIPS.put('k', "\u029E");
		CHAR_FLIPS.put('m', "\u026F");
		CHAR_FLIPS.put('n', "u");
		CHAR_FLIPS.put('r', "\u0279");
		CHAR_FLIPS.put('t', "\u0287");
		CHAR_FLIPS.put('v', "\u028C");
		CHAR_FLIPS.put('w', "\u028D");
		CHAR_FLIPS.put('y', "\u028E");
		CHAR_FLIPS.put('A', "\u2200");
		CHAR_FLIPS.put('C', "\u0186");
		CHAR_FLIPS.put('E', "\u018E");
		CHAR_FLIPS.put('F', "\u2132");
		CHAR_FLIPS.put('G', "\u05E4");
		CHAR_FLIPS.put('H', "H");
		CHAR_FLIPS.put('I', "I");
		CHAR_FLIPS.put('J', "\u017F");
		CHAR_FLIPS.put('L', "\u02E5");
		CHAR_FLIPS.put('M', "W");
		CHAR_FLIPS.put('N', "N");
		CHAR_FLIPS.put('P', "\u0500");
		CHAR_FLIPS.put('T', "\u2534");
		CHAR_FLIPS.put('U', "\u2229");
		CHAR_FLIPS.put('V', "\u039B");
		CHAR_FLIPS.put('Y', "\u2144");
		CHAR_FLIPS.put('1', "\u0196");
		CHAR_FLIPS.put('2', "\u1105");
		CHAR_FLIPS.put('3', "\u0190");
		CHAR_FLIPS.put('4', "\u3123");
		CHAR_FLIPS.put('5', "\u03DB");
		CHAR_FLIPS.put('6', "9");
		CHAR_FLIPS.put('7', "\u3125");
		CHAR_FLIPS.put('8', "8");
		CHAR_FLIPS.put('9', "6");
		CHAR_FLIPS.put('0', "0");
		CHAR_FLIPS.put('.', "\u02D9");
		CHAR_FLIPS.put(',', "'");
		CHAR_FLIPS.put('\'', ",");
		CHAR_FLIPS.put('"', ",,");
		CHAR_FLIPS.put('`', ",");
		CHAR_FLIPS.put('?', "\u00BF");
		CHAR_FLIPS.put('!', "\u00A1");
		CHAR_FLIPS.put('[', "]");
		CHAR_FLIPS.put(']', "[");
		CHAR_FLIPS.put('(', ")");
		CHAR_FLIPS.put(')', "(");
		CHAR_FLIPS.put('{', "}");
		CHAR_FLIPS.put('}', "{");
		CHAR_FLIPS.put('<', ">");
		CHAR_FLIPS.put('>', "<");
		CHAR_FLIPS.put('&', "\u214B");
		CHAR_FLIPS.put('_', "\u203E");
		CHAR_FLIPS.put('\u2234', "\u2235");
		CHAR_FLIPS.put('\u2045', "\u2046");
	}

	public CommandFlip() {
		super("flip", false);
	}

	@Override
	public void trigger(PircBot bot, String channel, String sender, String login, String hostname, List<String> tokens) {
		String guy = "(\u256f\u00b0\u25a1\u00b0)\u256f\ufe35 ";

		String flipmsg = "";
		for(String s : tokens)
			flipmsg = flipmsg + s + " ";

		String riot = guy + (flipmsg.isEmpty() ? "\u253b\u2501\u253b" : flip(flipmsg));
		bot.sendMessage(channel, riot);
	}

	public String flip(String s) {
		StringBuilder b = new StringBuilder();
		char[] chars = s.toCharArray();
		for(int i = chars.length - 1; i >= 0; i--) {
			char c = chars[i];

			if(CHAR_FLIPS.containsKey(c))
				b.append(CHAR_FLIPS.get(c));
			else b.append(c);
		}

		return b.toString();
	}

	@Override
	public String getDescription() {
		return "FLIP YOUR <thing>";
	}

	@Override
	public String getUsage() {
		return "<thingtoflip (o)>";
	}

}
