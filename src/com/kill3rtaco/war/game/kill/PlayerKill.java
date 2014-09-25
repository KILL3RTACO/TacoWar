package com.kill3rtaco.war.game.kill;

import java.util.ArrayList;
import java.util.List;

import com.kill3rtaco.war.TacoWar;
import com.kill3rtaco.war.game.Game;
import com.kill3rtaco.war.game.player.WarPlayer;

public class PlayerKill {

	private WarPlayer		_killer, _victim;
	private String			_weapon;
	private List<WarPlayer>	_assists;

	public PlayerKill(WarPlayer killer, String weapon, WarPlayer victim) {
		_killer = killer;
		_weapon = weapon;
		_victim = victim;
		_assists = new ArrayList<WarPlayer>();
	}

	public void setAssists(List<WarPlayer> players) {
		_assists = players;
	}

	public List<WarPlayer> getAssists() {
		return _assists;
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
