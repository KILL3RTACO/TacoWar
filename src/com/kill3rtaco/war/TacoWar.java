package com.kill3rtaco.war;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import com.kill3rtaco.tacoapi.api.TacoPlugin;
import com.kill3rtaco.tacoapi.api.ncommands.CommandManager;
import com.kill3rtaco.tacoapi.obj.ChatObject;
import com.kill3rtaco.war.commands.MapCreationCommands;
import com.kill3rtaco.war.commands.WarCommands;
import com.kill3rtaco.war.game.Game;
import com.kill3rtaco.war.game.GameType;
import com.kill3rtaco.war.game.map.Playlist;
import com.kill3rtaco.war.game.map.WarMap;
import com.kill3rtaco.war.game.map.playlist.PlaylistDefault;
import com.kill3rtaco.war.game.player.WarKit;
import com.kill3rtaco.war.game.player.Weapon;
import com.kill3rtaco.war.game.player.kit.InternalKit;
import com.kill3rtaco.war.game.player.weapon.InternalWeapon;
import com.kill3rtaco.war.game.types.FFA;
import com.kill3rtaco.war.game.types.KOTH;
import com.kill3rtaco.war.game.types.TDM;
import com.kill3rtaco.war.util.Identifyable;

public class TacoWar extends TacoPlugin {
	
	public static TacoWar			plugin;
	public static ChatObject		chat;
	public static TacoWarConfig		config;
	private static Game				_game;
	private static List<GameType>	_gametypes;
	private static List<WarMap>		_maps;
	private static List<Weapon>		_weapons;
	private static List<WarKit>		_kits;
	private static List<Playlist>	_playlists;
	private CommandManager			_commands	= new CommandManager(this);
	
	@Override
	public void onStart() {
		plugin = this;
		chat = new ChatObject("&9TacoWar");
		config = new TacoWarConfig(new File(TW.DATA_FOLDER + "/config.yml"));
		reloadWeapons();
		reloadKits(); //relies on weapons
		reloadGameTypes(); //relies on kits somewhat (FORCE_KIT)
		reloadMaps();
		reloadPlaylists(); //relies on maps and gametypes
		_commands.reg(WarCommands.class);
		_commands.reg(MapCreationCommands.class);
	}
	
	@Override
	public void onStop() {
		
	}
	
	public static void reloadGameTypes() {
		_gametypes = new ArrayList<GameType>();
		chat.out("[GameTypes] Reloading GameTypes...");
		_gametypes.add(GameType.freeForAll);
		_gametypes.add(GameType.kingOfTheHill);
		_gametypes.add(GameType.teamDeathmatch);
		List<ConfigurationSection> configs = getConfigsInDirectory(TW.GT_FOLDER);
		for (ConfigurationSection c : configs) {
			String base = c.getString(GameType.KEY_BASE_TYPE);
			if (base == null)
				continue;
			
			GameType gt;
			if (base.equals(FFA.ID))
				gt = new FFA(c);
			else if (base.equals(KOTH.ID))
				gt = new KOTH(c);
			else if (base.equals(TDM.ID))
				gt = new TDM(c);
			else
				continue;
			
			if (!gt.isValid())
				continue;
			
			_gametypes.add(gt);
		}
		chat.out("[GameTypes] " + _gametypes.size() + " GameTypes loaded");
	}
	
	public static void reloadMaps() {
		_maps = new ArrayList<WarMap>();
		chat.out("[Maps] Reloading Maps...");
		List<ConfigurationSection> configs = getConfigsInDirectory(TW.MAPS_FOLDER);
		for (ConfigurationSection c : configs) {
			WarMap m = new WarMap(c);
			if (!m.isValid())
				continue;
			
			_maps.add(m);
		}
		chat.out("[Maps] " + _maps.size() + " Maps loaded");
	}
	
	public static void reloadWeapons() {
		_weapons = new ArrayList<Weapon>();
		chat.out("[Weapons] Reloading Weapons...");
		_weapons.add(InternalWeapon.BASEBALL_BAT);
		_weapons.add(InternalWeapon.CRESCENT_ROSE);
		_weapons.add(InternalWeapon.EXPLOSIVE_AXE);
		_weapons.add(InternalWeapon.FISHBONES);
		_weapons.add(InternalWeapon.HOT_FORK);
		_weapons.add(InternalWeapon.LEGOLAS_BOW);
		_weapons.add(InternalWeapon.MAGIC_WAND);
		_weapons.add(InternalWeapon.MJOLNIR);
		_weapons.add(InternalWeapon.PLAN_G);
		_weapons.add(InternalWeapon.THE_LIE);
		List<ConfigurationSection> configs = getConfigsInDirectory(TW.WEAPONS_FOLDER);
		for (ConfigurationSection c : configs) {
			Weapon w = new Weapon(c);
			if (!w.isValid())
				continue;
			
			_weapons.add(w);
		}
		chat.out("[Weapons] " + _weapons.size() + " Weapons loaded");
	}
	
	public static void reloadKits() {
		_kits = new ArrayList<WarKit>();
		chat.out("[Kits] Reloading Kits...");
		_kits.add(InternalKit.DEFAULT);
		_kits.add(InternalKit.HEXPLOSIVES);
		_kits.add(InternalKit.ROCKETS);
		_kits.add(InternalKit.SILLY);
		List<ConfigurationSection> configs = getConfigsInDirectory(TW.KITS_FOLDER);
		for (ConfigurationSection c : configs) {
			WarKit k = new WarKit(c);
			if (!k.isValid())
				continue;
			
			_kits.add(k);
		}
		chat.out("[Kits] " + _maps.size() + " Kits loaded");
	}
	
	public static void reloadPlaylists() {
		_playlists = new ArrayList<Playlist>();
		chat.out("[Maps] Reloading Playlists...");
		_playlists.add(new PlaylistDefault());
		List<ConfigurationSection> configs = getConfigsInDirectory(TW.PL_FOLDER);
		for (ConfigurationSection c : configs) {
			Playlist pl = new Playlist(c);
			if (!pl.isValid())
				continue;
			
			_playlists.add(pl);
		}
		chat.out("[Playlist] " + _maps.size() + " Playlists loaded");
	}
	
	public static GameType getGameType(String id) {
		return getIdentifyable(_gametypes, id);
	}
	
	public static WarMap getMap(String id) {
		return getIdentifyable(_maps, id);
	}
	
	public static Weapon getWeapon(String id) {
		return getWeapon(id, false);
	}
	
	public static Weapon getWeapon(String id, boolean clone) {
		return TW.cloneOrNot(getIdentifyable(_weapons, id), clone);
	}
	
	public static WarKit getKit(String id) {
		return getIdentifyable(_kits, id);
	}
	
	public static WarKit getKit(String id, boolean clone) {
		return TW.cloneOrNot(getIdentifyable(_kits, id), clone);
	}
	
	public static Playlist getPlaylist(String id) {
		return getIdentifyable(_playlists, id);
	}
	
	public static List<GameType> getGameTypes() {
		return _gametypes;
	}
	
	public static List<WarMap> getMaps() {
		return _maps;
	}
	
	public static List<Weapon> getWeapons() {
		return _weapons;
	}
	
	public static List<WarKit> getKits() {
		return _kits;
	}
	
	public static List<Playlist> getPlaylists() {
		return _playlists;
	}
	
	public static List<GameType> getGameTypes(List<String> ids) {
		return getIdentifyableList(_gametypes, ids);
	}
	
	public static List<GameType> getGameTypesAndExclude(List<String> exclude) {
		return getIdentifyableListAndExclude(_gametypes, exclude);
	}
	
	public static List<WarMap> getMaps(List<String> ids) {
		return getIdentifyableList(_maps, ids);
	}
	
	public static List<WarMap> getMapsAndExclude(List<String> exclude) {
		return getIdentifyableListAndExclude(_maps, exclude);
	}
	
	public static List<Weapon> getWeapons(List<String> ids) {
		return getWeapons(ids, false);
	}
	
	public static List<Weapon> getWeapons(List<String> ids, boolean clone) {
		return TW.cloneOrNotList(getIdentifyableList(_weapons, ids), clone);
	}
	
	public static List<Weapon> getWeaponsAndExclude(List<String> exclude) {
		return getWeaponsAndExclude(exclude, false);
	}
	
	public static List<Weapon> getWeaponsAndExclude(List<String> exclude, boolean clone) {
		return TW.cloneOrNotList(getIdentifyableListAndExclude(_weapons, exclude), clone);
	}
	
	public static List<WarKit> getKits(List<String> ids) {
		return getIdentifyableList(_kits, ids);
	}
	
	public static List<WarKit> getKitsAndExclude(List<String> exclude) {
		return getIdentifyableListAndExclude(_kits, exclude);
	}
	
	public static List<Playlist> getPlaylists(List<String> ids) {
		return getIdentifyableList(_playlists, ids);
	}
	
	public static List<Playlist> getPlaylistsAndExclude(List<String> exclude) {
		return getIdentifyableListAndExclude(_playlists, exclude);
	}
	
	public static <T extends Identifyable> T getIdentifyable(List<T> list, String id) {
		for (T t : list) {
			if (t.getId().equals(id)) {
				return t;
			}
		}
		return null;
	}
	
	public static <T extends Identifyable> List<T> getIdentifyableList(List<T> list, List<String> ids) {
		List<T> l = new ArrayList<T>();
		for (String s : ids) {
			T identifyable = getIdentifyable(list, s);
			if (identifyable != null)
				l.add(identifyable);
		}
		return l;
	}
	
	public static <T extends Identifyable> List<T> getIdentifyableListAndExclude(List<T> list, List<String> exclude) {
		if (exclude == null || exclude.isEmpty())
			return list;
		List<T> l = new ArrayList<T>();
		for (T t : l) { //well, this looks weird
			if (!exclude.contains(t.getId()))
				l.add(t);
		}
		return l;
	}
	
	private static List<ConfigurationSection> getConfigsInDirectory(File dir) {
		FilenameFilter filter = new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				return name.matches(".+\\.yml"); //*.yml
			}
			
		};
		List<ConfigurationSection> configs = new ArrayList<ConfigurationSection>();
		for (File f : dir.listFiles(filter)) {
			configs.add(YamlConfiguration.loadConfiguration(f));
		}
		return configs;
	}
	
	public static Game currentGame() {
		return _game;
	}
	
	public static void startNewGame() {
		_game = new Game();
	}
	
}
