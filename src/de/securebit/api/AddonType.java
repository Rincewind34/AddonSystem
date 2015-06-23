package de.securebit.api;

public enum AddonType {
	
	CHAT("chat"),
	CHEST("chest"),
	CHESTITEMS("chestitems"),
	MAP("map"),
	SPECIAL("special"),
	SPECTATOR("spectator"),
	TELEPORTER("teleporter");
	
	private String name;
	
	private AddonType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
