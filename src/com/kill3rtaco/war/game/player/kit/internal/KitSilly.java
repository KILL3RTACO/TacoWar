package com.kill3rtaco.war.game.player.kit.internal;

import java.util.Arrays;

import com.kill3rtaco.war.game.player.kit.Food;
import com.kill3rtaco.war.game.player.kit.InternalKit;
import com.kill3rtaco.war.game.player.weapon.internal.WeaponHotFork;
import com.kill3rtaco.war.game.player.weapon.internal.WeaponTheButton;
import com.kill3rtaco.war.game.player.weapon.internal.WeaponTheLie;

public class KitSilly extends InternalKit {
	
	public static final String	ID	= "tw_silly";
	
	@Override
	protected void setConfig() {
		set(KEY_ID, ID);
		set(KEY_NAME, "Silly Weapons");
		set(KEY_WEAPONS, Arrays.asList(WeaponHotFork.ID, WeaponTheButton.ID, WeaponTheLie.ID));
		set(KEY_FOOD, Food.DEF_FOOD_LIST);
	}
	
}
