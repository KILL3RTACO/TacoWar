package com.kill3rtaco.war.game.map;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.configuration.ConfigurationSection;

import com.kill3rtaco.war.TacoWar;
import com.kill3rtaco.war.game.GameType;
import com.kill3rtaco.war.game.player.kit.WarKit;
import com.kill3rtaco.war.util.Identifyable;
import com.kill3rtaco.war.util.ValidatedConfig;

public class Playlist extends ValidatedConfig implements Identifyable {
	
	public static final String		KEY_ID			= "id";
	public static final String		KEY_MAPS		= "maps";
	public static final String		KEY_KITS		= "kits";
	public static final String		KEY_GAMETYPES	= "gametypes";
	
	protected List<PlaylistEntry>	_maps;
	protected List<GameType>		_genericGameTypes;
	protected List<WarKit>			_genericKits;
	private WarMap					_currentMap;
	private GameType				_currentGameType;
	private WarKit					_currentKit;
	private File					_file;
	
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
		
		//prevent removing first element from actual list in config
		_genericGameTypes = getGameTypesFromList(new ArrayList<String>(getStringList(KEY_GAMETYPES, false)));
		_genericKits = getKitsFromList(new ArrayList<String>(getStringList(KEY_KITS, false)));
		
		_maps = new ArrayList<PlaylistEntry>();
		List<String> list = new ArrayList<String>(_config.getConfigurationSection(KEY_MAPS).getKeys(false));
		for (String s : list) {
			WarMap map = TacoWar.getMap(s);
			if (map == null)
				continue;
			PlaylistEntry entry = new PlaylistEntry(map);
			
			List<String> mapIds = getStringList(KEY_MAPS + "." + s + "." + KEY_GAMETYPES, false);
			List<GameType> gametypes = TacoWar.getGameTypes(mapIds);
			if (gametypes.isEmpty())
				continue;
			entry.getGameTypes().addAll(gametypes);
			
			mapIds = getStringList(KEY_MAPS + "." + s + "." + KEY_KITS, false);
			List<WarKit> kits = TacoWar.getKits(mapIds);
			if (gametypes.isEmpty())
				continue;
			entry.getKits().addAll(kits);
			
			if (map == null || !map.isReady() || getGameTypesFor(map.getId()).isEmpty() || getKitsFor(map.getId()).isEmpty())
				continue;
			
			_maps.add(entry);
		}
		
	}
	
	public void setCurrentMap(WarMap map) {
		_currentMap = map;
	}
	
	private List<GameType> getGameTypesFromList(List<String> ids) {
		if (ids.size() == 1 && ids.get(0).equalsIgnoreCase("none")) {
			return _genericGameTypes = new ArrayList<GameType>();
		} else if (ids.size() > 1 && ids.get(0).equalsIgnoreCase("all")) {
			ids.remove(0);
			return TacoWar.getGameTypesAndExclude(ids);
		} else {
			return _genericGameTypes = TacoWar.getGameTypes(ids);
		}
	}
	
	private List<WarKit> getKitsFromList(List<String> ids) {
		if (ids.size() == 1 && ids.get(0).equalsIgnoreCase("none")) {
			return new ArrayList<WarKit>();
		} else if (ids.size() > 1 && ids.get(0).equalsIgnoreCase("all")) {
			ids.remove(0);
			return TacoWar.getKitsAndExclude(ids);
		} else {
			return TacoWar.getKits(ids);
		}
	}
	
	public String getId() {
		return _config.getString(KEY_ID);
	}
	
	public boolean isEmpty() {
		return _maps.isEmpty();
	}
	
	public GameType selectGameType() {
//		System.out.println("[DEBUG] -> _currentMap == null: " + (_currentMap == null));
		if (_currentMap == null)
			return null;
		List<GameType> gameTypes = new ArrayList<GameType>();
		for (PlaylistEntry e : _maps) {
			if (e.getMap() == _currentMap && !e.getGameTypes().isEmpty())
				gameTypes = e.getGameTypes();
		}
		List<GameType> types = new ArrayList<GameType>(gameTypes);
		types.addAll(_genericGameTypes);
		List<GameType> available = new ArrayList<GameType>();
		for (GameType gt : types) {
			if (_currentMap.gameTypeSupported(gt.getType()))
				available.add(gt);
		}
		if (available.isEmpty()) {
			_currentGameType = null;
			return null;
		}
		if (available.size() == 1) {
			_currentGameType = available.get(0);
			return _currentGameType;
		}
		_currentGameType = available.get(new Random().nextInt(available.size()));
		return _currentGameType;
	}
	
	public WarKit selectKit() {
		List<WarKit> kits = new ArrayList<WarKit>();
		for (PlaylistEntry e : _maps) {
			if (e.getMap() == _currentMap && !e.getKits().isEmpty())
				kits = e.getKits();
		}
//		System.out.println("[DEBUG] -> kits.size(): " + kits.size());
		List<WarKit> available = new ArrayList<WarKit>(kits);
		available.addAll(_genericKits);
//		System.out.println("[DEBUG] -> available.size(): " + available.size());
		if (available.isEmpty()) {
			_currentKit = null;
			return null;
		}
		if (available.size() == 1) {
			_currentKit = available.get(0);
			return _currentKit;
		}
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
	
	public List<WarMap> getMapsForPlayercount(int playercount) {
		List<WarMap> maps = new ArrayList<WarMap>();
		for (WarMap m : getMaps()) {
			if (m.canHoldPlayers(playercount))
				maps.add(m);
		}
		return maps;
	}
	
	public List<WarMap> getMaps(int amount, int playercount) {
		List<WarMap> maps = new ArrayList<WarMap>(getMapsForPlayercount(playercount));
		List<WarMap> avail = new ArrayList<WarMap>();
		for (int i = 0; i < amount; i++) {
			if (maps.isEmpty())
				return avail;
			avail.add(maps.remove(new Random().nextInt(maps.size())));
		}
		return avail;
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
