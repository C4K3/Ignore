package net.simpvp.Ignore;

import org.bukkit.plugin.java.JavaPlugin;

public class Ignore extends JavaPlugin {

	public static Ignore instance;

	public Ignore() {
		instance = this;
	}

	public void onEnable() {
		getServer().getPluginManager().registerEvents(new ChatListener(), this);
		getCommand("ignore").setExecutor(new IgnoreCommand());
		getCommand("me").setExecutor(new MeCommand());
		getCommand("tell").setExecutor(new TellCommand());
		getCommand("r").setExecutor(new PMCommands());
		getCommand("m").setExecutor(new PMCommands());
	}

	public void onDisable() {

	}

}

