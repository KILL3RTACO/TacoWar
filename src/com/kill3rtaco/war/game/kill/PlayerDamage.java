package com.kill3rtaco.war.game.kill;

import com.kill3rtaco.war.game.player.WarPlayer;

public class PlayerDamage {

	private long		_time;
	private WarPlayer	_attacker;
	private double		_damage;

	public PlayerDamage(WarPlayer attacker, double damage) {
		this(attacker, damage, System.currentTimeMillis());
	}

	public PlayerDamage(WarPlayer attacker, double damage, long time) {
		_time = time;
		_attacker = attacker;
		_damage = damage;
	}

	public WarPlayer getAttacker() {
		return _attacker;
	}

	public long getTime() {
		return _time;
	}

	public double getDamage() {
		return _damage;
	}

	public void setTime(long time) {
		_time = time;
	}

	public void setDamage(double damage) {
		_damage = damage;
	}

}
