package com.kill3rtaco.war;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import com.kill3rtaco.tacoapi.api.TacoConfig;
import com.kill3rtaco.war.util.MapUtil;

public class TacoWarConfig extends TacoConfig {

	public static final String	KEY_DAMAGE_EXPIRE_THRESHOLD	= "damage_expire_threshold";	//seconds
	public static final String	KEY_MIN_PLAYERS				= "min_players";
	public static final String	KEY_TIME_BEFORE_GAME		= "time_before_game";			//seconds
	public static final String	KEY_WAR_WORLD				= "war_world";
	public static final String	KEY_WORLD_LOBBY				= "world_lobby";				//location ("x y z")

	public static final double	DEF_DAMAGE_EXPIRE_THRESHOLD	= 4D;
	public static final int		DEF_MIN_PLAYERS				= 8;
	public static final int		DEF_TIME_BEFORE_GAME		= 30;
	public static final String	DEF_WAR_WORLD				= "world";

	public TacoWarConfig(File file) {
		super(file);
	}

	@Override
	protected void setDefaults() {
		addDefaultValue(KEY_DAMAGE_EXPIRE_THRESHOLD, DEF_DAMAGE_EXPIRE_THRESHOLD);
		addDefaultValue(KEY_MIN_PLAYERS, DEF_MIN_PLAYERS);
		addDefaultValue(KEY_TIME_BEFORE_GAME, DEF_TIME_BEFORE_GAME);
		addDefaultValue(KEY_WAR_WORLD, DEF_WAR_WORLD);
	}

	public World getWarWorld() {
		return Bukkit.getWorld(getString(KEY_WAR_WORLD));
	}

	public Location getWorldLobby() {
		return MapUtil.getLocation(KEY_WORLD_LOBBY);
	}

	public long getDamageExpireThresholdMillis() {
		int second = 1000;
		return (long) (second * getDouble(KEY_DAMAGE_EXPIRE_THRESHOLD));
	}

	public int getMinPlayers() {
		return getInt(KEY_MIN_PLAYERS);
	}

	public int getTimeBeforeGame() {
		return getInt(KEY_TIME_BEFORE_GAME);
	}

}
