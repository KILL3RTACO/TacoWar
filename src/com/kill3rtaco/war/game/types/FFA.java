package com.kill3rtaco.war.game.types;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import com.kill3rtaco.war.game.GameType;
import com.kill3rtaco.war.game.player.WarPlayer;

public class FFA extends GameType {

	public static String	ID	= "ffa";

	public FFA() {
		super();
		set(KEY_ID, ID);
		set(KEY_NAME, "Free for All");
		set(KEY_AUTHOR, "KILL3RTACO");
		set(KEY_TEAMS_ENABLED, false);
		_type = GameType.FFA;
	}

	public FFA(ConfigurationSection config) {
		this();
		_config = config;
	}

	@Override
	public boolean onKill() {
		return true;
	}

	@Override
	public void onMove(WarPlayer player, Location from, Location to) {
		return;
	}

}
