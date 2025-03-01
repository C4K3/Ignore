package net.simpvp.ignore;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * Takes over the /tell command.
 * Respects people ignoring other players, and also lets players /tell server
 */
public class TellCommand implements CommandExecutor {

	public boolean onCommand(
			CommandSender sender,
			Command cmd,
			String label,
			String[] args) {

		Player player = null;
		if (sender instanceof Player) {
			player = (Player) sender;
		} else {
			return ServertellCommand.server_tell_command(sender, cmd, label, args);
		}

		if (args.length < 2) {
			TextComponent m = new TextComponent("Usage: /tell <player> <private message ...>");
			m.setColor(ChatColor.RED);
			Chat.send_chat(player, m);
			return true;
		}

		/* Only used as command argument */
		@SuppressWarnings("deprecation")
		Player target = Ignore.instance.getServer()
				.getPlayerExact(args[0]);

		List<String> msg = Arrays.stream(args, 1, args.length).toList();

		sendPM(player, target, msg, args[0].equalsIgnoreCase("server"));

		return true;
	}

	/**
	 * Have player send msg to target
	 */
	public static void sendPM(
			Player player,
			Player target,
			Collection<String> msg,
			boolean is_target_server) {

		/* String of the player, in case it's null and it should
		 * be 'Server' */
		String splayer;
		/* Command sender's uuid */
		UUID uplayer = null;
		if (player == null) {
			splayer = "Server";
		} else {
			splayer = player.getName();
			uplayer = player.getUniqueId();
		}

		if (target == null && !is_target_server) {
			TextComponent m = new TextComponent("No such player online.");
			m.setColor(ChatColor.RED);
			Chat.send_chat(player, m);
			return;
		}

		String starget;
		UUID utarget = null;
		if (target == null) {
			starget = "Server";
		} else {
			starget = target.getName();
			utarget = target.getUniqueId();
		}

		/* Don't let people figure out if a vanished admin is
		 * online or not */
		if (player == null || target == null || player.canSee(target)) {
			Chat.send_pm(player, target, msg);
			PMCommands.mPlayerList.put(uplayer, utarget);
		} else {
			TextComponent m = new TextComponent("No such player online.");
			m.setColor(ChatColor.RED);
			Chat.send_chat(player, m);
		}

		/* Only actually send the message to the recipient if they're
		 * not ignoring the sender */
		if (is_target_server) {
			Chat.server_receive_pm(player, msg);
		} else if (player == null || !Storage.getIsIgnoring(target, player)) {
			Chat.receive_pm(target, player, msg);
			PMCommands.rPlayerList.put(utarget, uplayer);
		}

		return;
	}
}
