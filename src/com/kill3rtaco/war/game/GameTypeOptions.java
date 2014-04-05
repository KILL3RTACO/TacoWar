package com.kill3rtaco.war.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Color;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import com.kill3rtaco.tacoapi.api.serialization.SingleItemSerialization;
import com.kill3rtaco.war.TWDefaults;
import com.kill3rtaco.war.TacoWar;
import com.kill3rtaco.war.ValidatedConfig;
import com.kill3rtaco.war.game.player.TeamColor;

/*
 * abstract for there will be a class for every GameType
 * this class defines the basic config options that every gametype will have,
 * such as id, name and authors
*/
public abstract class GameTypeOptions extends ValidatedConfig {
	
	protected String		_id, _name;
	protected GameType		_baseType;
	protected Color			_playerArmorColor;
	protected int			_maxScore, _timeLimit;	//minutes
	protected int			_ammoCount;
	protected List<String>	_authors;
	protected List<Kit>		_kits;
	protected boolean		_teamsEnabled;
	protected int			_playerSpeed;			//percentage
	protected int			_maxHealth;			//half-hearts
	protected int			_friendlyFirePenalty;
	protected int			_suicidePenalty;
	
	public GameTypeOptions(YamlConfiguration config) {
		super(config);
		_id = getString("id", true);
		_name = getString("name", true);
		_authors = _config.getStringList("authors");
		_playerArmorColor = overrideColor("player_armor_color", TeamColor.RED.getArmorColor());
		if(_authors.isEmpty())
			_authors.add("Unknown");
		String baseType = getString("base_type", true);
		if(baseType != null) {
			_baseType = GameType.getGameType(baseType);
			if(_baseType == null)
				_baseType = GameType.TDM;
		}
		_kits = getKitList("kits");
		_maxScore = _config.getInt("max_score", 0); //< 1 = game determines score based on players
		_timeLimit = config.getInt("time_limit", 0); // < 1 = default used
		_teamsEnabled = _config.getBoolean("teams_enabled", true);
		_playerSpeed = _config.getInt("player_speed", 100);
		if(_playerSpeed < TWDefaults.MIN_PLAYER_SPEED) {
			_playerSpeed = TWDefaults.MIN_PLAYER_SPEED;
		}
		if(_playerSpeed > TWDefaults.MAX_PLAYER_SPEED) {
			_playerSpeed = TWDefaults.MAX_PLAYER_SPEED;
		}
		_maxHealth = _config.getInt("max_health", 20);
		if(_maxHealth <= 0) {
			_maxHealth = 20;
		}
		_friendlyFirePenalty = overrideInt("friendly_fire_penalty", 0); // <0 -> off, 0-> on; no penalty, >0 -> on w/penalty
		_suicidePenalty = overrideInt("suicide_penalty", 0);
	}
	
	protected ItemStack overrideItemStack(String path, ItemStack override) {
		ItemStack cItem = SingleItemSerialization.getItem(_config.getString(path, ""));
		if(cItem != null) {
			return cItem;
		} else {
			return override;
		}
	}
	
	protected int overrideInt(String path, int override) {
		if(_config.isInt(path)) {
			return _config.getInt(path);
		} else {
			return override;
		}
	}
	
	protected Color overrideColor(String path, Color override) {
		TeamColor tc = TeamColor.getTeamColor(_config.getString(path, "red"));
		if(tc != null) {
			return tc.getArmorColor();
		} else {
			return override;
		}
	}
	
	public GameType baseType() {
		return _baseType;
	}
	
	public String id() {
		return _id;
	}
	
	public String name() {
		return _name;
	}
	
	public int maxScore() {
		return _maxScore;
	}
	
	public int timeLimit() {
		return _timeLimit;
	}
	
	public long timeLimitTicks() {
		return _timeLimit * 60 * 20L; //time limit is in minutes
	}
	
	public List<Kit> getKitList(String path) {
		List<Kit> kits = new ArrayList<Kit>();
		List<String> kitIds = _config.getStringList(path);
		for(String s : kitIds) {
			Kit k = TacoWar.plugin.getKit(s);
			if(k != null) {
				kits.add(k);
			}
		}
		return kits;
	}
	
	public float toSpeedFloat(int speedPercentage) {
		return 0.1F * (speedPercentage / 100F); //0.1 is default speed
	}
	
	public boolean friendlyFireEnabled() {
		return _friendlyFirePenalty >= 0;
	}
	
	public int friendlyFirePenalty() {
		return _friendlyFirePenalty;
	}
	
	public int suicidePenalty() {
		return Math.abs(_suicidePenalty);
	}
	
	public Kit randomKit(List<Kit> kits) {
		if(kits == null || kits.isEmpty()) {
			return null;
		}
		return kits.get(new Random().nextInt(kits.size()));
	}
	
	protected Kit kit(String path) {
		return TacoWar.plugin.getKit(_config.getString(path));
	}
	
}
