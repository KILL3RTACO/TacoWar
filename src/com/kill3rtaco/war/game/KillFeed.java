package com.kill3rtaco.war.game;

import java.util.ArrayList;
import java.util.List;

import com.kill3rtaco.war.TacoWar;

public class KillFeed {
	
	private List<Kill>	_feed;
	
	public KillFeed() {
		_feed = new ArrayList<Kill>();
	}
	
	public Kill addToFeed(Player killer, String weapon, Player victim) {
		Kill kill = new Kill(killer, weapon, victim);
		addToFeed(kill);
		return kill;
	}
	
	public void addToFeed(Kill kill) {
		_feed.add(kill);
	}
	
	public void printKill(Kill kill) {
		Game game = TacoWar.plugin.currentGame();
		Player killer = kill.getKiller();
		Player victim = kill.getVictim();
		String weapon = kill.getWeapon();
		String message;
		if(killer.equals(victim)) {
			message = "&7[&c" + weapon + "&7] " + victim.getColorfulName();
		} else {
			message = killer.getColorfulName() + " &7[&c" + kill.getWeapon() + "&7] " + victim.getColorfulName();
		}
		game.sendMessageToPlayers(message);
	}
	
}
