package com.kill3rtaco.war.game;

import java.util.ArrayList;
import java.util.List;

import com.kill3rtaco.war.TacoWar;

public class KillFeed {
	
	private List<PlayerKill>	_feed;
	
	public KillFeed() {
		_feed = new ArrayList<PlayerKill>();
	}
	
	public PlayerKill addToFeed(Player killer, String weapon, Player victim) {
		PlayerKill kill = new PlayerKill(killer, weapon, victim);
		addToFeed(kill);
		return kill;
	}
	
	public void addToFeed(PlayerKill kill) {
		_feed.add(kill);
	}
	
	public void printKill(PlayerKill kill) {
		Game game = TacoWar.plugin.currentGame();
		Player killer = kill.getKiller();
		Player victim = kill.getVictim();
		String weapon = kill.getWeapon();
		String weaponDisplay = "&8[&4" + weapon + "&8]";
		String message;
		if(killer.equals(victim)) {
			message = weaponDisplay + " " + victim.getColorfulName();
		} else {
			message = killer.getColorfulName() + " " + weaponDisplay + " " + victim.getColorfulName();
		}
		game.sendMessageToPlayers(message);
	}
	
}
