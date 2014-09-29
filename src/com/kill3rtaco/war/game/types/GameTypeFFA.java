package com.kill3rtaco.war.game.types;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import com.kill3rtaco.tacoapi.TacoAPI;
import com.kill3rtaco.war.game.GameType;
import com.kill3rtaco.war.game.player.WarPlayer;

public class GameTypeFFA extends GameType {
	
	public static String	ID		= "ffa";
	public static String	NAME	= "Free for All";
	
	public GameTypeFFA() {
		super();
		set(KEY_ID, ID);
		set(KEY_NAME, NAME);
		set(KEY_AUTHOR, "KILL3RTACO");
		set(KEY_TEAMS_ENABLED, false);
	}
	
	public GameTypeFFA(ConfigurationSection config) {
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
	
	@Override
	public String getObjectiveMessage() {
		String[] messages = new String[]{"Kill everyone.",
				"You are alone. Show them who's boss!",
				"Not even Trevor can help you here.",
				"How are you holding up? Because I'M A POTATO. Let's get 'em."};
		return TacoAPI.getChatUtils().getRandomElement(new ArrayList<String>(Arrays.asList(messages)));
	}
	
}
