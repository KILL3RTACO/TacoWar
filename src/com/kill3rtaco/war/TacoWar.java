package com.kill3rtaco.war;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import com.kill3rtaco.tacoapi.api.TacoPlugin;
import com.kill3rtaco.tacoapi.obj.ChatObject;
import com.kill3rtaco.war.game.Game;
import com.kill3rtaco.war.game.GameType;
import com.kill3rtaco.war.game.TacoWarConfig;
import com.kill3rtaco.war.game.map.Playlist;
import com.kill3rtaco.war.game.map.WarMap;
import com.kill3rtaco.war.game.player.WarKit;
import com.kill3rtaco.war.game.player.Weapon;
import com.kill3rtaco.war.game.types.FFA;
import com.kill3rtaco.war.game.types.KOTH;
import com.kill3rtaco.war.game.types.TDM;

public class TacoWar extends TacoPlugin {

	public static TacoWar			plugin;
	public static ChatObject		chat;
	public static TacoWarConfig		config;
	private static Game				game;
	private static List<GameType>	_gametypes;
	private static List<WarMap>		_maps;
	private static List<Weapon>		_weapons;
	private static List<WarKit>		_kits;
	private static List<Playlist>	_playlists;

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
	}

	@Override
	public void onStop() {

	}

	public static void reloadGameTypes() {
		_gametypes = new ArrayList<GameType>();
		chat.out("[GameTypes] Reloading GameTypes...");
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
		chat.out("[Kits] Reloading Maps...");
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
		chat.out("[Maps] Reloading Maps...");
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
		return getIdentifyable(_weapons, id);
	}

	public static WarKit getKit(String id) {
		return getIdentifyable(_kits, id);
	}

	public static Playlist getPlaylist(String id) {
		return getIdentifyable(_playlists, id);
	}

	public static <T extends Identifyable> T getIdentifyable(List<T> list, String id) {
		for (T t : list) {
			if (t.getId().equals(id)) {
				return t;
			}
		}
		return null;
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
		return game;
	}

	public static int getNearestDegree(double degree, double factor) {
		return (int) (Math.round(degree / factor) * factor);
	}

}
