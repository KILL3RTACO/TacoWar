package com.kill3rtaco.war.game.types;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import com.kill3rtaco.tacoapi.TacoAPI;
import com.kill3rtaco.war.game.GameType;
import com.kill3rtaco.war.game.player.WarPlayer;

public class GameTypeTDM extends GameType {
	
	public static final String	ID		= "koth";
	public static final String	NAME	= "Team Deathmatch";
	
	public GameTypeTDM() {
		super();
		set(KEY_ID, ID);
		set(KEY_NAME, NAME);
		set(KEY_AUTHOR, "KILL3RTACO");
		_type = GameType.TDM;
	}
	
	public GameTypeTDM(ConfigurationSection config) {
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
	
	@Override
	public String getObjectiveMessage() {
		String[] messages = new String[]{
				"You're all in this together, so stay together.",
				"Teamwork + Murder = Victory"};
		return TacoAPI.getChatUtils().getRandomElement(new ArrayList<String>(Arrays.asList(messages)));
	}
	
}
