package com.kill3rtaco.war.game.player.weapon.internal;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import com.kill3rtaco.war.game.player.weapon.InternalWeapon;

public class WeaponSharpshooterBow extends InternalWeapon {

	public static final String	ID	= "legolas_bow";

	@Override
	@SuppressWarnings("deprecation")
	protected void setConfig() {
		//Power II, Punch I
		String enchantments = Enchantment.ARROW_DAMAGE.getId() + ":2;" + Enchantment.ARROW_KNOCKBACK.getId() + ":1";

		set(KEY_ID, ID);
		set(KEY_ITEM_INFO, "{id: " + Material.BOW.getId() + ", enchantments: " + enchantments + "}");
		set(KEY_NAME, "Sharpshooter's Bow");
		set(KEY_DESC, Arrays.asList("\"I'm on seventeen!\"", "  - Legolas"));
		set(KEY_AMMO, -1);
	}

}
