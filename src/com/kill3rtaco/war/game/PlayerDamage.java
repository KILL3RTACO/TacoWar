package com.kill3rtaco.war.game;

public class PlayerDamage {
	
	private long	_time;
	private Player	_player;
	private double	_damage;
	
	public PlayerDamage(Player player, double damage) {
		this(player, damage, System.currentTimeMillis());
	}
	
	public PlayerDamage(Player player, double damage, long time) {
		_time = time;
		_player = player;
		_damage = damage;
	}
	
	public Player getAttacker() {
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
