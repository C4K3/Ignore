package net.simpvp.Ignore;

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

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		Player player = null;
		if (sender instanceof Player) {
			player = (Player) sender;
		}

		if (player == null) {
			Ignore.instance.getLogger().info("Only players can use this command.");
			return true;
		}

		if (args.length > 1) {
			player.sendMessage(ChatColor.RED + "Incorrect amount of arguments.\n"
					+ "Usage: /ignore [player]");
			return true;
		}

		if (args.length == 0) {
			ArrayDeque<String> ignored = Storage.getListIgnoring(player);
			
			if (ignored == null || ignored.size() == 0) {
				player.sendMessage(ChatColor.GOLD + "You are not ignoring anybody.");
				return true;
			}

			Iterator<String> ite = ignored.iterator();
			String msg = ChatColor.GOLD + "You are ignoring: " + ite.next();
			while (ite.hasNext())
				msg += ", " + ite.next();

			player.sendMessage(msg);

		}

		if (args.length == 1) {
			@SuppressWarnings("deprecation") /* Only used to get as command argument, is saved as UUID */
			Player target = Ignore.instance.getServer().getPlayer(args[0]);

			if (target == null) {
				player.sendMessage(ChatColor.RED + "There is no player with that name online.");
				return true;
			}

			if (Storage.getIsIgnoring(player, target)) {
				player.sendMessage(ChatColor.GOLD + "You are no longer ignoring " + target.getName() + ".");
				Storage.removeIgnore(player, target);
			} else {
				player.sendMessage(ChatColor.GOLD + "You are now ignoring " + target.getName() + ". Use /ignore again to unignore.");
				Storage.addIgnore(player, target);
			}

		}

		return true;
	}

}

