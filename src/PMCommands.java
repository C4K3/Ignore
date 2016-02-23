package net.simpvp.Ignore;

import java.util.UUID;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Implements the /r and /m commands
 */
public class PMCommands implements CommandExecutor {

	/* Key is command sender, value is person they're sending to */
	public static HashMap<UUID, UUID> mPlayerList = new HashMap<UUID, UUID>();
	public static HashMap<UUID, UUID> rPlayerList = new HashMap<UUID, UUID>();

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = null;
		UUID uplayer = null;
		if (sender instanceof Player) {
			player = (Player) sender;
			uplayer = player.getUniqueId();
		}

		if (args.length < 1) {
			TellCommand.send_msg(player, "You cannot send an empty message.", ChatColor.RED);
			return true;
		}

		String msg = args[0];
		for (int i = 1; i < args.length; i++)
			msg += " " + args[i];

		UUID utarget;
		if (cmd.getName().equalsIgnoreCase("m")) {
			if (!mPlayerList.containsKey(uplayer)) {
				TellCommand.send_msg(player, "You have not sent any PMs yet.", ChatColor.RED);
				return true;
			}
			utarget = mPlayerList.get(uplayer);
		} else {
			if (!rPlayerList.containsKey(uplayer)) {
				TellCommand.send_msg(player, "You have not received any PMs yet.", ChatColor.RED);
				return true;
			}
			utarget = rPlayerList.get(uplayer);
		}

		Player target = null;
		if (utarget != null)
			target = Ignore.instance.getServer().getPlayer(utarget);

		TellCommand.sendPM(player, target, msg, utarget == null);

		return true;
	}

}

