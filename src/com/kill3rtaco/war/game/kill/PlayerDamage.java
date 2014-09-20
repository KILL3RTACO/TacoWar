package com.kill3rtaco.war.game.kill;

import com.kill3rtaco.war.game.player.WarPlayer;

public class PlayerDamage {

	private long _time;
	private WarPlayer _player;
	private double _damage;

	public PlayerDamage(WarPlayer player, double damage) {
		this(player, damage, System.currentTimeMillis());
	}

	public PlayerDamage(WarPlayer player, double damage, long time) {
		_time = time;
		_player = player;
		_damage = damage;
	}

	public WarPlayer getAttacker() {
		return _player;
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
