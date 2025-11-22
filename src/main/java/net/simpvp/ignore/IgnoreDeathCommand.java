package net.simpvp.ignore;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Toggles whether death messages from ignored players are hidden.
 */
public class IgnoreDeathCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender,
			Command cmd,
			String label,
			String[] args) {

		Player player = null;
		if (sender instanceof Player) {
			player = (Player) sender;
		}

		if (player == null) {
			Ignore.instance.getLogger().info(
					"Only players can use this command.");
			return true;
		}

		if (args.length != 0) {
			send_message(player, ChatColor.RED,
					"Incorrect number of arguments.\n"
					+ "Usage: /ignoredeath");
			return true;
		}

		boolean currentValue = Storage.getIgnoreDeath(player);
		boolean newValue = !currentValue;

		Storage.setIgnoreDeath(player, newValue);
		SQLite.set_ignore_death(player.getUniqueId(), newValue);

		if (newValue) {
			send_message(player, ChatColor.GOLD,
					"Death messages from ignored players will be hidden.");
		} else {
			send_message(player, ChatColor.GOLD,
					"Death messages from ignored players will be shown.");
		}
		return true;
	}

	private void send_message(Player player, ChatColor color, String msg) {
		player.sendMessage(color + msg);
		// Ignore.instance.getLogger().info(msg);
	}
}
