package com.kill3rtaco.war.game.player.weapon.internal;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import com.kill3rtaco.war.game.player.weapon.InternalWeapon;

public class WeaponBaseballBat extends InternalWeapon {

	public static final String	ID	= "baseball_bat";

	@Override
	@SuppressWarnings("deprecation")
	protected void setConfig() {
		String enchantments = Enchantment.KNOCKBACK.getId() + ":5";

		set(KEY_ID, ID);
		set(KEY_ITEM_INFO, "{id:" + Material.WOOD_HOE.getId() + ", enchantments: " + enchantments + "}");
		set(KEY_NAME, "Baseball Bat");
		set(KEY_DESC, Arrays.asList("&oStrrrrike one!", "", "&oThe Baseball Bat is an excellent weapon for removing unwanted enemies."
				+ " With one swing you can launch them great distances across the landscape", "  - Wormopedia"));
		set(KEY_AMMO, -1);
	}
}
