package com.kill3rtaco.war.game.types;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import com.kill3rtaco.war.game.GameType;
import com.kill3rtaco.war.game.player.WarPlayer;

public class TDM extends GameType {

	public static final String	ID	= "koth";

	public TDM() {
		super();
		set(KEY_ID, ID);
		set(KEY_NAME, "Team Deathmatch");
		set(KEY_AUTHOR, "KILL3RTACO");
		_type = GameType.TDM;
	}

	public TDM(ConfigurationSection config) {
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

	@Override
	public int getType() {
		return GameType.TDM;
	}

}
