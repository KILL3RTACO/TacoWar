package com.kill3rtaco.war.game.map.options;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import com.kill3rtaco.war.game.map.Map;
import com.kill3rtaco.war.game.player.TeamColor;

public class TDMMapOptions extends MapOptions {
	
	private HashMap<TeamColor, List<Location>>	_spawns;
	
	public TDMMapOptions(Map map, ConfigurationSection section) {
		super(map);
		_spawns = new HashMap<TeamColor, List<Location>>();
		for(String s : section.getConfigurationSection("team_spawns").getKeys(false)) {
			TeamColor c = TeamColor.getTeamColor(s);
			if(c == null) {
				continue;
			}
			List<Location> locs = map.getLocationList(section.getStringList("team_spawns." + s));
			if(!locs.isEmpty()) {
				_spawns.put(c, locs);
			}
		}
		if(_spawns.size() < 2) {
			_valid = false;
		}
	}
	
}
