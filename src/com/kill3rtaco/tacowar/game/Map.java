package com.kill3rtaco.tacowar.game;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import com.kill3rtaco.tacowar.TacoWar;

public class Map {
	
	private String							_id, _name, _author;
	private boolean							_valid	= true;
	private HashMap<TeamColor, Location>	_spawns;
	private Location						_origin, _lobby;
	
	public Map(YamlConfiguration config) {
		setString(config, _id, "id", true);
		setString(config, _name, "name", true);
		setString(config, _author, "author", false);
		setLocation(config, _origin, "origin", false, true);
		setLocation(config, _lobby, "lobby", true, true);
		_spawns = new HashMap<TeamColor, Location>();
		for(String s : config.getConfigurationSection("teams").getKeys(false)) {
			TeamColor c = TeamColor.getTeamColor(s);
			if(c != null && config.isString("teams." + s)) {
				String str = config.getString("teams." + s);
				Location loc = getPoint(str);
				if(loc == null) {
					continue;
				}
				_spawns.put(c, loc);
			}
		}
		if(_spawns.size() < 2) {
			_valid = false;
		}
	}
	
	private void setString(YamlConfiguration c, String field, String path, boolean req) {
		if(c.isString(path))
			field = c.getString(path);
		else if(req)
			_valid = false;
	}
	
	private void setLocation(YamlConfiguration c, Location field, String path, boolean relative, boolean req) {
		if(c.isString(path)) {
			Location loc = getPoint(c.getString(path));
			if(relative) {
				field = getPointRelative(loc);
			} else {
				field = loc;
			}
			if(field == null && req) {
				_valid = false;
			}
		} else if(req) {
			_valid = false;
		}
	}
	
	public boolean isValid() {
		return _valid;
	}
	
	public String getId() {
		return _id;
	}
	
	public String getName() {
		return _name;
	}
	
	public String getAuthor() {
		return _author != null ? _author : "Unknown";
	}
	
	private Location getPoint(String s) {
		String[] split = s.split("\\s+");
		if(split.length < 3)
			return null;
		try {
			int x = Integer.parseInt(split[0]);
			int y = Integer.parseInt(split[1]);
			int z = Integer.parseInt(split[2]);
			return new Location(TacoWar.config.getWarWorld(), x, y, z);
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	public Location getPointRelative(Location loc) {
		if(_origin == null)
			return null;
		double x = _origin.getX() + loc.getX();
		double y = _origin.getY() + loc.getY();
		double z = _origin.getZ() + loc.getZ();
		return new Location(TacoWar.config.getWarWorld(), x, y, z);
	}
	
	public Location getTeamSpawn(TeamColor team) {
		return _spawns.get(team);
	}
	
	public HashMap<TeamColor, Location> getTeamSpawns() {
		return _spawns;
	}
	
	public Location getOrigin() {
		return _origin;
	}
	
	public Location getLobbyLocation() {
		return _lobby;
	}
	
}
