package net.simpvp.ignore;

import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

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
		return server_tell_command(sender, cmd, label, args);
	}

	public static boolean server_tell_command(
			CommandSender sender,
			Command cmd,
			String label,
			String[] args) {

		Player player = null;
		if (sender instanceof Player) {
			player = (Player) sender;
		}

		if (player != null && player.isOp() == false) {
			TextComponent m = new TextComponent("You do not have permission to use this command.");
			m.setColor(ChatColor.RED);
			Chat.send_chat(player, m);
			return true;
		}

		if (args.length < 2) {
			TextComponent m = new TextComponent("Usage: /tell <player> <private message ...>");
			m.setColor(ChatColor.RED);
			Chat.send_chat(player, m);
			return true;
		}

		/* Only used as command argument */
		@SuppressWarnings("deprecation")
		Player target = Ignore.instance.getServer()
				.getPlayerExact(args[0]);

		if (target == null) {
			TextComponent m = new TextComponent("No such player online.");
			m.setColor(ChatColor.RED);
			Chat.send_chat(player, m);
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

		/* null for player = Server */
		Chat.receive_pm(target, null, msg);
		PMCommands.rPlayerList.put(recipient_uuid, null);

		Chat.server_send_pm(target, msg);

		return true;
	}

}

