package net.simpvp.ignore;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.UUID;

public class LastLog implements CommandExecutor {

	private static HashMap<UUID, Deque<String>> messages = new HashMap<>();

	public boolean onCommand(
			CommandSender sender,
			Command cmd,
			String label,
			String[] args) {

		Player player;
		if (sender instanceof Player) {
			player = (Player) sender;
		} else {
			Ignore.instance.getLogger().info("Only players can use this command.");
			return true;
		}

		Deque<String> d = messages.get(player.getUniqueId());
		String m = "";
		if (d != null) {
			for (String s : d) {
				m += s + "\n";
			}
		}
		player.sendMessage(String.format("%sLastlog:\n%s%s%sEnd of Lastlog%s",
					ChatColor.GREEN,
					ChatColor.RESET,
					m,
					ChatColor.GREEN,
					ChatColor.RESET));

		return true;
	}

	public static void add_to_log(Player target, String msg) {
		Deque<String> d = messages.get(target.getUniqueId());
		if (d == null) {
			d = new ArrayDeque<>();
			messages.put(target.getUniqueId(), d);
		}
		while (d.size() > 20) {
			d.removeFirst();
		}
		d.addLast(msg);
	}
}
