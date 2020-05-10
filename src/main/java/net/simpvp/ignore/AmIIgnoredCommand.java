package net.simpvp.ignore;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AmIIgnoredCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player player = null;
		if (sender instanceof Player) {
			player = (Player) sender;
		}

		if (player == null) {
			Ignore.instance.getLogger().info(
					"Only players can use this command.");
			return true;
		}

		if (args.length != 1) {
			send_message(player, ChatColor.RED,
					"Incorrect number of arguments.\n"
							+ "Usage: /amiignored <player>");
		} else {
			Player target = Ignore.instance.getServer()
					.getPlayer(args[0]);

			if (target == null) {
				send_message(player, ChatColor.RED,
						"There is no player with that name online.");
				return true;
			}

			if (Storage.getIsIgnoring(target, player)) {
				send_message(player, ChatColor.WHITE, "You are being ignored by that player.");
			} else {
				send_message(player, ChatColor.WHITE, "You are not being ignored by that player.");
			}

			return true;
		}

		return false;
	}

	private void send_message(Player player, ChatColor color, String msg) {
		player.sendMessage(color + msg);
		Ignore.instance.getLogger().info(msg);
	}

}
