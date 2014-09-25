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
import com.kill3rtaco.war.TacoWar;
import com.kill3rtaco.war.ValidatedConfig;
import com.kill3rtaco.war.game.GameType;

public class Playlist extends ValidatedConfig implements Identifyable {

	public static final String				KEY_ID			= "id";
	public static final String				KEY_MAPS		= "maps";
	public static final String				KEY_GAMETYPES	= "gametypes";

	private HashMap<WarMap, List<GameType>>	_maps;
	private List<GameType>					_genericGameTypes;
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
	public void reload() {
		if (_config == null || !_config.isConfigurationSection(KEY_MAPS))
			return;

		_maps = new HashMap<WarMap, List<GameType>>();
		List<String> list = new ArrayList<String>(_config.getConfigurationSection(KEY_MAPS).getKeys(false));
		for (String s : list) {
			WarMap map = TacoWar.getMap(s);
			if (map == null)
				continue;
			List<String> ids = getStringList(KEY_MAPS + "." + s, false);
			List<GameType> gametypes = TacoWar.getGameTypes(ids);
			if (gametypes.isEmpty())
				continue;
			_maps.put(map, gametypes);
		}
		List<String> ids = getStringList(KEY_GAMETYPES, false);
		if (ids == null || ids.isEmpty())
			return;
		_genericGameTypes = TacoWar.getGameTypes(ids);
	}

	public String getId() {
		return _config.getString(KEY_ID);
	}

	public boolean isEmpty() {
		return _maps.isEmpty();
	}

	//selectMap()
	//selectGameType() - based on map, fails if map not selected

	public WarMap selectMap(int playerCount) {
		List<WarMap> maps = getMaps();
		if (maps.isEmpty()) {
			_currentMap = null;
			return null;
		}
		List<WarMap> available = new ArrayList<WarMap>();
		for (WarMap m : maps) {
			if (m.canHoldPlayers(playerCount))
				available.add(m);
		}
		_currentMap = available.get(new Random().nextInt(available.size()));
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
		List<GameType> available = new ArrayList<GameType>(gameTypes);
		available.addAll(_genericGameTypes);
		_currentGameType = available.get(new Random().nextInt(available.size()));
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
