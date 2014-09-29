package com.kill3rtaco.war.game.player.weapon.internal;

import java.util.Arrays;

import org.bukkit.Material;

import com.kill3rtaco.war.game.player.weapon.InternalWeapon;

public class WeaponHotFork extends InternalWeapon {

	public static final String	ID	= "hot_fork";

	@Override
	@SuppressWarnings("deprecation")
	protected void setConfig() {
		set(KEY_ID, ID);
		set(KEY_ITEM_INFO, "{id: " + Material.BLAZE_ROD.getId() + "}");
		set(KEY_NAME, "Hot Fork");
		set(KEY_DESC, Arrays.asList("\"Not all Forks are created equal...\"", "  - Unknown"));
		set(KEY_ON_HIT, ACTION_DAMAGE + " 2");
		set(KEY_ON_DEATH, Arrays.asList(ACTION_EXPLODE + " 1"));
		set(KEY_AMMO, -1);
	}

}
