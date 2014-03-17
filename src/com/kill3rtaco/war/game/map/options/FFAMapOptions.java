package com.kill3rtaco.war.game.map.options;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import com.kill3rtaco.war.game.map.Map;

public class FFAMapOptions extends MapOptions {
	
	private List<Location>	_spawns;
	
	public FFAMapOptions(Map map, ConfigurationSection section) {
		super(map);
		_spawns = map.getLocationList(section.getStringList("spawns"));
		if(_spawns.isEmpty()) {
			_valid = false;
		}
	}
	
	public List<Location> spawns() {
		return _spawns;
	}
}
