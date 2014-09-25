package com.kill3rtaco.war.game.player.weapon;

import java.util.Arrays;

import org.bukkit.Material;

public class WeaponExplosiveAxe extends InternalWeapon {

	public static final String	ID	= "explosive_axe";

	@Override
	@SuppressWarnings("deprecation")
	protected void setConfig() {
		set(KEY_ID, ID);
		set(KEY_ITEM_INFO, "{id: " + Material.STONE_AXE.getId() + "}");
		set(KEY_NAME, "Explosive Axe");
		set(KEY_DESC, Arrays.asList("\"I refuse to use this due to the mess it causes."
				+ " Perhaps you can find some use\"", "  - The Executioner"));
		set(KEY_ON_CRIT, Arrays.asList(ACTION_EXPLODE + " 2"));
		set(KEY_AMMO, -1);
	}

}
