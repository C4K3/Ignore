package net.simpvp.Ignore;

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

		/* String of the player, in case it's null and it should server */
		String splayer;
		if (player == null) {
			splayer = "Server";
		} else {
			splayer = player.getName();
		}

		@SuppressWarnings("deprecation") /* Only used to get as command argument */
		Player target = Ignore.instance.getServer().getPlayerExact(args[0]);
		String starget;

		if (target == null) {
			if (args[0].equalsIgnoreCase("server")) {
				starget = "Server";
			} else {
				send_msg(player, "[" + splayer + ": That player cannot be found]", ChatColor.GRAY);
				return true;
			}
		} else {
			starget = target.getName();
		}

		String msg = args[1];
		for (int i = 2; i < args.length; i++)
			msg += " " + args[i];

		send_msg(player, "-->" + starget + ": " + msg, ChatColor.GRAY);

		/* Only actually send the message to the recipient if they're not ignoring the sender */
		if (player == null || !Storage.getIsIgnoring(target, player))
			send_msg(target, "<--" + splayer + ": " + msg, ChatColor.GRAY);

		return true;
	}

	/**
	 * Common message for sending a message to somebody
	 */
	private void send_msg(Player player, String message, ChatColor chatcolor) {
		if (player == null) {
			Ignore.instance.getLogger().info(message);
		} else {
			player.sendMessage(chatcolor + message);
		}
	}

}

