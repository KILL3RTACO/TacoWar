package com.kill3rtaco.war;

import static com.kill3rtaco.war.TWConstants.*;

import java.io.File;

import org.bukkit.World;

import com.kill3rtaco.tacoapi.api.TacoConfig;
import com.kill3rtaco.war.game.Playlist;

public class TacoWarConfig extends TacoConfig {
	
	private Playlist	_currentPlaylist;
	
	public TacoWarConfig(File file) {
		super(file);
	}
	
	@Override
	protected void setDefaults() {
		addDefaultValue(C_WAR_WORLD, "world");
		addDefaultValue(C_TIME_LIMIT, 10); //minutes
		addDefaultValue(C_IDLE_TIME_LIMIT, 5); //minutes
		addDefaultValue(C_LOBBY_WAIT_TIME, 30); //seconds
		addDefaultValue(C_NEXT_GAME_WAIT_TIME, 15); //seconds
		addDefaultValue(C_POST_GAME_WAIT_TIME, 30); //seconds
		addDefaultValue(C_FF_ON, false);
		addDefaultValue(C_FF_PEN, 1);
		addDefaultValue(C_SUICIDE_PEN, 0);
		addDefaultValue(C_CURRENT_PLAYLIST, "");
		save();
	}
	
	public void setCurrentPlaylist(Playlist pl) {
		
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
	
	public Playlist getCurrentPlaylist() {
		return null;
	}
	
}
