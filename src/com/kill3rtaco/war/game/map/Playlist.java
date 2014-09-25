package com.kill3rtaco.war.game.map;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import com.kill3rtaco.war.TW;
import com.kill3rtaco.war.TacoWar;
import com.kill3rtaco.war.game.GameType;
import com.kill3rtaco.war.game.player.WarKit;
import com.kill3rtaco.war.util.Identifyable;
import com.kill3rtaco.war.util.ValidatedConfig;

public class Playlist extends ValidatedConfig implements Identifyable {

	public static final String	KEY_ID			= "id";
	public static final String	KEY_MAPS		= "maps";
	public static final String	KEY_KITS		= "kits";
	public static final String	KEY_GAMETYPES	= "gametypes";

	private List<PlaylistEntry>	_maps;
	private List<GameType>		_genericGameTypes;
	private List<WarKit>		_genericKits;
	private WarMap				_currentMap;
	private GameType			_currentGameType;
	private WarKit				_currentKit;
	private File				_file;

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
	 *     gametypes:
	 *       - gametype_id 
	 *       - gametype_id 
	 *     kits:
	 *       - kit_id
	 *       - kit_id
	 * gametypes: 
	 *    - gametype_id 
	 *    - gametype_id
	 */
	public void reload() {
		if (_config == null || !_config.isConfigurationSection(KEY_MAPS))
			return;

		_maps = new ArrayList<PlaylistEntry>();
		List<String> list = new ArrayList<String>(_config.getConfigurationSection(KEY_MAPS).getKeys(false));
		for (String s : list) {
			WarMap map = TacoWar.getMap(s);
			if (map == null)
				continue;
			PlaylistEntry entry = new PlaylistEntry(map);

			List<String> ids = getStringList(KEY_MAPS + "." + s + "." + KEY_GAMETYPES, false);
			List<GameType> gametypes = TacoWar.getGameTypes(ids);
			if (gametypes.isEmpty())
				continue;
			entry.getGameTypes().addAll(gametypes);

			ids = getStringList(KEY_MAPS + "." + s + "." + KEY_KITS, false);
			List<WarKit> kits = TacoWar.getKits(ids);
			if (gametypes.isEmpty())
				continue;
			entry.getKits().addAll(kits);
		}

		List<String> ids = getStringList(KEY_GAMETYPES, false);
		if (ids == null || ids.isEmpty())
			return;
		_genericGameTypes = TacoWar.getGameTypes(ids);

		ids = getStringList(KEY_KITS, false);
		if (ids == null || ids.isEmpty())
			return;
		_genericKits = TacoWar.getKits(ids);
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
		List<GameType> gameTypes = _maps.get(_maps.indexOf(new PlaylistEntry(_currentMap))).getGameTypes();
		if (gameTypes == null || gameTypes.isEmpty()) {
			_currentGameType = null;
			return null;
		}
		List<GameType> available = new ArrayList<GameType>(gameTypes);
		available.addAll(_genericGameTypes);
		_currentGameType = available.get(new Random().nextInt(available.size()));
		return _currentGameType;
	}

	public WarKit selectKit() {
		if (_currentMap == null) {
			return null;
		}
		List<WarKit> kits = _maps.get(_maps.indexOf(new PlaylistEntry(_currentMap))).getKits();
		if (kits == null || kits.isEmpty()) {
			_currentKit = null;
			return null;
		}
		List<WarKit> available = new ArrayList<WarKit>(kits);
		available.addAll(_genericKits);
		_currentKit = available.get(new Random().nextInt(available.size()));
		return _currentKit;
	}

	public List<WarMap> getMaps() {
		List<WarMap> maps = new ArrayList<WarMap>();
		for (PlaylistEntry e : _maps) {
			maps.add(e.getMap());
		}
		return maps;
	}

	public List<GameType> getGameTypesFor(String mapId) {
		List<WarMap> maps = getMaps();
		for (int i = 0; i < maps.size(); i++) {
			if (maps.get(i).getId().equals(mapId)) {
				return _maps.get(i).getGameTypes();
			}
		}
		return null;
	}

	public List<WarKit> getKitsFor(String mapId) {
		List<WarMap> maps = getMaps();
		for (int i = 0; i < maps.size(); i++) {
			if (maps.get(i).getId().equals(mapId)) {
				return _maps.get(i).getKits();
			}
		}
		return null;
	}

	public void save() {
		save(_file);
	}

}
