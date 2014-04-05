package com.kill3rtaco.war.game.map.options;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import com.kill3rtaco.war.game.map.Map;
import com.kill3rtaco.war.game.map.options.koth.Hill;

public class KOTHMapOptions extends MapOptions {
	
	private List<Hill>	_hills;
	
	public KOTHMapOptions(Map map, ConfigurationSection section) {
		super(map, section);
		_hills = new ArrayList<Hill>();
		if(section.isConfigurationSection("hills")) {
			for(String s : section.getConfigurationSection("hills").getKeys(false)) {
				String node = "hills." + s;
				String location = section.getString(node + ".location");
				int radius = section.getInt(node + ".radius", 10);
				Location loc = map.getPointRelative(location);
				if(loc == null)
					continue;
				_hills.add(new Hill(loc, radius));
			}
		}
		if(_hills.isEmpty()) {
			_valid = false;
		}
	}
}
