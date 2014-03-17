package com.kill3rtaco.war.game.map.options;

import org.bukkit.configuration.ConfigurationSection;

import com.kill3rtaco.war.game.map.Map;

public abstract class MapOptions {
	
	protected boolean	_valid	= true;
	protected Map		_map;
	
	public MapOptions(Map map, ConfigurationSection section) {
		_map = map;
	}
	
	public boolean isValid() {
		return _valid;
	}
	
	public Map getMap() {
		return _map;
	}
	
}
