package net.simpvp.ignore;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.ArrayList;

public class ReportCommand implements CommandExecutor {

	public boolean onCommand(
			CommandSender sender,
			Command cmd,
			String label,
			String[] args) {

		Player player = null;
		if (sender instanceof Player) {
			player = (Player) sender;
		} else {
			sender.sendMessage("Only players can use this command!");
			return true;
		}

		if (args.length == 0) {
			player.sendMessage(ChatColor.RED + "Usage: /report <message...>");
			return true;
		}

		ArrayList<String> msg = new ArrayList<>();
		msg.add("(Report)");
		msg.addAll(Arrays.asList(args));

		Chat.server_receive_pm(player, msg);
		player.sendMessage(ChatColor.GREEN + "Thank you for your report. An admin will review it when possible.");

		return true;
	}
}
