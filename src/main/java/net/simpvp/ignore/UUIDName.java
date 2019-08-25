package net.simpvp.ignore;

import java.util.UUID;

/**
 * Because java doesn't have structs
 */
public class UUIDName {
	public UUID uuid;
	public String name;

	public UUIDName(UUID uuid, String name) {
		this.uuid = uuid;
		this.name = name;
	}

}

