package com.kill3rtaco.war.game.types;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import com.kill3rtaco.war.game.GameTypeOptions;
import com.kill3rtaco.war.game.Kit;
import com.kill3rtaco.war.game.kill.AttackInfo;
import com.kill3rtaco.war.game.player.Player;
import com.kill3rtaco.war.game.player.TeamConstants;

public class InfectionOptions extends GameTypeOptions {
	
	private Color	_infectedArmorColor, _survivorArmorColor;
	private int		_infectedSpeed;
	private Kit		_infectedKit, _survivorKit;
	
	public InfectionOptions(YamlConfiguration config) {
		super(config);
		_infectedKit = kit("infected_kit");
		_survivorKit = kit("survivor_kit");
		_infectedArmorColor = overrideColor("infected_armor_color", TeamConstants.GREEN_ARMOR);
		_survivorArmorColor = overrideColor("survivor_armor_color", TeamConstants.BLUE_ARMOR);
		_infectedSpeed = overrideInt("infected_speed", 125);
	}
	
	public Color infectedArmorColor() {
		return _infectedArmorColor;
	}
	
	public Color survivorArmorColor() {
		return _survivorArmorColor;
	}
	
	public float infectedSpeed() {
		return toSpeedFloat(_infectedSpeed);
	}
	
	public Kit infectedKit() {
		return _infectedKit;
	}
	
	public Kit survivorKit() {
		return _survivorKit;
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
