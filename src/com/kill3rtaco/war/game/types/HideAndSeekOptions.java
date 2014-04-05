package com.kill3rtaco.war.game.types;

import org.bukkit.Color;
import org.bukkit.configuration.file.YamlConfiguration;

import com.kill3rtaco.war.game.GameTypeOptions;
import com.kill3rtaco.war.game.Kit;
import com.kill3rtaco.war.game.player.TeamColor;

public class HideAndSeekOptions extends GameTypeOptions {
	
	private Color	_seekerArmorColor;
	private Kit		_seekerKit;
	private int		_seekerSpeed;		//percentage, overrides player_speed
	private int		_seekerWaitTime;	//seconds
										
	public HideAndSeekOptions(YamlConfiguration config) {
		super(config);
		_teamsEnabled = true;
		_kits = null;
		_seekerArmorColor = overrideColor("seeker_armor_color", TeamColor.BLACK.getArmorColor());
		_seekerKit = kit("seeker_kit");
		_seekerSpeed = overrideInt("seeker_speed", 125);
		_seekerWaitTime = overrideInt("seeker_wait_time", 30); //30 seconds
	}
	
	public Color seekerArmorColor() {
		return _seekerArmorColor;
	}
	
	public Kit seekerKit() {
		return _seekerKit;
	}
	
	public int seekerSpeed() {
		return _seekerSpeed;
	}
	
	public int seekerWaitTime() {
		return _seekerWaitTime;
	}
	
}
