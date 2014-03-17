package com.kill3rtaco.war.game;

import java.util.List;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import com.kill3rtaco.tacoapi.api.serialization.SingleItemSerialization;
import com.kill3rtaco.war.TWDefaults;
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
	protected ItemStack		_wPrimary, _wSecondary;
	protected int			_ammoCount;
	protected List<String>	_authors;
	protected boolean		_teamsEnabled;
	protected int			_playerSpeed;			//percentage
	protected int			_maxHealth;			//half-hearts
													
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
		_maxScore = _config.getInt("max_score", 0); //< 1 = game determines score based on players
		_timeLimit = config.getInt("time_limit", 0); // < 1 = default used
		_wPrimary = overrideItemStack("primary_weapon", TWDefaults.PRIMARY_WEAPON);
		_wSecondary = overrideItemStack("secondary_weapon", TWDefaults.SECONDARY_WEAPON);
		if(_wPrimary.getType() == Material.BOW || _wSecondary.getType() == Material.BOW) {
			_ammoCount = config.getInt("ammo_count", 0);
			if(_ammoCount <= 0) {
				_ammoCount = 64 * 2;
			}
		}
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
	
	public ItemStack primaryWeapon() {
		return _wPrimary;
	}
	
	public ItemStack secondaryWeapon() {
		return _wSecondary;
	}
	
	public float toSpeedFloat(int speedPercentage) {
		return 0.1F * (speedPercentage / 100F); //0.1 is default speed
	}
	
}
