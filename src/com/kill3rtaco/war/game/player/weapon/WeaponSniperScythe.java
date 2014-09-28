package com.kill3rtaco.war.game.player.weapon;

import java.util.Arrays;

import org.bukkit.Material;

public class WeaponSniperScythe extends InternalWeapon {
	
	public static final String	ID	= "crescent_rose";
	
	@Override
	@SuppressWarnings("deprecation")
	protected void setConfig() {
		set(KEY_ID, ID);
		set(KEY_NAME, "Sniper Scythe");
		set(KEY_ITEM_INFO, "{id:" + Material.IRON_HOE.getId() + "}");
		set(KEY_DESC, Arrays.asList("\"It's also a customizable, high-impact sniper rifle.\"", "  - Ruby Rose"));
		set(KEY_ON_USE, Arrays.asList(ACTION_ARROW));
		set(KEY_ON_HIT, Arrays.asList(ACTION_DAMAGE + " 4"));
		set(KEY_AMMO, 16);
	}
}
