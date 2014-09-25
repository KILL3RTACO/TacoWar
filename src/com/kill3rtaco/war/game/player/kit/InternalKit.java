package com.kill3rtaco.war.game.player.kit;

import com.kill3rtaco.war.game.player.WarKit;

public abstract class InternalKit extends WarKit {

	public InternalKit() {
		super();
	}

	public void reload() {
		setConfig();
		super.reload();
	}

	protected abstract void setConfig();

}
