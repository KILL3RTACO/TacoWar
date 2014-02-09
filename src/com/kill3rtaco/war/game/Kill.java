package com.kill3rtaco.war.game;

public class Kill {
	
	private Player	_killer, _victim;
	private String	_weapon;
	
	public Kill(Player killer, String weapon, Player victim) {
		_killer = killer;
		_weapon = weapon;
		_victim = victim;
	}
	
	public Player getKiller() {
		return _killer;
	}
	
	public String getWeapon() {
		return _weapon;
	}
	
	public Player getVictim() {
		return _victim;
	}
	
}
