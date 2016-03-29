package net.simpvp.Ignore;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Takes over the /tell command.
 * Respects people ignoring other players, and also lets players /tell server
 */
public class TellCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = null;
		if (sender instanceof Player)
			player = (Player) sender;

		if (args.length < 2) {
			send_msg(player, "Usage: /tell <player> <private message ...>", ChatColor.RED);
			return true;
		}

		@SuppressWarnings("deprecation") /* Only used to get as command argument */
		Player target = Ignore.instance.getServer().getPlayerExact(args[0]);

		String msg = args[1];
		for (int i = 2; i < args.length; i++)
			msg += " " + args[i];

		sendPM(player, target, msg, args[0].equalsIgnoreCase("server"));

		return true;
	}

	/**
	 * Have player send msg to target
	 */
	public static void sendPM(Player player, Player target, String msg, boolean is_target_server) {
		/* String of the player, in case it's null and it should be server */
		String splayer;
		UUID uplayer = null;
		if (player == null) {
			splayer = "Server";
		} else {
			splayer = player.getName();
			uplayer = player.getUniqueId();
		}

		if (target == null && !is_target_server) {
			/* Yes this is confusing, but the message is imitating default (Notchian) behavior */
			send_msg(player, "[" + splayer + ": That player cannot be found]", ChatColor.GRAY);
			return;
		}

		String starget;
		UUID utarget = null;
		if (target == null) {
			starget = "Server";
		} else {
			starget = target.getName();
			utarget = target.getUniqueId();
		}

		/* Don't let people figure out if a vanished admin is online or not */
		if (player == null || target == null || player.canSee(target)) {
			send_msg(player, "-->" + starget + ": " + msg, ChatColor.GRAY);
			PMCommands.mPlayerList.put(uplayer, utarget);
		} else {
			send_msg(player, "[" + splayer + ": That player cannot be found]", ChatColor.GRAY);
		}

		/* Only actually send the message to the recipient if they're not ignoring the sender */
		if (player == null || !Storage.getIsIgnoring(target, player)) {
			send_msg(target, "<--" + splayer + ": " + msg, ChatColor.GRAY);
			PMCommands.rPlayerList.put(utarget, uplayer);
		}

		return;
	}

	/**
	 * Common message for sending a message to somebody
	 */
	public static void send_msg(Player player, String message, ChatColor chatcolor) {
		if (player == null) {
			Ignore.instance.getLogger().info(message);
		} else {
			player.sendMessage(chatcolor + message);
		}
	}

}

