package com.kill3rtaco.war.game.kill;

import java.util.ArrayList;
import java.util.List;

import com.kill3rtaco.war.TacoWar;
import com.kill3rtaco.war.game.Game;
import com.kill3rtaco.war.game.player.WarPlayer;
import com.kill3rtaco.war.util.WarUtil;

public class PlayerKill {
	
	private WarPlayer		_killer, _victim, _awardTo;
	private String			_weapon;
	private List<WarPlayer>	_assists;
	
	public PlayerKill(WarPlayer killer, String weapon, WarPlayer victim) {
		this(killer, weapon, victim, null);
	}
	
	public PlayerKill(WarPlayer killer, String weapon, WarPlayer victim,
			WarPlayer awardTo) {
		_killer = killer;
		_weapon = weapon;
		_victim = victim;
		_assists = new ArrayList<WarPlayer>();
		_awardTo = awardTo;
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
			if (_awardTo != null)
				message += " &7-> " + _awardTo.getColorfulName();
		}
		game.broadcast(message);
		WarUtil.broadcast(_assists, "&eAssist!");
	}
	
}
