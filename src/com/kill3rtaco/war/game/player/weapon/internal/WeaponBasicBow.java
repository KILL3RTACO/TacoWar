package com.kill3rtaco.war.game.player.weapon.internal;

import org.bukkit.Material;

import com.kill3rtaco.war.game.player.weapon.InternalWeapon;

public class WeaponBasicBow extends InternalWeapon {
	
	public static final String	ID	= "tw_bow";
	
	@Override
	@SuppressWarnings("deprecation")
	protected void setConfig() {
		set(KEY_ID, ID);
		set(KEY_NAME, "Bow");
		set(KEY_ITEM_INFO, "{id:" + Material.BOW.getId() + "}");
		set(KEY_AMMO, -1);
	}
	
}
