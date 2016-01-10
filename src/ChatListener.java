package net.simpvp.Ignore;

import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Listens to all chat messages, and removes any recipients as necessary.
 */
public class ChatListener implements Listener {

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled=true)
	public void onPlayerChat(AsyncPlayerChatEvent event) {

		if (!Storage.getIsIgnored(event.getPlayer()))
			return;

		Set<Player> recipients = event.getRecipients();

		for (Player p : Ignore.instance.getServer().getOnlinePlayers()) {
			if (Storage.getIsIgnoring(p, event.getPlayer())) {
				recipients.remove(p);
			}
		}

	}

}

