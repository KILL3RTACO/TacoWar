package com.kill3rtaco.war.game.map.options;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import com.kill3rtaco.war.game.map.Map;

public class HideAndSeekMapOptions extends MapOptions {
	
	private List<Location>	_seekerSpawns, _hiderSpawns;
	
	public HideAndSeekMapOptions(Map map, ConfigurationSection section) {
		super(map);
		_seekerSpawns = map.getLocationList(section.getStringList("seeker_spawns"));
		_hiderSpawns = map.getLocationList(section.getStringList("hider_spawns"));
	}
	
	public List<Location> seekerSpawns() {
		return _seekerSpawns;
	}
	
	public List<Location> hiderSpawns() {
		return _hiderSpawns;
	}
	
}
