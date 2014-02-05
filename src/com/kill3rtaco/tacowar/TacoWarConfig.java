package com.kill3rtaco.tacowar;

import java.io.File;

import org.bukkit.World;

import com.kill3rtaco.tacoapi.api.TacoConfig;

public class TacoWarConfig extends TacoConfig {
	
	public TacoWarConfig(File file) {
		super(file);
	}
	
	@Override
	protected void setDefaults() {
		addDefaultValue("war_world", "world");
	}
	
	public World getWarWorld() {
		return TacoWar.plugin.getServer().getWorld(getString("war_world"));
	}
	
}
