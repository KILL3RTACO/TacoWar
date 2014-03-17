package com.kill3rtaco.war.game.map.options;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import com.kill3rtaco.war.game.GameType;
import com.kill3rtaco.war.game.map.Map;

public class JuggernautMapOptions extends MapOptions {
	
	private Location		_juggernautStartSpawn;
	private List<Location>	_spawns;
	private boolean			_useFFASpawns;
	
	public JuggernautMapOptions(Map map, ConfigurationSection section) {
		super(map, section);
		_juggernautStartSpawn = map.getPointRelative(section.getString("juggernaut_start_spawn"));
		if(_juggernautStartSpawn == null) {
			_valid = false;
		}
		_spawns = map.getLocationList(section.getStringList("spawns"));
		_useFFASpawns = map.gameTypeSupported(GameType.FFA) && section.getBoolean("use_ffa_spawns");
		if(_spawns.isEmpty()) {
			if((_useFFASpawns && map.ffa().spawns().isEmpty())) {
				_valid = false;
			}
		}
	}
	
	public Location juggernautStartSpawn() {
		return _juggernautStartSpawn;
	}
	
	public List<Location> spawns() {
		return _spawns;
	}
	
	public boolean useFFASpawns() {
		return _useFFASpawns;
	}
	
}
