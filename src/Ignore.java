package net.simpvp.Ignore;

import org.bukkit.plugin.java.JavaPlugin;

public class Ignore extends JavaPlugin {

	public static Ignore instance;

	public Ignore() {
		instance = this;
	}

	public void onEnable() {
		getServer().getPluginManager().registerEvents(new ChatListener(), this);
		getServer().getPluginManager().registerEvents(new TellListener(), this);
		getCommand("ignore").setExecutor(new IgnoreCommand());
		getCommand("me").setExecutor(new MeCommand());
	}

	public void onDisable() {

	}

}

