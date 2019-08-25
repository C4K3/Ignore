package net.simpvp.ignore;

import java.util.UUID;
import java.util.ArrayDeque;
import java.util.Iterator;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Lets players toggle whether to ignore target player.
 * If used with no arguments, prints who you are ignoring.
 */
public class IgnoreCommand implements CommandExecutor {

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

		if (args.length > 1) {
			send_message(player, ChatColor.RED,
					"Incorrect amount of arguments.\n"
					+ "Usage: /ignore [player]");

		} else if (args.length == 0) {
			/* Print who the sender is ignoring */
			ArrayDeque<String> ignored
				= Storage.getListIgnoring(player);
			
			if (ignored == null || ignored.size() == 0) {
				send_message(player, ChatColor.GOLD,
						"You are not ignoring anybody.");
				return true;
			}

			Iterator<String> ite = ignored.iterator();
			String msg = "You are ignoring: "
				+ ite.next();
			while (ite.hasNext()) {
				msg += ", " + ite.next();
			}

			send_message(player, ChatColor.GOLD, msg);

		} else if (args.length == 1) {
			/* Is not stored, only used as command argument */
			@SuppressWarnings("deprecation")
			Player target = Ignore.instance.getServer()
					.getPlayer(args[0]);
			/* WARNING: It is important to only begin ignoring
			 * players who are currently online. Changing this would
			 * require changing the logic in PlayerJoin */

			if (target == null) {
				send_message(player, ChatColor.RED,
						"There is no player with that name online.");
				return true;
			}

			if (Storage.getIsIgnoring(player, target)) {
				send_message(player, ChatColor.GOLD,
						"You are no longer ignoring "
						+ target.getName() + ".");
				Storage.removeIgnore(player, target);
				SQLite.remove_ignore(player.getUniqueId(),
						target.getUniqueId());
			} else {
				send_message(player, ChatColor.GOLD,
						"You are now ignoring "
						+ target.getName()
						+ ". Use /ignore again to unignore.");
				Storage.addIgnore(player, target);
				SQLite.add_ignore(player.getUniqueId(),
						target.getUniqueId(),
						target.getName());
			}

		}

		return true;
	}

	private void send_message(Player player, ChatColor color, String msg) {
		player.sendMessage(color + msg);
		Ignore.instance.getLogger().info(msg);
	}

}

