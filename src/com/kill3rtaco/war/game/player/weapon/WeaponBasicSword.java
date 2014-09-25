package com.kill3rtaco.war.game.player.weapon;

import org.bukkit.Material;

public class WeaponBasicSword extends InternalWeapon {

	public static final String	ID	= "tw_sword";

	@Override
	@SuppressWarnings("deprecation")
	protected void setConfig() {
		set(KEY_ID, ID);
		set(KEY_ITEM_INFO, "{id:" + Material.IRON_SWORD.getId() + "}");
		set(KEY_AMMO, -1);
	}

}
