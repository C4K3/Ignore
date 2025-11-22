package net.simpvp.ignore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.UUID;

public class SQLite {

	private static Connection conn = null;

	/**
	 * Opens the SQL connection
	 */
	public static void connect() {
		String database = "jdbc:sqlite:plugins/Ignore/ignore.sqlite";

		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection(database);

			String query = "PRAGMA user_version;";
			PreparedStatement st = conn.prepareStatement(query);

			/* Get database version */
			ResultSet rs = st.executeQuery();

			int user_version = 0;
			while (rs.next()) {
				user_version = rs.getInt("user_version");
			}

			rs.close();
			st.close();

			switch (user_version) {

				/* Database is brand new. Create tables */
				case 0: {
					Ignore.instance.getLogger().info(
							"Database not yet created. Creating ...");
					query = "CREATE TABLE ignores "
						+ "(id INTEGER PRIMARY KEY AUTOINCREMENT, "
						+ "ignorer BLOB, "
						+ "ignored BLOB, "
						+ "ignoredname TEXT);";
					st = conn.prepareStatement(query);
					st.executeUpdate();
					st.close();

					query = "PRAGMA user_version = 1;";
					st = conn.prepareStatement(query);
					st.executeUpdate();
					st.close();

				}

				case 1: {
					Ignore.instance.getLogger().info(
							"Migrating to version 2");
					Statement statement = conn.createStatement();
					query = ""
						+ "CREATE INDEX idx_ignores_both ON ignores (ignorer, ignored);"

						+ "PRAGMA user_version = 2;";
					statement.executeUpdate(query);
					statement.close();
				}

				case 2: {
					Ignore.instance.getLogger().info(
							"Migrating to version 3");
					Statement statement = conn.createStatement();
					query = ""
						+ "CREATE TABLE ignoredeath (player_uuid BLOB PRIMARY KEY);"

						+ "PRAGMA user_version = 3;";
					statement.executeUpdate(query);
					statement.close();
				}
			}

		} catch (Exception e) {
			Ignore.instance.getLogger().severe(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Closes the database connection
	 */
	public static void close() {
		try {
			conn.close();
		} catch (Exception e) {
			Ignore.instance.getLogger().severe(e.getMessage());
		}
	}

	/**
	 * Adds it to the database that ignorer is ignoring ignored
	 */
	public static void add_ignore(
			UUID ignorer,
			UUID ignored,
			String ignoredname) {

		try {
			String query = "INSERT INTO ignores (ignorer, ignored, ignoredname)"
				+ " VALUES (?, ?, ?)";
			PreparedStatement st = conn.prepareStatement(query);
			st.setString(1, ignorer.toString());
			st.setString(2, ignored.toString());
			st.setString(3, ignoredname);

			st.executeUpdate();
			st.close();
		} catch (Exception e) {
			Ignore.instance.getLogger().warning(e.getMessage());
		}
	}

	/**
	 * Removes from the database that ignorer is ignoring ignored
	 */
	public static void remove_ignore(UUID ignorer, UUID ignored) {
		try {
			String query = "DELETE FROM ignores WHERE "
				+ "ignorer = ? AND ignored = ?;";
			PreparedStatement st = conn.prepareStatement(query);
			st.setString(1, ignorer.toString());
			st.setString(2, ignored.toString());

			st.executeUpdate();
			st.close();
		} catch (Exception e) {
			Ignore.instance.getLogger().warning(e.getMessage());
		}
	}

	/**
	 * Get a list of all the people that ignorer is ignoring
	 */
	public static ArrayDeque<UUIDName> get_ignoring(UUID ignorer) {
		ArrayDeque<UUIDName> ret = null;
		try {
			String query = "SELECT * FROM ignores WHERE "
				+ "ignorer = ?;";
			PreparedStatement st = conn.prepareStatement(query);
			st.setString(1, ignorer.toString());

			ResultSet rs = st.executeQuery();
			ret = new ArrayDeque<UUIDName>();
			while (rs.next()) {
				UUID uuid = UUID.fromString(
						rs.getString("ignored"));
				String name = rs.getString("ignoredname");

				UUIDName tmp = new UUIDName(uuid, name);
				ret.add(tmp);
			}

			rs.close();
			st.close();

		} catch (Exception e) {
			Ignore.instance.getLogger().warning(e.getMessage());
		}

		return ret;
	}

	/**
	 * Returns all players who have ignore-death enabled.
	 */
	public static HashSet<UUID> get_ignore_death_all() {
		HashSet<UUID> ret = new HashSet<UUID>();
		try {
			String query = "SELECT player_uuid FROM ignoredeath;";
			PreparedStatement st = conn.prepareStatement(query);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				String uuidStr = rs.getString("player_uuid");
				try {
					UUID uuid = UUID.fromString(uuidStr);
					ret.add(uuid);
				} catch (IllegalArgumentException ex) {
					// Ignore malformed UUIDs
				}
			}

			rs.close();
			st.close();
		} catch (Exception e) {
			Ignore.instance.getLogger().warning(e.getMessage());
		}
		return ret;
	}

	/**
	 * Sets whether a player has ignore-death enabled.
	 */
	public static void set_ignore_death(UUID player, boolean enabled) {
		try {
			if (enabled) {
				String query = "INSERT OR IGNORE INTO ignoredeath (player_uuid)"
						+ " VALUES (?);";
				PreparedStatement st = conn.prepareStatement(query);
				st.setString(1, player.toString());
				st.executeUpdate();
				st.close();
			} else {
				String query = "DELETE FROM ignoredeath WHERE player_uuid = ?;";
				PreparedStatement st = conn.prepareStatement(query);
				st.setString(1, player.toString());
				st.executeUpdate();
				st.close();
			}
		} catch (Exception e) {
			Ignore.instance.getLogger().warning(e.getMessage());
		}
	}
}


