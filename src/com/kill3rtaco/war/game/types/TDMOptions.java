package com.kill3rtaco.war.game.types;

import org.bukkit.configuration.file.YamlConfiguration;

import com.kill3rtaco.war.game.GameTypeOptions;

public class TDMOptions extends GameTypeOptions {
	
	public TDMOptions(YamlConfiguration config) {
		super(config);
		_teamsEnabled = true; //teams_enabled ignored in .yml
		
	}
	
	@Override
	public void registerTimers() {
	}
	
	@Override
	public boolean isKillBased() {
		return true;
	}
	
}
