package com.kill3rtaco.war.game.types;

import org.bukkit.Color;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import com.kill3rtaco.war.TWDefaults;
import com.kill3rtaco.war.game.GameTypeOptions;
import com.kill3rtaco.war.game.player.TeamColor;

public class InfectionOptions extends GameTypeOptions {
	
	private Color		_infectedArmorColor, _survivorArmorColor;
	private int			_infectedSpeed;
	private ItemStack	_infectedWeapon;
	
	public InfectionOptions(YamlConfiguration config) {
		super(config);
		_wPrimary = TWDefaults.MAX_BOW;
		_wSecondary = null;
		_infectedArmorColor = overrideColor("infected_armor_color", TeamColor.GREEN.getArmorColor());
		_survivorArmorColor = overrideColor("survivor_armor_color", TeamColor.BLUE.getArmorColor());
		_infectedSpeed = overrideInt("infected_speed", 125);
		_infectedWeapon = overrideItemStack("infected_weapon", TWDefaults.MAX_SWORD);
	}
	
	public Color infectedArmorColor() {
		return _infectedArmorColor;
	}
	
	public Color survivorArmorColor() {
		return _survivorArmorColor;
	}
	
	public int infectedSpeed() {
		return _infectedSpeed;
	}
	
	public ItemStack infectedWeapon() {
		return _infectedWeapon;
	}
	
}
