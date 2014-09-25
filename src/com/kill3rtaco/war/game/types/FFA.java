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
	}

	public FFA(ConfigurationSection config) {
		super(config);
	}

	@Override
	public boolean onKill() {
		return true;
	}

	@Override
	public void onMove(WarPlayer player, Location from, Location to) {
		return;
	}

	@Override
	public int getType() {
		return GameType.FFA;
	}

}
