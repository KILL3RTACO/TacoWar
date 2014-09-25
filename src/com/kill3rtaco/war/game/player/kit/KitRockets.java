package com.kill3rtaco.war.game.player.kit;

import java.util.Arrays;

import com.kill3rtaco.war.game.player.Food;

public class KitRockets extends InternalKit {

	public static final String	ID	= "tw_rockets";

	@Override
	protected void setConfig() {
		set(KEY_ID, ID);
		set(KEY_NAME, "Rockets");
		set(KEY_WEAPONS, Arrays.asList("rocket_launcher"));
		set(KEY_FOOD, Food.DEF_FOOD_LIST);
	}
}
