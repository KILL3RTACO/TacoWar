package com.kill3rtaco.war.game;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import com.kill3rtaco.tacoapi.api.serialization.SingleItemSerialization;
import com.kill3rtaco.war.TWDefaults;
import com.kill3rtaco.war.ValidatedConfig;

/*
 * abstract for there will be a class for every GameType
 * this class defines the basic config options that every gametype will have,
 * such as id, name and authors
*/
public abstract class GameTypeOptions extends ValidatedConfig {
	
	protected String		_id, _name;
	protected GameType		_baseType;
	protected int			_maxScore, _timeLimit;
	protected ItemStack		_wPrimary, _wSecondary;
	protected int			_ammoCount		= 0;
	protected List<String>	_authors;
	protected boolean		_teamsEnabled	= true;
	
	public GameTypeOptions(YamlConfiguration config) {
		super(config);
		_id = getString("id", true);
		_name = getString("name", true);
		_authors = _config.getStringList("authors");
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
		setWeapon(true, "primary_weapon", TWDefaults.PRIMARY_WEAPON);
		setWeapon(false, "primary_weapon", TWDefaults.PRIMARY_WEAPON);
		if(_wPrimary.getType() == Material.BOW || _wSecondary.getType() == Material.BOW) {
			_ammoCount = config.getInt("ammo_count", 0);
			if(_ammoCount <= 0) {
				_ammoCount = 64 * 2;
			}
		}
		_teamsEnabled = _config.getBoolean("teams_enabled", true);
	}
	
	protected void setWeapon(boolean primary, String path, ItemStack def) {
		ItemStack cItem = SingleItemSerialization.getItem(_config.getString(path, ""));
		if(primary) {
			_wPrimary = cItem != null ? cItem : def;
		} else {
			_wSecondary = cItem != null ? cItem : def;
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
	
}
