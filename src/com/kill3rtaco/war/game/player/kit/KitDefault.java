package com.kill3rtaco.war.game.player.kit;

import java.util.Arrays;

import com.kill3rtaco.war.game.player.Food;
import com.kill3rtaco.war.game.player.weapon.WeaponBasicBow;
import com.kill3rtaco.war.game.player.weapon.WeaponBasicSword;

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
