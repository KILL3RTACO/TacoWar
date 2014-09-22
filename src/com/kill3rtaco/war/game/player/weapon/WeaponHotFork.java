package com.kill3rtaco.war.game.player.weapon;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class WeaponHotFork extends InternalWeapon {

	protected ConfigurationSection getWeaponConfig() {
		YamlConfiguration c = new YamlConfiguration();

		c.set(KEY_ID, "hot_fork");
		c.set(KEY_NAME, "Hot Fork");
		c.set(KEY_DESC, "Pokey pokey!");
		c.set(KEY_ON_HIT, "damage 2");
		c.set(KEY_ON_DEATH, "explode 1");
		c.set(KEY_AMMO, -1);

		return c;
	}

}
