package net.simpvp.ignore;

import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Listens to all chat messages, and removes from the recipients any player
 * ignoring the event.getPlayer
 */
public class ChatListener implements Listener {

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled=true)
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		Set<Player> recipients = event.getRecipients();

		if (Storage.getIsIgnored(event.getPlayer())) {
			for (Player p : Ignore.instance.getServer().getOnlinePlayers()) {
				if (Storage.getIsIgnoring(p, event.getPlayer())) {
					recipients.remove(p);
				}
			}
		}

		for (Player p : recipients) {
			if (event.getMessage().startsWith(p.getName() + ": ")) {
				LastLog.add_to_log(p, event.getMessage());
			}
		}
	}
}
