package net.simpvp.ignore;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.ArrayDeque;
import java.util.UUID;

/**
 * Retrieves ignore info for each player who logs in from the SQLite db
 */
public class PlayerJoin implements Listener {

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled=true)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		/* If the player is already in one of the maps, then it can be
		 * presumed that the player has already been loaded from the
		 * database */
		if (Storage.getIsIgnored(player)
				|| Storage.getIsIgnoring(player)) {
			return;
		}

		UUID ignorer = player.getUniqueId();

		ArrayDeque<UUIDName> array
			= SQLite.get_ignoring(ignorer);

		Iterator<UUIDName> iter = array.iterator();

		while (iter.hasNext()) {
			UUIDName tmp = iter.next();

			Storage.addIgnore(ignorer, tmp.uuid, tmp.name);
		}
	}
}

