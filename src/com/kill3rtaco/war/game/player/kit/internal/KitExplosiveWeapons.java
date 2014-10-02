package com.kill3rtaco.war.game.player.kit.internal;

import java.util.Arrays;

import com.kill3rtaco.war.game.player.kit.Food;
import com.kill3rtaco.war.game.player.kit.InternalKit;
import com.kill3rtaco.war.game.player.weapon.internal.WeaponExplosiveAxe;
import com.kill3rtaco.war.game.player.weapon.internal.WeaponMagicWand;
import com.kill3rtaco.war.game.player.weapon.internal.WeaponRocketLauncher;

public class KitExplosiveWeapons extends InternalKit {
	
	public static final String	ID	= "tw_hexplosives";
	
	@Override
	protected void setConfig() {
		set(KEY_ID, ID);
		set(KEY_NAME, "Hexplosives");
		set(KEY_WEAPONS, Arrays.asList(WeaponRocketLauncher.ID, WeaponExplosiveAxe.ID, WeaponMagicWand.ID));
		set(KEY_FOOD, Food.DEF_FOOD_LIST);
	}
	
}
