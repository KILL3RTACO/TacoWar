package com.kill3rtaco.war.game.player.kit;

import com.kill3rtaco.war.game.player.WarKit;

public abstract class InternalKit extends WarKit {
	
	public static final WarKit	DEFAULT		= new KitDefault();
	public static final WarKit	HEXPLOSIVES	= new KitExplosiveWeapons();
	public static final WarKit	ROCKETS		= new KitRockets();
	public static final WarKit	SILLY		= new KitSilly();
	
	public InternalKit() {
		super();
	}
	
	public void reload() {
		setConfig();
		super.reload();
	}
	
	protected abstract void setConfig();
	
}
