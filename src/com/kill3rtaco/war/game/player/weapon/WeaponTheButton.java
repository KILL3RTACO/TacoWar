package com.kill3rtaco.war.game.player.weapon;

import java.util.Arrays;

import org.bukkit.Material;

public class WeaponTheButton extends InternalWeapon {

	public static final String	ID	= "plan_g";

	@Override
	@SuppressWarnings("deprecation")
	protected void setConfig() {
		set(KEY_ID, ID);
		set(KEY_NAME, "The Button");
		set(KEY_ITEM_INFO, "{id: " + Material.STONE_BUTTON.getId() + "}");
		set(KEY_DESC, Arrays.asList("\"Yeah, it's a button! What does it do? ... OOOHHHH\"", "  - Michael Jones"));
		set(KEY_ON_USE, Arrays.asList(ACTION_EXPLODE + " 4"));
	}

}
