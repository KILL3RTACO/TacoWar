package com.kill3rtaco.war.game;

public enum GameType {
	
	//base gametypes
	FFA("Free for All"),
	HIDE_AND_SEEK("Hide and Seek", "has"),
	INFECTION("Infection", "inf"),
	JUGGERNAUT("Juggernaut", "jug"),
	KOTH("King of the Hill"),
	TDM("Team Deathmatch");
	
	private String		_name;
	private String[]	_aliases;
	
	private GameType(String name, String... aliases) {
		_name = name;
		_aliases = aliases;
	}
	
	public String getName() {
		return _name;
	}
	
	public boolean hasAlias(String alias) {
		if(name().equalsIgnoreCase(alias)) {
			return true;
		}
		for(String s : _aliases) {
			if(s.equalsIgnoreCase(alias)) {
				return true;
			}
		}
		return false;
	}
	
	public static GameType getGameType(String name) {
		for(GameType gm : values()) {
			if(gm.hasAlias(name)) {
				return gm;
			}
		}
		return null;
	}
	
}
