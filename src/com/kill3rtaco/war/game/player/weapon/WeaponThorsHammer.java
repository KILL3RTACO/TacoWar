package com.kill3rtaco.war.game.player.weapon;

import java.util.Arrays;

import org.bukkit.Material;

public class WeaponThorsHammer extends InternalWeapon {

	@Override
	@SuppressWarnings("deprecation")
	protected void setConfig() {
		set(KEY_ID, "mjolnir");
		set(KEY_ITEM_INFO, "{id: " + Material.STONE_AXE.getId() + "}");
		set(KEY_NAME, "Thor's Hammer");
		set(KEY_DESC, Arrays.asList("\"You've got the Lightning; Light the bastards up.\"", "  - Captain America"));
		set(KEY_ON_USE, Arrays.asList(ACTION_LIGHTNING));
		set(KEY_AMMO, 5);
	}

}
