package com.kill3rtaco.war.game;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import com.kill3rtaco.war.TacoWar;

public class Map {
	
	private String							_id, _name, _author;
	private boolean							_valid	= true;
	private HashMap<TeamColor, Location>	_spawns;
	private Location						_origin, _lobby;
	private File							_file	= null;
	private YamlConfiguration				_config;
	
	public Map(String id) {
		_id = id;
		_name = "";
		_author = "";
		_spawns = new HashMap<TeamColor, Location>();
		_origin = null;
		_lobby = null;
		_config = new YamlConfiguration();
	}
	
	public Map(File file) {
		_file = file;
		_config = YamlConfiguration.loadConfiguration(file);
		_id = getString("id", true);
		_name = getString("name", true);
		_author = getString("author", false);
		_origin = getLocation("origin", false, true);
		_lobby = getLocation("lobby", true, true);
		_spawns = new HashMap<TeamColor, Location>();
		for(String s : _config.getConfigurationSection("teams").getKeys(false)) {
			TeamColor c = TeamColor.getTeamColor(s);
			if(c != null && _config.isString("teams." + s)) {
				String str = _config.getString("teams." + s);
				Location loc = getPointRelative(str);
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
	
	private String getString(String path, boolean req) {
		if(_config.isString(path))
			return _config.getString(path);
		if(req)
			_valid = false;
		return null;
	}
	
	private Location getLocation(String path, boolean relative, boolean req) {
		if(_config.isString(path)) {
			Location loc = getPoint(_config.getString(path));
			Location value;
			if(relative) {
				value = getPointRelative(loc);
			} else {
				value = loc;
			}
			if(value == null && req) {
				_valid = false;
			} else {
				return value;
			}
		} else if(req) {
			_valid = false;
		}
		return null;
	}
	
	private int getInt(String s) {
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return 0;
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
		int x = getInt(split[0]);
		int y = getInt(split[1]);
		int z = getInt(split[2]);
		int pitch = 0;
		int yaw = 0;
		if(split.length > 3)
			yaw = getInt(split[3]);
		if(split.length > 4)
			yaw = getInt(split[4]);
		return new Location(TacoWar.config.getWarWorld(), x, y, z, yaw, pitch);
	}
	
	public Location getPointRelative(Location loc) {
		if(_origin == null || loc == null) {
			return null;
		}
		double x = _origin.getX() + loc.getX() + .5; //add .5 to get center of block
		double y = _origin.getY() + loc.getY() + .5; //a little bit higher just in case
		double z = _origin.getZ() + loc.getZ() + .5;
		return new Location(TacoWar.config.getWarWorld(), x, y, z);
	}
	
	public Location getPointRelative(String str) {
		return getPointRelative(getPoint(str));
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
	
	public String toMessage() {
		return "&aMap&7: &3" + getName() + " &aby &b" + getAuthor();
	}
	
	public boolean isOriginSet() {
		return _origin != null;
	}
	
	public void setOrigin(Location loc) {
		_origin = loc;
		_config.set("origin", getLocationString(loc));
		save();
	}
	
	public void setLobbyLocationRelative(Location loc) {
		if(_origin == null) {
			return;
		}
		_lobby = loc.subtract(_origin);
	}
	
	private String getLocationString(Location loc) {
		return loc.getBlockX() + " "
				+ loc.getBlockY() + " "
				+ loc.getBlockZ() + " "
				+ getNearestDegree(loc.getYaw(), 45) + " "
				+ getNearestDegree(loc.getPitch(), 45);
	}
	
	public void save() {
		try {
			_file.getParentFile().mkdirs();
			if(_file != null) {
				_config.save(_file);
				return;
			}
			_config.save(TacoWar.plugin.getDataFolder() + "/maps/map_" + _id + "/map.yml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static int getNearestDegree(double degree, double factor) {
		return (int) (Math.round(degree / factor) * factor);
	}
	
}
