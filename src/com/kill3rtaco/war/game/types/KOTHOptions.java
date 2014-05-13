package com.kill3rtaco.war.game.types;

import org.bukkit.configuration.file.YamlConfiguration;

import com.kill3rtaco.war.game.GameTypeOptions;

public class KOTHOptions extends GameTypeOptions {
	
	private boolean	_roamingHills;
	private int		_pointsPerSecond, _maxHills;
	
	public KOTHOptions(YamlConfiguration config) {
		super(config);
		_roamingHills = config.getBoolean("roaming_hills", false);
		_pointsPerSecond = overrideInt("points_per_second", 1);
		_maxHills = overrideInt("max_hills", -1); //<= 0 -> no limit
	}
	
	public boolean roamingHills() {
		return _roamingHills;
	}
	
	public int pointsPerSecond() {
		return _pointsPerSecond;
	}
	
	public int maxHills() {
		return _maxHills;
	}
	
	@Override
	public void registerTimers() {
	}
	
	@Override
	public boolean isKillBased() {
		return false;
	}
	
}