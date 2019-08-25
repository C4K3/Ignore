package net.simpvp.ignore;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

/**
 * This class keeps track of which players are ignoring which other players.
 */
public class Storage {

	/* Key is player who is ignored, and the arraydeque is a list of all
	 * players who have ignored the key. */
	private static HashMap<UUID, ArrayDeque<UUID>> ignoreMap
		= new HashMap<UUID, ArrayDeque<UUID>>();

	/* Key is player who is ignoring, value is name of player ignorer
	 * is ignoring */
	private static HashMap<UUID, ArrayDeque<String>> ignoringMap
		= new HashMap<UUID, ArrayDeque<String>>();

	/**
	 * Returns whether player is ignored by anybody.
	 * @param uuid Player to check if is ignored.
	 * @return True if player is ignored by at least 1 player, else false.
	 */
	public static boolean getIsIgnored(Player player) {
		return ignoreMap.containsKey(player.getUniqueId());
	}

	/**
	 * Returns whether player is ignoring anybody
	 * @param ignorer Player to check if is ignoring anybody
	 * @return True if ignored is ignoring at least 1 player, else false
	 */
	public static boolean getIsIgnoring(Player ignorer) {
		return ignoringMap.containsKey(ignorer.getUniqueId());
	}

	/**
	 * Gets whether ignorer is ignoring ignored.
	 * @param ignorer Player who is ignoring.
	 * @param ignored Player who is being ignored.
	 * @return True if ignorer is ignoring ignored, else false.
	 */
	public static boolean getIsIgnoring(Player ignorer, Player ignored) {
		ArrayDeque<UUID> ignorers = ignoreMap.get(ignored.getUniqueId());
		if (ignorers == null)
			return false;
		return ignorers.contains(ignorer.getUniqueId());
	}

	/**
	 * Returns All the players whom the ignorer is ignoring.
	 * @param ignorer Player who is ignoring.
	 * @return ArrayDeque<String> of player names
	 */
	public static ArrayDeque<String> getListIgnoring(Player ignorer) {
		return ignoringMap.get(ignorer.getUniqueId());
	}

	/**
	 * Has ignorer unignore ignored.
	 * @param ignorer Player who is ignoring.
	 * @param ignored Player who is being ignored.
	 */
	public static void removeIgnore(Player ignorer, Player ignored) {
		ArrayDeque<UUID> ignorers
			= ignoreMap.get(ignored.getUniqueId());
		ignorers.remove(ignorer.getUniqueId());
		ignoreMap.put(ignored.getUniqueId(), ignorers);

		ArrayDeque<String> ignoring
			= ignoringMap.get(ignorer.getUniqueId());
		ignoring.remove(ignored.getName());
		ignoringMap.put(ignorer.getUniqueId(), ignoring);
	}

	/**
	 * Has ignorer ignore ignored.
	 * @param ignorer Player who is ignoring.
	 * @param ignored Player who is being ignored.
	 */
	public static void addIgnore(Player ignorer, Player ignored) {
		addIgnore(ignorer.getUniqueId(),
				ignored.getUniqueId(),
				ignored.getName());
	}

	/**
	 * Has ignorer ignore ignored.
	 * @param ignorer Player who is ignoring.
	 * @param ignored Player who is being ignored.
	 * @param ignored_name Name of player who is being ignored.
	 */
	public static void addIgnore(UUID ignorer,
			UUID ignored,
			String ignored_name) {

		ArrayDeque<UUID> ignorers
			= ignoreMap.get(ignored);

		if (ignorers == null) {
			ignorers = new ArrayDeque<UUID>();
		}

		ignorers.addLast(ignorer);
		ignoreMap.put(ignored, ignorers);

		ArrayDeque<String> ignoring
			= ignoringMap.get(ignorer);

		if (ignoring == null) {
			ignoring = new ArrayDeque<String>();
		}

		ignoring.addLast(ignored_name);
		ignoringMap.put(ignorer, ignoring);
	}
}


