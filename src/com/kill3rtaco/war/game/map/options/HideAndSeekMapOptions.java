package com.kill3rtaco.war.game.map.options;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import com.kill3rtaco.war.game.map.Map;

public class HideAndSeekMapOptions extends MapOptions {
	
	private List<Location>	_seekerSpawns, _hiderSpawns;
	private String			_hiderName, _seekerName;
	
	public HideAndSeekMapOptions(Map map, ConfigurationSection section) {
		super(map, section);
		_seekerSpawns = map.getLocationList(section.getStringList("seeker_spawns"));
		_hiderSpawns = map.getLocationList(section.getStringList("hider_spawns"));
		_hiderName = section.getString("hider_name", "Hider");
		_seekerName = section.getString("seeker_name", "Seeker");
	}
	
	public List<Location> seekerSpawns() {
		return _seekerSpawns;
	}
	
	public List<Location> hiderSpawns() {
		return _hiderSpawns;
	}
	
	public String hiderName() {
		return _hiderName;
	}
	
	public String seekerName() {
		return _seekerName;
	}
	
}
