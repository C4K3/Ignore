package net.simpvp.ignore;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

import org.apache.commons.lang3.StringUtils;

import net.md_5.bungee.api.chat.TextComponent;

/**
 * Listens to all chat messages, and removes from the recipients any player
 * ignoring the event.getPlayer
 */
public class ChatListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled=true)
	public void onPlayerChat(PlayerChatEvent event) {
		Set<Player> recipients = event.getRecipients();
		recipients.removeIf(p -> Storage.getIsIgnoring(p, event.getPlayer()));

		String raw_chat = String.format(event.getFormat(), event.getPlayer().getDisplayName(), event.getMessage());
		TextComponent chat = Chat.from_args(Arrays.asList(raw_chat.split(" ")));

		// Intentional use of bukkit logger
		Bukkit.getLogger().info(chat.toPlainText());

		event.setCancelled(true);
		for (Player p : recipients) {
			p.spigot().sendMessage(chat);

			if (StringUtils.startsWithIgnoreCase(event.getMessage(), p.getName())) {
				LastLog.add_to_log(p, chat);
			}
		}
	}
}
