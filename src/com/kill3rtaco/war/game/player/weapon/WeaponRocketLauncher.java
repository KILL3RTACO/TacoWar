package com.kill3rtaco.war.game.player.weapon;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class WeaponRocketLauncher extends InternalWeapon {

	@Override
	protected ConfigurationSection getWeaponConfig() {
		YamlConfiguration c = new YamlConfiguration();

		c.set(KEY_ID, "rocket_launcher");
		c.set(KEY_NAME, "Rocket Launcher");
		c.set(KEY_DESC, "Explode 'em good!");
		c.set(KEY_ON_USE, "explode 4");
		c.set(KEY_AMMO, 6);

		return c;
	}

}
