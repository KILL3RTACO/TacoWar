package com.kill3rtaco.war.game.types;

import org.bukkit.configuration.file.YamlConfiguration;

import com.kill3rtaco.war.game.GameTypeOptions;

public class FFAOptions extends GameTypeOptions {
	
	//TODO players have customizable armor colors?
	
	public FFAOptions(YamlConfiguration config) {
		super(config);
		_teamsEnabled = false;
	}
	
	@Override
	public void registerTimers() {
	}
	
	@Override
	public boolean isKillBased() {
		return true;
	}
	
}
