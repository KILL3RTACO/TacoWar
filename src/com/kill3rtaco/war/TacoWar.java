package com.kill3rtaco.war;

import java.io.File;

import com.kill3rtaco.tacoapi.api.TacoPlugin;
import com.kill3rtaco.tacoapi.obj.ChatObject;
import com.kill3rtaco.war.game.Game;
import com.kill3rtaco.war.game.TacoWarConfig;

public class TacoWar extends TacoPlugin {

	public static TacoWar plugin;
	public static ChatObject chat;
	public static TacoWarConfig config;
	private static Game game;

	@Override
	public void onStart() {
		plugin = this;
		chat = new ChatObject("&9TacoWar");
		config = new TacoWarConfig(new File(getDataFolder() + "/config.yml"));
	}

	@Override
	public void onStop() {

	}

	public static int getNearestDegree(double degree, double factor) {
		return (int) (Math.round(degree / factor) * factor);
	}

	public static Game currentGame() {
		return game;
	}

}
