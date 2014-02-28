package com.kill3rtaco.war.game;

import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

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
	protected List<String>	_authors;
	
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
	}
	
	public GameType getBaseType() {
		return _baseType;
	}
	
}
