package com.kill3rtaco.war.game.player.kit.internal;

import java.util.Arrays;

import com.kill3rtaco.war.game.player.kit.Food;
import com.kill3rtaco.war.game.player.kit.InternalKit;
import com.kill3rtaco.war.game.player.weapon.internal.WeaponBasicBow;
import com.kill3rtaco.war.game.player.weapon.internal.WeaponBasicSword;

public class KitDefault extends InternalKit {

	public static final String	ID	= "default";

	@Override
	protected void setConfig() {
		set(KEY_ID, ID);
		set(KEY_NAME, "TacoWar Default");
		set(KEY_WEAPONS, Arrays.asList(WeaponBasicSword.ID, WeaponBasicBow.ID));
		set(KEY_FOOD, Food.DEF_FOOD_LIST);
	}
}
