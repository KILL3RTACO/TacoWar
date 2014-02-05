package com.kill3rtaco.tacowar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

import com.kill3rtaco.tacoapi.api.TacoPlugin;
import com.kill3rtaco.tacowar.game.Game;
import com.kill3rtaco.tacowar.game.Map;

public class TacoWar extends TacoPlugin {
	
	public static TacoWar		plugin;
	public static TacoWarConfig	config;
	private List<Map>			_maps;
	private Game				_currentGame;
	
	@Override
	public void onStart() {
		plugin = this;
		config = new TacoWarConfig(new File(getDataFolder() + "/config.yml"));
		reloadMaps();
		registerEvents(new GameListener());
	}
	
	@Override
	public void onStop() {
		
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
			File schema = new File(f + "/map.schematic");
			File config = new File(f + "/map.yml");
			if(!schema.exists() || !config.exists()) {
				continue;
			}
			Map m = new Map(YamlConfiguration.loadConfiguration(config));
			if(m.isValid())
				_maps.add(m);
		}
	}
	
	public void startNewGame() {
		if(config.getWarWorld() == null) {
			chat.out("Could not start a new game because the world given in the config does not exist");
			return;
		}
		if(_maps == null || _maps.size() < 1) {
			chat.out("Could not start a new game because there are no maps to play");
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
	
}
