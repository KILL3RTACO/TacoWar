package com.kill3rtaco.war.game.player.weapon;

import org.bukkit.configuration.ConfigurationSection;

import com.kill3rtaco.war.game.player.Weapon;

public abstract class InternalWeapon extends Weapon {

	public InternalWeapon() {
		_config = getWeaponConfig();
		load();
	}

	protected abstract ConfigurationSection getWeaponConfig();

}
