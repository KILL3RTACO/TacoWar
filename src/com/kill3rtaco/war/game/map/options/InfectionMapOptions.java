package com.kill3rtaco.war.game.map.options;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import com.kill3rtaco.war.game.map.Map;

public class InfectionMapOptions extends MapOptions {
	
	private List<Location>	_infectedSpawns, _survivorSpawns;
	private String			_infectedName, _survivorName;
	
	public InfectionMapOptions(Map map, ConfigurationSection section) {
		super(map, section);
		_infectedSpawns = map.getLocationList(section.getStringList("infected_spawns"));
		_survivorSpawns = map.getLocationList(section.getStringList("survivor_spawns"));
		_infectedName = section.getString("infected_name", "Infected");
		_survivorName = section.getString("survivor_name", "Survivor");
	}
	
	public List<Location> infectedSpawns() {
		return _infectedSpawns;
	}
	
	public List<Location> survivorSpawns() {
		return _survivorSpawns;
	}
	
	public String infectedName() {
		return _infectedName;
	}
	
	public String survivorName() {
		return _survivorName;
	}
}
