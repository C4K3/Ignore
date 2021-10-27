package net.simpvp.ignore;

import org.bukkit.entity.Player;

import org.apache.commons.lang3.StringUtils;

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

	public static void receive_pm(Player player, Player sender, String message[]) {
		String s;
		if (sender == null) {
			s = "Server";
		} else {
			s = sender.getName();
		}

		TextComponent m = new TextComponent(String.format("<--%s: ", s));
		m.setColor(ChatColor.GRAY);
		m.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, String.format("/tell %s ", s)));
		m.addExtra(from_args(message));

		send_chat(player, m);
		LastLog.add_to_log(player, m);
	}

	public static void send_pm(Player player, Player target, String message[]) {
		String s;
		if (target == null) {
			s = "Server";
		} else {
			s = target.getName();
		}

		TextComponent m = new TextComponent(String.format("-->%s: ", s));
		m.setColor(ChatColor.GRAY);
		m.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, String.format("/tell %s ", s)));
		m.addExtra(from_args(message));

		send_chat(player, m);
	}

	public static void server_receive_pm(Player player, String[] message) {
		TextComponent m = new TextComponent(String.format("Server<--%s: ", player.getName()));
		m.setColor(ChatColor.DARK_GRAY);
		m.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, String.format("/servertell %s ", player.getName())));
		m.addExtra(from_args(message));

		send_chat(null, m);
		for (Player p : Ignore.instance.getServer().getOnlinePlayers()) {
			if (p.isOp() == false) {
				continue;
			}

			send_chat(p, m);
			LastLog.add_to_log(p, m);
		}
	}

	public static void server_send_pm(Player player, String[] message) {
		TextComponent m = new TextComponent(String.format("Server-->%s: ", player.getName()));
		m.setColor(ChatColor.DARK_GRAY);
		m.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, String.format("/servertell %s ", player.getName())));
		m.addExtra(from_args(message));

		send_chat(null, m);
		for (Player p: Ignore.instance.getServer().getOnlinePlayers()) {
			if (p.isOp() == false) {
				continue;
			}

			send_chat(p, m);
		}
	}

	/**
	 * Given a list of arguments, assemble them into a space-separated TextComponent.
	 *
	 * Links will be given an OPEN_URL ClickEvent.
	 *
	 * Note that the returned TextComponent will always start with a space ' '.
	 */
	static TextComponent from_args(String[] args) {
		TextComponent ret = new TextComponent();
		String c = "";
		boolean not_first = false;
		for (String arg : args) {
			if (not_first) {
				c += " ";
			}
			not_first = true;

			// If an arg contains a period, we consider it as (maybe)
			// an url.
			//
			// Yeah, urls don't have to contain a dot,
			// but this is close enough for most use-cases.
			//
			// There's no real harm in marking something that's not
			// an url as an url. (Maybe we should just mark
			// everything as an url?)
			if (arg.contains(".")) {
				if (!c.isEmpty()) {
					ret.addExtra(c);
				}
				c = "";

				String url;
				if (StringUtils.startsWithIgnoreCase(arg, "http://") || StringUtils.startsWithIgnoreCase(arg, "https://")) {
					url = arg;

				} else {
					url = "https://" + arg;
				}

				TextComponent link = new TextComponent(arg);
				link.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
				ret.addExtra(link);

			} else {
				c += arg;
			}
		}
		if (!c.isEmpty()) {
			ret.addExtra(c);
		}

		return ret;
	}
}
