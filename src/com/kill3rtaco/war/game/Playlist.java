package com.kill3rtaco.war.game;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

import com.kill3rtaco.war.TacoWar;
import com.kill3rtaco.war.ValidatedConfig;
import com.kill3rtaco.war.game.map.Map;

public class Playlist extends ValidatedConfig {
	
	private String								_id;
	private HashMap<Map, List<GameTypeOptions>>	_maps;
	private File								_file;
	
	public Playlist(String id) {
		this(id, new HashMap<Map, List<GameTypeOptions>>());
	}
	
	public Playlist(File file) {
		super(YamlConfiguration.loadConfiguration(file));
		_file = file;
		_id = getString("id", true);
		reload();
	}
	
	public Playlist(String id, HashMap<Map, List<GameTypeOptions>> maps) {
		super(new YamlConfiguration());
		_id = id;
		_maps = maps;
	}
	
	private void reload() {
		if(_config != null) {
			List<String> list = _config.getStringList("playlist");
			_maps = new HashMap<Map, List<GameTypeOptions>>();
			for(String s : list) {
				String[] split = s.split("\\s+");
				if(split.length == 0) {
					continue;
				}
				String mapId = split[0];
				Map map = TacoWar.plugin.getMap(mapId);
				if(map == null) {
					continue;
				}
				
			}
		}
	}
	
	public String getId() {
		return _id;
	}
	
	public boolean isEmpty() {
		return _maps.isEmpty();
	}
	
	//selectMap()
	//selectGameType() - based on map, fails if map not selected
	
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
	
	public List<Map> getMaps() {
		return _maps;
	}
	
}
