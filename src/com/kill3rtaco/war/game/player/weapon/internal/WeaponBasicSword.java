package com.kill3rtaco.war.game.player.weapon.internal;

import org.bukkit.Material;

import com.kill3rtaco.war.game.player.weapon.InternalWeapon;

public class WeaponBasicSword extends InternalWeapon {
	
	public static final String	ID	= "tw_sword";
	
	@Override
	@SuppressWarnings("deprecation")
	protected void setConfig() {
		set(KEY_ID, ID);
		set(KEY_NAME, "Sword");
		set(KEY_ITEM_INFO, "{id:" + Material.IRON_SWORD.getId() + "}");
		set(KEY_AMMO, -1);
	}
	
}
