package com.kill3rtaco.war.game;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.configuration.file.YamlConfiguration;

import com.kill3rtaco.war.TacoWar;

public class Playlist {
	
	private String				_id;
	private List<Map>			_maps;
	private File				_file;
	private YamlConfiguration	_config;
	
	public Playlist(String id) {
		this(id, new ArrayList<Map>());
	}
	
	public Playlist(String id, File file) {
		this(id);
		_file = file;
		_config = YamlConfiguration.loadConfiguration(file);
		reload();
	}
	
	public Playlist(String id, List<Map> maps) {
		_id = id;
		_maps = maps;
	}
	
	private void reload() {
		if(_config != null) {
			List<String> list = _config.getStringList("maps");
			for(String s : list) {
				Map m = TacoWar.plugin.getMap(s);
				if(m != null) {
					_maps.add(m);
				}
			}
		}
	}
	
	public String getId() {
		return _id;
	}
	
	public Map getRandomMap() {
		if(_maps.isEmpty()) {
			return TacoWar.plugin.getRandomMap();
		}
		return _maps.get(new Random().nextInt(_maps.size()));
	}
	
	public void save() {
		ArrayList<String> mapNames = new ArrayList<String>();
		for(Map m : _maps) {
			if(m == null)
				continue;
			String id = m.getId();
			if(mapNames.contains(id))
				continue;
			mapNames.add(id);
		}
		_config.set("maps", mapNames);
		try {
			_config.save(_file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addMap(Map map) {
		if(map != null && !hasMap(map)) {
			_maps.add(map);
			save();
		}
	}
	
	public boolean hasMap(Map map) {
		if(map == null)
			return false;
		for(Map m : _maps) {
			if(m.getId().equals(map.getId())) {
				return true;
			}
		}
		return false;
	}
	
	public void removeMap(Map map) {
		if(map != null && hasMap(map)) {
			_maps.remove(map);
			save();
		}
	}
	
}
