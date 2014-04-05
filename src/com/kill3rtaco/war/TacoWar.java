package com.kill3rtaco.war;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import com.kill3rtaco.tacoapi.api.TacoPlugin;
import com.kill3rtaco.tacoapi.api.ncommands.CommandManager;
import com.kill3rtaco.war.commands.GameCommands;
import com.kill3rtaco.war.commands.MapCreationCommands;
import com.kill3rtaco.war.game.Game;
import com.kill3rtaco.war.game.GameType;
import com.kill3rtaco.war.game.GameTypeOptions;
import com.kill3rtaco.war.game.Kit;
import com.kill3rtaco.war.game.Playlist;
import com.kill3rtaco.war.game.map.Map;
import com.kill3rtaco.war.game.types.FFAOptions;
import com.kill3rtaco.war.game.types.HideAndSeekOptions;
import com.kill3rtaco.war.game.types.InfectionOptions;
import com.kill3rtaco.war.game.types.JuggernautOptions;
import com.kill3rtaco.war.game.types.KOTHOptions;
import com.kill3rtaco.war.game.types.TDMOptions;
import com.kill3rtaco.war.listener.GameListener;

public class TacoWar extends TacoPlugin {
	
	public static TacoWar			plugin;
	public static TacoWarConfig		config;
	private List<Map>				_maps, _experimental;
	private List<Playlist>			_playlists;
	private List<GameTypeOptions>	_gameTypes;
	private List<Kit>				_kits;
	private Game					_currentGame;
	private boolean					_automate	= true;
	private CommandManager			_commands;
	
	@Override
	public void onStart() {
		plugin = this;
		_commands = new CommandManager(plugin);
		_commands.reg(GameCommands.class);
		_commands.reg(MapCreationCommands.class);
		config = new TacoWarConfig(new File(getDataFolder() + "/config.yml"));
		_experimental = new ArrayList<Map>();
		reloadKits();
		reloadGameTypes(); //gametypes are need kits to load properly
		reloadMaps(); //maps require gametypes to load properly
		reloadPlaylists(); //playlists needs maps and gametypes to load
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
	
	public void reloadKits() {
		_kits = new ArrayList<Kit>();
		File kitDir = new File(getDataFolder(), "kits/");
		if(!kitDir.isDirectory()) {
			return;
		}
		for(File f : kitDir.listFiles()) {
			YamlConfiguration kitConfig = YamlConfiguration.loadConfiguration(f);
			Kit k = new Kit(kitConfig);
			if(k.isValid()) {
				_kits.add(k);
			}
		}
	}
	
	public Kit getKit(String id) {
		for(Kit k : _kits) {
			if(k.getId().equals(id)) {
				return k;
			}
		}
		return null;
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
		File playlists = new File(getDataFolder(), "playlists");
		if(playlists.exists() && playlists.isDirectory()) {
			for(File f : playlists.listFiles()) {
				Playlist pl = new Playlist(f);
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
	
	public static int getNearestDegree(double degree, double factor) {
		return (int) (Math.round(degree / factor) * factor);
	}
	
	public void reloadGameTypes() {
		//add defaults
		File gtDir = new File(getDataFolder(), "gametypes");
		if(gtDir.isFile()) {
			return;
		}
		for(File f : gtDir.listFiles()) {
			if(f.isDirectory()) {
				continue;
			}
			YamlConfiguration config = YamlConfiguration.loadConfiguration(f);
			if(!config.isString("base_type")) {
				return;
			}
			GameType gt = GameType.getGameType(config.getString("base_type"));
			GameTypeOptions options;
			if(gt == GameType.FFA) {
				options = new FFAOptions(config);
			} else if(gt == GameType.HIDE_AND_SEEK) {
				options = new HideAndSeekOptions(config);
			} else if(gt == GameType.INFECTION) {
				options = new InfectionOptions(config);
			} else if(gt == GameType.JUGGERNAUT) {
				options = new JuggernautOptions(config);
			} else if(gt == GameType.KOTH) {
				options = new KOTHOptions(config);
			} else if(gt == GameType.TDM) {
				options = new TDMOptions(config);
			} else {
				continue;
			}
			if(options.isValid()) {
				_gameTypes.add(options);
			}
		}
	}
	
}
