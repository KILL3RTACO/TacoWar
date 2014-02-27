package com.kill3rtaco.war.game;

import org.bukkit.configuration.file.YamlConfiguration;

/*
 * abstract for there will be a class for every GameType
 * this class defines the basic config options that every gametype will have,
 * such as id, name and author
*/
public abstract class GameTypeOptions {
	
	private YamlConfiguration	_config;
	
	public GameTypeOptions(YamlConfiguration config) {
		
	}
	
}
