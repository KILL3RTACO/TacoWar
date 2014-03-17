package com.kill3rtaco.war.game.types;

import org.bukkit.Color;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import com.kill3rtaco.war.TWDefaults;
import com.kill3rtaco.war.game.GameTypeOptions;
import com.kill3rtaco.war.game.player.TeamColor;

public class JuggernautOptions extends GameTypeOptions {
	
	public Color		_juggernautArmorColor;
	public int			_juggernautSpeed;		//percentage
	public ItemStack	_juggernautWeapon;
	
	public JuggernautOptions(YamlConfiguration config) {
		super(config);
		_playerArmorColor = null; //only juggernaut has armor
		_juggernautArmorColor = overrideColor("juggernaut_armor_color", TeamColor.PURPLE.getArmorColor());
		_juggernautSpeed = overrideInt("juggernaut_speed", 150);
		_juggernautWeapon = overrideItemStack("juggernaut_weapon", TWDefaults.MAX_SWORD);
	}
	
}
