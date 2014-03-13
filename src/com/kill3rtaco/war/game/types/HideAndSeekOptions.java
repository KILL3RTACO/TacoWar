package com.kill3rtaco.war.game.types;

import org.bukkit.Color;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import com.kill3rtaco.war.TWDefaults;
import com.kill3rtaco.war.game.GameTypeOptions;
import com.kill3rtaco.war.game.player.TeamColor;

public class HideAndSeekOptions extends GameTypeOptions {
	
	private Color		_seekerArmorColor;
	private ItemStack	_seekerWeapon;
	private int			_seekerSpeed;		//percentage, overrides player_speed
											
	public HideAndSeekOptions(YamlConfiguration config) {
		super(config);
		_wPrimary = null;
		_wSecondary = null; //remove weapons from hiders
		_seekerArmorColor = overrideColor("seeker_armor_color", TeamColor.GREEN.getArmorColor());
		_seekerWeapon = overrideItemStack("seeker_weapon", TWDefaults.SEEKER_WEAPON);
		_seekerSpeed = overrideInt("seeker_speed", 125);
	}
	
	public Color seekerArmorColor() {
		return _seekerArmorColor;
	}
	
	public ItemStack seekerWeapon() {
		return _seekerWeapon;
	}
	
	public int seekerSpeed() {
		return _seekerSpeed;
	}
	
}
