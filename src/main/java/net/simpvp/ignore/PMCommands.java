package net.simpvp.ignore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * Implements the /r and /m commands
 */
public class PMCommands implements CommandExecutor {

	/* (both hashmaps) Key is the sender, value is their recipient
	 * I.e. if (A, B) is in mPlayerList, then the next time A
	 * does /m it will be sent to B, and similar for rPlayerList */
	public static HashMap<UUID, UUID> mPlayerList
		= new HashMap<UUID, UUID>();
	public static HashMap<UUID, UUID> rPlayerList
		= new HashMap<UUID, UUID>();

	public boolean onCommand(
			CommandSender sender,
			Command cmd,
			String label,
			String[] args) {

		Player player = null;
		UUID uplayer = null;
		if (sender instanceof Player) {
			player = (Player) sender;
			uplayer = player.getUniqueId();
		}

		if (args.length < 1) {
			TextComponent m = new TextComponent("You cannot send an empty message.");
			m.setColor(ChatColor.RED);
			Chat.send_chat(player, m);

			return true;
		}

		UUID utarget;
		if (cmd.getName().equalsIgnoreCase("m")) {
			if (!mPlayerList.containsKey(uplayer)) {
				TextComponent m = new TextComponent("You have not sent any PMs yet.");
				m.setColor(ChatColor.RED);
				Chat.send_chat(player, m);
				return true;
			}
			utarget = mPlayerList.get(uplayer);
		} else {
			if (!rPlayerList.containsKey(uplayer)) {
				TextComponent m = new TextComponent("You have not received any PMs yet.");
				m.setColor(ChatColor.RED);
				Chat.send_chat(player, m);
				return true;
			}
			utarget = rPlayerList.get(uplayer);
		}

		Player target = null;
		if (utarget != null) {
			target = Ignore.instance.getServer().getPlayer(utarget);
		}

		TellCommand.sendPM(player, target, args, utarget == null);

		return true;
	}

}

