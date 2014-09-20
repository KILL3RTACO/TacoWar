package com.kill3rtaco.war.game.kill;

import com.kill3rtaco.war.TacoWar;
import com.kill3rtaco.war.game.Game;
import com.kill3rtaco.war.game.player.WarPlayer;

public class PlayerKill {

	private WarPlayer _killer, _victim;
	private String _weapon;

	public PlayerKill(WarPlayer killer, String weapon, WarPlayer victim) {
		_killer = killer;
		_weapon = weapon;
		_victim = victim;
	}

	public WarPlayer getKiller() {
		return _killer;
	}

	public String getWeapon() {
		return _weapon;
	}

	public WarPlayer getVictim() {
		return _victim;
	}

	public void broadcast() {
		Game game = TacoWar.currentGame();
		String weaponDisplay = "&8[&4" + _weapon + "&8]";
		String message;
		if (_killer.equals(_victim)) {
			message = weaponDisplay + " " + _victim.getColorfulName();
		} else {
			message = _killer.getColorfulName() + " " + weaponDisplay + " " + _victim.getColorfulName();
		}
		game.broadcast(message);
	}

}
