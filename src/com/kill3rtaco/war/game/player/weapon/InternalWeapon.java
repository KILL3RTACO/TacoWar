package com.kill3rtaco.war.game.player.weapon;

import com.kill3rtaco.war.game.player.Weapon;

public abstract class InternalWeapon extends Weapon {

	public InternalWeapon() {
		super();
	}

	public void reload() {
		setConfig();
		super.reload();
	}

	protected abstract void setConfig();

}
