package com.kill3rtaco.war;

import java.io.File;

import org.bukkit.World;

import com.kill3rtaco.tacoapi.api.TacoConfig;

public class TacoWarConfig extends TacoConfig {
	
	public TacoWarConfig(File file) {
		super(file);
	}
	
	@Override
	protected void setDefaults() {
		addDefaultValue("war_world", "world");
		addDefaultValue("time_limit", 10); //minutes
		addDefaultValue("idle_time_limit", 5); //minutes
		addDefaultValue("lobby_wait_time", 30); //seconds
		addDefaultValue("next_game_wait", 15); //seconds
		addDefaultValue("post_game_wait_time", 30); //seconds
		addDefaultValue("friendly_fire.enabled", false);
		addDefaultValue("friendly_fire.penalty", 1);
		addDefaultValue("suicide_penalty", 0);
		save();
	}
	
	public World getWarWorld() {
		return TacoWar.plugin.getServer().getWorld(getString("war_world"));
	}
	
	public int getTimeLimit() {
		return getInt("time_limit");
	}
	
	public int getIdleTimeLimit() {
		return getInt("idle_time_limit");
	}
	
	public int getLobbyWaitTime() {
		return getInt("lobby_wait_time");
	}
	
	public boolean friendlyFireEnabled() {
		return getBoolean("friendly_fire.enabled");
	}
	
	public int friendlyFirePenalty() {
		return getInt("friendly_fire.penalty");
	}
	
	public int suicidePenalty() {
		return getInt("suicide_penalty");
	}
	
	public int getNextGameWait() {
		return getInt("next_game_wait");
	}
	
}
