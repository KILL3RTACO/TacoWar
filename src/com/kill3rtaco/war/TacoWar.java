package com.kill3rtaco.war;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.scheduler.BukkitRunnable;

import com.kill3rtaco.tacoapi.api.TacoPlugin;
import com.kill3rtaco.war.game.Game;
import com.kill3rtaco.war.game.Map;

public class TacoWar extends TacoPlugin {
	
	public static TacoWar		plugin;
	public static TacoWarConfig	config;
	private List<Map>			_maps, _experimental;
	private Game				_currentGame;
	private boolean				_automate	= true;
	
	@Override
	public void onStart() {
		plugin = this;
		config = new TacoWarConfig(new File(getDataFolder() + "/config.yml"));
		_experimental = new ArrayList<Map>();
		reloadMaps();
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
			if(m.isValid())
				_maps.add(m);
			else
				chat.out("Map '" + f.getName() + "/map.yml' is invalid. Please ensure all necessary options are included");
		}
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
	
	public void addExperimentalMap(Map map) {
		_experimental.add(map);
	}
	
	public Map getExperimetalMap(String id) {
		for(Map m : _experimental) {
			if(m.getId().equals(id)) {
				return m;
			}
		}
		return null;
	}
	
}
