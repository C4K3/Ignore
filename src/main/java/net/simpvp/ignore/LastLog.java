package net.simpvp.ignore;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.UUID;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class LastLog implements CommandExecutor {

	private static HashMap<UUID, Deque<TextComponent>> messages = new HashMap<>();

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

		Deque<TextComponent> d = messages.get(player.getUniqueId());

		TextComponent m = new TextComponent();

		TextComponent header = new TextComponent("Lastlog:\n");
		header.setColor(ChatColor.GREEN);

		TextComponent footer = new TextComponent("End of Lastlog");
		footer.setColor(ChatColor.GREEN);

		m.addExtra(header);

		if (d != null) {
			for (TextComponent component : d) {
				m.addExtra(component);
			}
		}

		m.addExtra(footer);

		player.spigot().sendMessage(m);

		return true;
	}

	public static void add_to_log(Player target, TextComponent msg) {
		// Clone it to avoid appending newline to stored copy
		TextComponent m = msg.duplicate();
		m.addExtra("\n");

		Deque<TextComponent> d = messages.get(target.getUniqueId());
		if (d == null) {
			d = new ArrayDeque<>();
			messages.put(target.getUniqueId(), d);
		}
		while (d.size() > 20) {
			d.removeFirst();
		}
		d.addLast(m);
	}
}
