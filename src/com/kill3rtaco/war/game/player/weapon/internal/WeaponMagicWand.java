package com.kill3rtaco.war.game.player.weapon.internal;

import java.util.Arrays;

import org.bukkit.Material;

import com.kill3rtaco.war.game.player.weapon.InternalWeapon;

public class WeaponMagicWand extends InternalWeapon {

	public static final String	ID	= "magic_wand";

	@Override
	@SuppressWarnings("deprecation")
	protected void setConfig() {
		set(KEY_ID, ID);
		set(KEY_ITEM_INFO, "{id: " + Material.STICK.getId() + "}");
		set(KEY_NAME, "Magic Wand");
		set(KEY_DESC, Arrays.asList("\"You're a wizard, Harry!\"", "  - Hagrid"));
		set(KEY_ON_USE, Arrays.asList(ACTION_FIREBALL));
		set(KEY_AMMO, -1);
	}

}
