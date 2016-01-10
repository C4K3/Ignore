package net.simpvp.Ignore;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.Listener;
import org.bukkit.ChatColor;

/**
 * Listens for the /tell command, and cancels it if target is ignoring sender.
 * 
 * A bit hacky solution that uses PlayerCommandPreprocessEvent, but it works.
 */
public class TellListener implements Listener {

	@EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
	public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {

		if (!Storage.getIsIgnored(event.getPlayer()))
			return;

		String[] args = event.getMessage().split("\\s+");

		if (!args[0].equalsIgnoreCase("/tell"))
			return;

		if (args.length < 2)
			return;

		@SuppressWarnings("deprecation") /* Only used to get as command argument */
		Player target = Ignore.instance.getServer().getPlayerExact(args[1]);

		if (target == null)
			return;

		if (!Storage.getIsIgnoring(target, event.getPlayer()))
			return;

		event.setCancelled(true);

		/* Send fake message */
		String msg = ChatColor.GRAY + "" + ChatColor.ITALIC + "You whisper to " + target.getName() + ":";
		for (int i = 2; i < args.length; i++)
			msg += " " + args[i];

		event.getPlayer().sendMessage(msg);

	}

}

