package net.simpvp.ignore;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * Filters death messages based on ignore and ignore-death settings,
 * while preserving the original message data (hover, lore, etc).
 */
public class DeathListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerDeath(PlayerDeathEvent event) {
		Component msg = event.deathMessage();
		if (msg == null) {
			return;
		}

		Player dead = event.getEntity();
		event.deathMessage(null);

		String plain = PlainTextComponentSerializer.plainText().serialize(msg);
		Bukkit.getLogger().info(plain);

		for (Player p : Bukkit.getOnlinePlayers()) {
			if (Storage.getIgnoreDeath(p) && Storage.getIsIgnoring(p, dead)) {
				continue;
			}

			p.sendMessage(msg);
		}
	}
}
