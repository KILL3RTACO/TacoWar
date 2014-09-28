package com.kill3rtaco.war.game.player.weapon;

import com.kill3rtaco.war.game.player.Weapon;

public abstract class InternalWeapon extends Weapon {

	public static final Weapon	BASEBALL_BAT	= new WeaponBaseballBat();
	public static final Weapon	BASIC_BOW		= new WeaponBasicBow();
	public static final Weapon	BASIC_SWORD		= new WeaponBasicSword();
	public static final Weapon	CRESCENT_ROSE	= new WeaponSniperScythe();
	public static final Weapon	EXPLOSIVE_AXE	= new WeaponExplosiveAxe();
	public static final Weapon	FISHBONES		= new WeaponRocketLauncher();
	public static final Weapon	HOT_FORK		= new WeaponHotFork();
	public static final Weapon	LEGOLAS_BOW		= new WeaponSharpshooterBow();
	public static final Weapon	MAGIC_WAND		= new WeaponMagicWand();
	public static final Weapon	MJOLNIR			= new WeaponThorsHammer();
	public static final Weapon	PLAN_G			= new WeaponTheButton();
	public static final Weapon	THE_LIE			= new WeaponTheLie();

	public InternalWeapon() {
		super();
	}

	public void reload() {
		setConfig();
		super.reload();
	}

	protected abstract void setConfig();

}
