package vazkii.rem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jibble.pircbot.Colors;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import vazkii.rem.Config.CommandInfo;
import vazkii.rem.command.Command;
import vazkii.rem.command.CommandCoinFlip;
import vazkii.rem.command.CommandConsult;
import vazkii.rem.command.CommandCustom;
import vazkii.rem.command.CommandDongers;
import vazkii.rem.command.CommandFlip;
import vazkii.rem.command.CommandHelp;
import vazkii.rem.command.CommandLmgtfy;
import vazkii.rem.command.CommandRiot;
import vazkii.rem.command.CommandRoll;
import vazkii.rem.command.op.CommandAdd;
import vazkii.rem.command.op.CommandJoin;
import vazkii.rem.command.op.CommandNick;
import vazkii.rem.command.op.CommandPart;
import vazkii.rem.command.op.CommandQuit;
import vazkii.rem.command.op.CommandRaw;
import vazkii.rem.command.op.CommandRemove;
import vazkii.rem.command.op.CommandSlowMode;

public class Rem extends PircBot {

	public static final Gson gson = (new GsonBuilder().setPrettyPrinting()).create();
	public static final Type CONFIG_TYPE = new TypeToken<Config>(){}.getType();

	private static final Pattern TWITTER_PATTERN = Pattern.compile(".*?https://twitter.com/\\w+/status/(\\d+)");
	private static final Pattern REPLACE_PATTERN = Pattern.compile("^s/([^/]*)/([^/]*)/?$");

	public static Rem instance;
	public static boolean connected = false;
	public static List<Command> commands = new ArrayList<Command>();
	public static List<CommandCustom> customCommands = new ArrayList<CommandCustom>();

	public static long slowMode = 500;
	public static Map<String, Long> lastMessageTime = new HashMap<String, Long>();
	public static Map<String, LinkedList<Message>> messages = new HashMap<String, LinkedList<Message>>(); 

	public static Config config;
	public static File configFile;

	public static Random rand = new Random();
	public static Twitter twitter;

	public Rem() {
		instance = this;
		configFile = new File(new File("."), "config.json");
		if(!configFile.exists()) {
			config = new Config();
			saveConfig();
			throw new IllegalArgumentException("No config file.");
		}

		loadConfig();
		if(config == null)
			throw new IllegalArgumentException("Invalid config file.");

		saveConfig();

		setVerbose(config.verboseLogging);
		setName(config.ircInfo.name);
		setLogin(config.ircInfo.login);
		setVersion(config.ircInfo.display);
		setMessageDelay(config.ircInfo.delay);

		try {
			setEncoding(config.ircInfo.encoding);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}

		initCommands();

		if(config.features.twitter) {
			ConfigurationBuilder twitterConfig = new ConfigurationBuilder();
			twitterConfig.setDebugEnabled(true)
			.setOAuthConsumerKey(config.twitterOAuth.consumerKey)
			.setOAuthConsumerSecret(config.twitterOAuth.consumerKeySecret)
			.setOAuthAccessToken(config.twitterOAuth.accessToken)
			.setOAuthAccessTokenSecret(config.twitterOAuth.accessTokenSecret);

			TwitterFactory factory = new TwitterFactory(twitterConfig.build());
			twitter = factory.getInstance();
		}
	}

	public void connect() throws Exception {
		connect(config.ircInfo.server);
		identify(config.ircInfo.password);
		joinChannel(config.ircInfo.homeChannel);

		if(config.features.otherChannels)
			for(String c : config.otherChannels)
				joinChannel(c);

		connected = true;
	}

	public void initCommands() {
		commands.clear();

		// Basic Commands
		addCommand(new CommandHelp());
		addCommand(new CommandRemove());
		addCommand(new CommandAdd());
		addCommand(new CommandJoin());
		addCommand(new CommandNick());
		addCommand(new CommandPart());
		addCommand(new CommandQuit());
		addCommand(new CommandRaw());
		addCommand(config.features.slowMode, () -> new CommandSlowMode());

		// Built In Commands
		addCommand(config.features.builtInCommands.coinflip, () -> new CommandCoinFlip());
		addCommand(config.features.builtInCommands.consult, () -> new CommandConsult());
		addCommand(config.features.builtInCommands.lmgtfy, () -> new CommandLmgtfy());
		addCommand(config.features.builtInCommands.roll, () -> new CommandRoll());
		addCommand(config.features.builtInCommands.dongers, () -> new CommandDongers());
		addCommand(config.features.builtInCommands.riot, () -> new CommandRiot());
		addCommand(config.features.builtInCommands.flip, () -> new CommandFlip());

		// Custom Commands
		for(CommandInfo info : config.customCommands)
			addCommand(info);
	}

	public void addCommand(boolean flag, Supplier<Command> supplier) {
		if(flag)
			addCommand(supplier.get());
	}

	public void addCommand(CommandInfo info) {
		addCommand(new CommandCustom(info));
	}

	public void addCommand(Command command) {
		for(Command c : commands)
			if(c.name.equals(command.name))
				return;

		commands.add(command);
		if(command instanceof CommandCustom)
			customCommands.add((CommandCustom) command);
	}

	@Override
	protected void onMessage(String channel, String sender, String login, String hostname, String message) {
		if(!lastMessageTime.containsKey(sender))
			lastMessageTime.put(sender, (long) 0);

		boolean op = false;
		for(User user : getUsers(config.ircInfo.homeChannel))
			if(user.getNick().equals(sender) && user.isOp())
				op = true;

		Matcher matcher;

		// Twitter
		if(config.features.twitter) {
			matcher = TWITTER_PATTERN.matcher(message);
			if(matcher.find()) {
				try {
					long l = Long.parseLong(matcher.group(1));
					Status status = twitter.showStatus(l);	
					sendMessage(channel, "<" + sender + "> @" + status.getUser().getScreenName() + ": " + status.getText().replace("\n", " "));
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}

		// Slow Mode Check
		long ms = System.currentTimeMillis(); 
		if(!config.features.slowMode || ms - lastMessageTime.get(sender) > slowMode || op) {

			// Find And Replace
			if(config.features.findAndReplace) {
				matcher = REPLACE_PATTERN.matcher(message.trim());
				if(matcher.find()) {
					String orig = matcher.group(1);
					String repl = matcher.group(2);

					LinkedList<Message> list = messages.get(channel);
					if(list != null && !list.isEmpty()) {
						ListIterator<Message> iterator = list.listIterator(list.size());
						Message m = null;
						boolean foundMatch = false;

						do {
							m = iterator.previous();
						} while(m != null && !(foundMatch = m.matches(orig)) && iterator.hasPrevious());

						if(foundMatch & m != null)
							try {
								sendMessage(channel, "<" + m.sender + "> " + m.text.replaceAll(orig, repl));
							} catch(Exception e) { sendMessage(channel, sender + ": Invalid Regex :<"); }
					}
					lastMessageTime.put(sender, ms);
				} else new Message(message, sender, channel).add();
			}

			// Table Unflip
			if(config.features.tableUnflip && message.contains("\u253b\u2501\u253b"))
				sendMessage(channel, sender + ": \u252C\u2500\u252C\u30CE( \u00BA _ \u00BA\u30CE)");

			for(Command command : commands)
				if(command.shouldTrigger(channel, message, op)) {
					command.trigger(this, channel, sender, login, hostname, message);
					lastMessageTime.put(sender, ms);
					break;
				}
		} else sendNotice(sender, Colors.RED + "Slow Mode is set to " + ((double) slowMode / 1000.0) + " seconds. Please wait that time between using commands or replaces.");
	}
	
	@Override
	protected void onPrivateMessage(String sender, String login, String hostname, String message) {
		boolean op = false;
		for(User user : getUsers(config.ircInfo.homeChannel))
			if(user.getNick().equals(sender) && user.isOp())
				op = true;
		
		for(Command command : commands)
			if(command.shouldTrigger(sender, message, op) && (!command.requiresOp || op))
				sendMessage(sender, Colors.CYAN + Colors.BOLD + command.name + Colors.NORMAL + Colors.RED + (command.requiresOp ? " (op)" : "") + Colors.NORMAL + ": " + command.getDisplayDescription());
	}

	public static void loadConfig() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(configFile), "UTF-8"));
			config = gson.<Config>fromJson(reader, CONFIG_TYPE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void saveConfig() {
		try {
			String json = gson.toJson(config, CONFIG_TYPE);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(configFile), "UTF-8"));
			writer.write(json);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static class Message {

		public final String text, sender, channel;

		public Message(String text, String sender, String channel) {
			this.text = text;
			this.sender = sender;
			this.channel = channel;
		}

		public void add() {
			if(!messages.containsKey(channel))
				messages.put(channel, new LinkedList<Message>());

			LinkedList<Message> channelMsgs = messages.get(channel);
			channelMsgs.add(this);
			if(channelMsgs.size() > 25)
				channelMsgs.removeFirst();
		}

		public boolean matches(String regex) {
			return text.matches(".*" + regex + ".*");
		}

	}

}
