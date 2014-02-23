package com.kill3rtaco.war;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.scheduler.BukkitRunnable;

import com.kill3rtaco.tacoapi.api.TacoPlugin;
import com.kill3rtaco.tacoapi.api.ncommands.CommandManager;
import com.kill3rtaco.war.commands.GameCommands;
import com.kill3rtaco.war.commands.MapCreationCommands;
import com.kill3rtaco.war.game.Game;
import com.kill3rtaco.war.game.Playlist;
import com.kill3rtaco.war.game.map.Map;
import com.kill3rtaco.war.listener.GameListener;

public class TacoWar extends TacoPlugin {
	
	public static TacoWar		plugin;
	public static TacoWarConfig	config;
	private List<Map>			_maps, _experimental;
	private List<Playlist>		_playlists;
	private Game				_currentGame;
	private boolean				_automate	= true;
	private CommandManager		_commands;
	
	@Override
	public void onStart() {
		plugin = this;
		_commands = new CommandManager(plugin);
		_commands.reg(GameCommands.class);
		_commands.reg(MapCreationCommands.class);
		config = new TacoWarConfig(new File(getDataFolder() + "/config.yml"));
		_experimental = new ArrayList<Map>();
		reloadMaps();
		reloadPlaylists();
		registerEvents(new GameListener());
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if(!_automate) {
					return;
				}
				Game game = currentGame();
				if(game == null) {
					startNewGame();
				}
			}
			
		}.runTaskTimer(plugin, 0, 20L * 60);
	}
	
	@Override
	public void onStop() {
		stopAutomation();
		if(_currentGame != null) {
			_currentGame.end();
		}
	}
	
	public void stopAutomation() {
		_automate = false;
	}
	
	public void startAutomation() {
		_automate = true;
	}
	
	//whether new games are automatically created or not
	public boolean isAutomating() {
		return _automate;
	}
	
	public void reloadMaps() {
		_maps = new ArrayList<Map>();
		File mapsDir = new File(getDataFolder() + "/maps");
		if(!mapsDir.isDirectory()) {
			return;
		}
		for(File f : mapsDir.listFiles()) {
			if(!f.isDirectory()) {
				continue;
			}
			//File schema = new File(f + "/map.schematic");
			File config = new File(f + "/map.yml");
//			if(!schema.exists() || !config.exists()) {
//				continue;
//			}
			Map m = new Map(config);
			if(m.isValid()) {
				_maps.add(m);
			} else {
				chat.out("Map '" + f.getName() + "/map.yml' is invalid. Adding it to experimental maps...");
				_experimental.add(m);
			}
		}
	}
	
	public void reloadPlaylists() {
		_playlists = new ArrayList<Playlist>();
		_playlists.add(new Playlist("default"));
		File playlists = new File(getDataFolder() + "/playlists");
		if(playlists.exists() && playlists.isDirectory()) {
			for(File f : playlists.listFiles()) {
				String name = f.getName();
				String id = name.substring(0, name.lastIndexOf("."));
				Playlist pl = new Playlist(id, f);
				_playlists.add(pl);
			}
		}
	}
	
	public Playlist getPlaylist(String id) {
		for(Playlist p : _playlists) {
			if(p.getId().equals(id)) {
				return p;
			}
		}
		return null;
	}
	
	public void startNewGame() {
		if(config.getWarWorld() == null) {
			_currentGame = null;
			chat.out("Could not start a new game because the world given in the config does not exist");
			stopAutomation();
			chat.out("Game automation stopped");
			return;
		}
		if(_maps == null || _maps.size() < 1) {
			_currentGame = null;
			chat.out("Could not start a new game because there are no maps to play");
			stopAutomation();
			chat.out("Game automation stopped");
			return;
		}
		_currentGame = new Game();
	}
	
	public Game currentGame() {
		return _currentGame;
	}
	
	public List<Map> getMaps() {
		return _maps;
	}
	
	public Map getMap(String id) {
		List<Map> maps = TacoWar.plugin.getMaps();
		for(Map m : maps) {
			if(m.getId().equals(id)) {
				return m;
			}
		}
		return null;
	}
	
	public Map getRandomMap() {
		if(_maps.isEmpty()) {
			return null;
		}
		return _maps.get(new Random().nextInt(_maps.size()));
	}
	
	public void addExperimentalMap(Map map) {
		_experimental.add(map);
	}
	
	public Map getExperimentalMap(String id) {
		for(Map m : _experimental) {
			if(m.getId().equals(id)) {
				return m;
			}
		}
		return null;
	}
	
	public boolean experimentalMapExists(String id) {
		return getExperimentalMap(id) != null;
	}
	
}
