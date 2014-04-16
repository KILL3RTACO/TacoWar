package com.kill3rtaco.war.game.types;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import com.kill3rtaco.war.TWDefaults;
import com.kill3rtaco.war.game.GameTypeOptions;
import com.kill3rtaco.war.game.kill.AttackInfo;
import com.kill3rtaco.war.game.player.Player;
import com.kill3rtaco.war.game.player.TeamConstants;

public class JuggernautOptions extends GameTypeOptions {
	
	public Color		_juggernautArmorColor;
	public int			_juggernautSpeed;		//percentage
	public ItemStack	_juggernautWeapon;
	
	public JuggernautOptions(YamlConfiguration config) {
		super(config);
		_playerArmorColor = null; //only juggernaut has armor
		_juggernautArmorColor = overrideColor("juggernaut_armor_color", TeamConstants.PURPLE_ARMOR);
		_juggernautSpeed = overrideInt("juggernaut_speed", 150);
		_juggernautWeapon = overrideItemStack("juggernaut_weapon", TWDefaults.MAX_SWORD);
	}
	
	@Override
	public void registerTimers() {
	}
	
	@Override
	public void onPlayerMove(Player player, Location from, Location to) {
	}
	
	@Override
	public boolean onPlayerAttack(AttackInfo info) {
		return false;
	}
	
	@Override
	public Location onPlayerDeath(AttackInfo info) {
		return null;
	}
	
}
