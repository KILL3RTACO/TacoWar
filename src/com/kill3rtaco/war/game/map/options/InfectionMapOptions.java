package com.kill3rtaco.war.game.map.options;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import com.kill3rtaco.war.game.map.Map;

public class InfectionMapOptions extends MapOptions {
	
	private List<Location>	_infectedSpawns, _survivorSpawns;
	
	public InfectionMapOptions(Map map, ConfigurationSection section) {
		super(map);
		_infectedSpawns = map.getLocationList(section.getStringList("infected_spawns"));
		_survivorSpawns = map.getLocationList(section.getStringList("survivor_spawns"));
	}
	
	public List<Location> infectedSpawns() {
		return _infectedSpawns;
	}
	
	public List<Location> survivorSpawns() {
		return _survivorSpawns;
	}
}
