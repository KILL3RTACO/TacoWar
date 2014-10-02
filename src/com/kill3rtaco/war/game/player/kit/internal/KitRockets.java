package com.kill3rtaco.war.game.player.kit.internal;

import java.util.Arrays;

import com.kill3rtaco.war.game.player.kit.Food;
import com.kill3rtaco.war.game.player.kit.InternalKit;
import com.kill3rtaco.war.game.player.weapon.internal.WeaponRocketLauncher;

public class KitRockets extends InternalKit {
	
	public static final String	ID	= "tw_rockets";
	
	@Override
	protected void setConfig() {
		set(KEY_ID, ID);
		set(KEY_NAME, "Rockets");
		set(KEY_WEAPONS, Arrays.asList(WeaponRocketLauncher.ID));
		set(KEY_FOOD, Food.DEF_FOOD_LIST);
	}
}
