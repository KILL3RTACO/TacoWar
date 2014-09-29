package com.kill3rtaco.war.game.player.weapon.internal;

import java.util.Arrays;

import org.bukkit.Material;

import com.kill3rtaco.war.game.player.weapon.InternalWeapon;

public class WeaponTheLie extends InternalWeapon {

	public static final String	ID	= "the_lie";

	@Override
	@SuppressWarnings("deprecation")
	protected void setConfig() {
		set(KEY_ID, ID);
		set(KEY_NAME, "The Lie");
		set(KEY_ITEM_INFO, "{id:" + Material.CAKE.getId() + "}");
		set(KEY_DESC, Arrays.asList("\"Enrichment Center regulations require both hands to be empty before "
				+ "any cake-- [garbled]\"", "  - GLaDOS"));
		set(KEY_ON_HIT, ACTION_IGNITE + " 40");
	}

}
