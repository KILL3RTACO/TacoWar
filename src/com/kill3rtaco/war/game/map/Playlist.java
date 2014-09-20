package com.kill3rtaco.war.game.map;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.configuration.file.YamlConfiguration;

import com.kill3rtaco.tacoapi.TacoAPI;
import com.kill3rtaco.war.TacoWar;
import com.kill3rtaco.war.ValidatedConfig;
import com.kill3rtaco.war.game.GameType;

public class Playlist extends ValidatedConfig {

	private String _id;
	private HashMap<Map, List<GameType>> _maps;
	private Map _currentMap;
	private GameType _currentGameType;
	private File _file;

	public Playlist(String id) {
		this(id, new HashMap<Map, List<GameType>>());
	}

	public Playlist(File file) {
		super(YamlConfiguration.loadConfiguration(file));
		_file = file;
		_id = getString("id", true);
		reload();
	}

	public Playlist(String id, HashMap<Map, List<GameType>> maps) {
		super(new YamlConfiguration());
		_file = new File(TacoWar.plugin.getDataFolder(), "playlists/playlist_" + id + ".yml");
		_id = id;
		_maps = maps;
	}

	private void reload() {
		if (_config != null) {
			List<String> list = _config.getStringList("playlist");
			_maps = new HashMap<Map, List<GameType>>();
			for (String s : list) {
				String[] split = s.split("\\s+");
				if (split.length <= 1) {
					continue;
				}
				String mapId = split[0];
				Map map = TacoWar.getMap(mapId);
				if (map == null) {
					continue;
				}
				List<GameType> options = new ArrayList<GameType>();
				String[] types = TacoAPI.getChatUtils().removeFirstArg(split);
				for (String t : types) {
					//default types
					if (t.equals("ffa")) {

					}
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

	public Map selectMap() {
		List<Map> maps = new ArrayList<Map>(getMaps());
		if (maps.isEmpty()) {
			_currentMap = null;
			return null;
		}
		_currentMap = maps.get(new Random().nextInt(maps.size()));
		return _currentMap;
	}

	public Map getCurrentMap() {
		return _currentMap;
	}

	public GameType selectGameType() {
		if (_currentMap == null) {
			return null;
		}
		List<GameType> gameTypes = _maps.get(_currentMap);
		if (gameTypes == null || gameTypes.isEmpty()) {
			_currentGameType = null;
			return null;
		}
		_currentGameType = gameTypes.get(new Random().nextInt(gameTypes.size()));
		return _currentGameType;
	}

	public List<Map> getMaps() {
		return new ArrayList<Map>(_maps.keySet());
	}

	public List<GameType> getGameTypesFor(String mapId) {
		for (Map m : getMaps()) {
			if (m.getId().equals(mapId)) {
				return _maps.get(m);
			}
		}
		return null;
	}

	public void save() {
		save(_file);
	}

}
