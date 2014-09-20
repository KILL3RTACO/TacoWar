package com.kill3rtaco.war.game;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.World;

import com.kill3rtaco.tacoapi.api.TacoConfig;

public class TacoWarConfig extends TacoConfig {

	public static final String KEY_WAR_WORLD = "war_world";

	public TacoWarConfig(File file) {
		super(file);
	}

	@Override
	protected void setDefaults() {
		addDefaultValue(KEY_WAR_WORLD, "world");
	}

	public World getWarWorld() {
		return Bukkit.getWorld(getString(KEY_WAR_WORLD));
	}

}
