package net.simpvp.Ignore;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Handles the /me command as close to vanilla as possible
 */
public class MeCommand implements CommandExecutor {

	public boolean onCommand(
			CommandSender sender,
			Command cmd,
			String label,
			String[] args) {

		Player player = null;
		if (sender instanceof Player)
			player = (Player) sender;

		if (args.length == 0) {
			String msg = "Usage: /me <action ...>";
			if (player == null) {
				Bukkit.getLogger().info(msg);
			} else {
				player.sendMessage(ChatColor.RED + msg);
			}

			return true;
		}

		String msg;
		if (player == null) {
			msg = "* Server";
		} else {
			msg = "* " + player.getName();
		}

		for (int i = 0; i < args.length; i++)
			msg += " " + args[i];

		Bukkit.getLogger().info(msg);

		for (Player p : Ignore.instance.getServer()
				.getOnlinePlayers()) {
			if (player != null && !Storage.getIsIgnoring(p, player))
				p.sendMessage(msg);
		}

		return true;
	}

}

