package com.kill3rtaco.war.game.map;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import com.kill3rtaco.war.Identifyable;
import com.kill3rtaco.war.TW;
import com.kill3rtaco.war.ValidatedConfig;
import com.kill3rtaco.war.game.GameType;

public class Playlist extends ValidatedConfig implements Identifyable {

	public static final String				KEY_ID			= "id";
	public static final String				KEY_MAPS		= "maps";
	public static final String				KEY_GAMETYPES	= "gametypes";

	private HashMap<WarMap, List<GameType>>	_maps;
	private WarMap							_currentMap;
	private GameType						_currentGameType;
	private File							_file;

	public Playlist(String id) {
		super(new YamlConfiguration());
		_config.set(KEY_ID, id);
		_file = new File(TW.PL_FOLDER, id + ".yml");
	}

	public Playlist(ConfigurationSection config) {
		super(config);
		reload();
	}

	/*
	 * maps: 
	 *   map_id: 
	 *     - gametype_id 
	 *     - gametype_id 
	 * gametypes: 
	 *    - gametype_id 
	 *    - gametype_id
	 */
	//TODO: Actually do stuff here
	private void reload() {
		if (_config == null)
			return;

		List<String> list = _config.getStringList("playlist");
		_maps = new HashMap<WarMap, List<GameType>>();
	}

	public String getId() {
		return _config.getString(KEY_ID);
	}

	public boolean isEmpty() {
		return _maps.isEmpty();
	}

	//selectMap()
	//selectGameType() - based on map, fails if map not selected

	public WarMap selectMap() {
		List<WarMap> maps = new ArrayList<WarMap>(getMaps());
		if (maps.isEmpty()) {
			_currentMap = null;
			return null;
		}
		_currentMap = maps.get(new Random().nextInt(maps.size()));
		return _currentMap;
	}

	public WarMap getCurrentMap() {
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

	public List<WarMap> getMaps() {
		return new ArrayList<WarMap>(_maps.keySet());
	}

	public List<GameType> getGameTypesFor(String mapId) {
		for (WarMap m : getMaps()) {
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
