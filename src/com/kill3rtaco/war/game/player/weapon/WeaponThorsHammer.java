package com.kill3rtaco.war.game.player.weapon;

import java.util.Arrays;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class WeaponThorsHammer extends InternalWeapon {

	@Override
	protected ConfigurationSection getWeaponConfig() {
		YamlConfiguration c = new YamlConfiguration();

		c.set(KEY_ID, "mjolnir");
		c.set(KEY_NAME, "Thor's Hammer");
		c.set(KEY_DESC, Arrays.asList("\"You've got the Lightning... Light the bastards up!\"", "  - Captain America"));
		c.set(KEY_ON_USE, "explode 4");
		c.set(KEY_AMMO, 5);

		return c;
	}

}
