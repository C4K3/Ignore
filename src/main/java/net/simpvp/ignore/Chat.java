package net.simpvp.ignore;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * Various functions for dealing with Minecraft Chat / TextComponent.
 */
public class Chat {

	/**
	 * Common function for sending a message to somebody
	 */
	public static void send_chat(
			Player player,
			TextComponent msg) {
		if (player == null) {
			Ignore.instance.getLogger().info(msg.getText());
		} else {
			player.spigot().sendMessage(msg);
		}
	}

	public static void receive_pm(Player player, Player sender, String message) {
		String s;
		if (sender == null) {
			s = "Server";
		} else {
			s = sender.getName();
		}

		TextComponent m = new TextComponent(String.format("<--%s: %s", s, message));
		m.setColor(ChatColor.GRAY);
		m.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, String.format("/tell %s ", s)));

		send_chat(player, m);
		LastLog.add_to_log(player, m);
	}

	public static void send_pm(Player player, Player target, String message) {
		String s;
		if (target == null) {
			s = "Server";
		} else {
			s = target.getName();
		}

		TextComponent m = new TextComponent(String.format("-->%s: %s", s, message));
		m.setColor(ChatColor.GRAY);
		m.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, String.format("/tell %s ", s)));

		send_chat(player, m);
	}

	public static void server_receive_pm(Player player, String message) {
		TextComponent m = new TextComponent(String.format("Server<--%s: %s", player.getName(), message));
		m.setColor(ChatColor.DARK_GRAY);
		m.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, String.format("/servertell %s ", player.getName())));

		send_chat(null, m);
		for (Player p : Ignore.instance.getServer().getOnlinePlayers()) {
			if (p.isOp() == false) {
				continue;
			}

			send_chat(p, m);
			LastLog.add_to_log(p, m);
		}
	}

	public static void server_send_pm(Player player, String message) {
		TextComponent m = new TextComponent(String.format("Server-->%s: %s", player.getName(), message));
		m.setColor(ChatColor.DARK_GRAY);
		m.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, String.format("/servertell %s ", player.getName())));

		send_chat(null, m);
		for (Player p: Ignore.instance.getServer().getOnlinePlayers()) {
			if (p.isOp() == false) {
				continue;
			}

			send_chat(p, m);
		}
	}
}
