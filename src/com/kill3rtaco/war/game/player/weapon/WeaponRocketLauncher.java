package com.kill3rtaco.war.game.player.weapon;

import java.util.Arrays;

import org.bukkit.Material;

public class WeaponRocketLauncher extends InternalWeapon {

	public static final String	ID	= "fishbones";

	@Override
	@SuppressWarnings("deprecation")
	protected void setConfig() {
		set(KEY_ID, ID);
		set(KEY_ITEM_INFO, "{id: " + Material.BOW.getId() + "}");
		set(KEY_NAME, "Rocket Launcher");
		set(KEY_DESC, Arrays.asList("\"I don't even think once about blowin' stuff up!\"", "  - Jinx"));
		set(KEY_ON_PROJECTILE_HIT, Arrays.asList(ACTION_EXPLODE + " 4"));
		set(KEY_AMMO, 6);
	}

}
