package net.simpvp.ignore;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

public class Ignore extends JavaPlugin {

	public static Ignore instance;

	public Ignore() {
		instance = this;
	}

	public void onEnable() {
		/* Check if this plugin's directory exists, if not create it */
		File dir = new File("plugins/Ignore");
		if (!dir.exists()) {
			dir.mkdir();
		}
		SQLite.connect();

		getServer().getPluginManager().registerEvents(new ChatListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
		getCommand("ignore").setExecutor(new IgnoreCommand());
		getCommand("me").setExecutor(new MeCommand());
		getCommand("tell").setExecutor(new TellCommand());
		getCommand("servertell").setExecutor(new ServertellCommand());
		getCommand("w").setExecutor(new TellCommand());
		getCommand("msg").setExecutor(new TellCommand());
		getCommand("r").setExecutor(new PMCommands());
		getCommand("m").setExecutor(new PMCommands());
		getCommand("lastlog").setExecutor(new LastLog());
	}

	public void onDisable() {

	}

}

