package com.kill3rtaco.war.game.player.kit;

import java.util.Arrays;

import com.kill3rtaco.war.game.player.Food;
import com.kill3rtaco.war.game.player.weapon.WeaponExplosiveAxe;
import com.kill3rtaco.war.game.player.weapon.WeaponMagicWand;
import com.kill3rtaco.war.game.player.weapon.WeaponRocketLauncher;

public class KitExplosiveWeapons extends InternalKit {

	public static final String	ID	= "";

	@Override
	protected void setConfig() {
		set(KEY_ID, ID);
		set(KEY_NAME, "Hexplosives");
		set(KEY_WEAPONS, Arrays.asList(WeaponRocketLauncher.ID, WeaponExplosiveAxe.ID, WeaponMagicWand.ID));
		set(KEY_FOOD, Food.DEF_FOOD_LIST);
	}

}
