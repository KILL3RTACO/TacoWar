package com.kill3rtaco.war.game.types;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import com.kill3rtaco.war.game.GameTypeOptions;
import com.kill3rtaco.war.game.kill.AttackInfo;
import com.kill3rtaco.war.game.player.Player;

public class FFAOptions extends GameTypeOptions {
	
	//TODO players have customizable armor colors?
	
	public FFAOptions(YamlConfiguration config) {
		super(config);
		_teamsEnabled = false;
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
