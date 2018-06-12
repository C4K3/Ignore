package net.simpvp.Ignore;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Controls the /servertell command
 *
 * Which allows admins to /tell people as Server
 */
public class ServertellCommand implements CommandExecutor {

	public boolean onCommand(
			CommandSender sender,
			Command cmd,
			String label,
			String[] args) {

		Player player = null;
		if (sender instanceof Player) {
			player = (Player) sender;
		}

		if (player != null && player.isOp() == false) {
			TellCommand.send_msg(player,
					"You do not have permission to use this command.",
					ChatColor.RED);
			return true;
		}

		if (args.length < 2) {
			TellCommand.send_msg(player,
					"Usage: /tell <player> <private message ...>",
					ChatColor.RED);
			return true;
		}

		/* Only used as command argument */
		@SuppressWarnings("deprecation")
		Player target = Ignore.instance.getServer()
				.getPlayerExact(args[0]);

		if (target == null) {
			TellCommand.send_msg(player,
					"No such player online.",
					ChatColor.RED);
			return true;
		}

		String msg = args[1];
		for (int i = 2; i < args.length; i++) {
			msg += " " + args[i];
		}

		UUID sender_uuid = null;
		if (player != null) {
			sender_uuid = player.getUniqueId();
		}
		UUID recipient_uuid = null;
		if (target != null) {
			recipient_uuid = target.getUniqueId();
		}

		TellCommand.send_msg(target, "<--Server: " + msg,
				ChatColor.GRAY);
		/* null for player = Server */
		PMCommands.rPlayerList.put(recipient_uuid, null);

		for (Player p : Ignore.instance.getServer().getOnlinePlayers()) {
			if (p.isOp() == false) {
				continue;
			}

			TellCommand.send_msg(p,
					"Server-->" + target.getName() + ": " + msg,
					ChatColor.DARK_GRAY);
		}

		return true;
	}

}

