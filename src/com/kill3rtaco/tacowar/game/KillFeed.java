package com.kill3rtaco.tacowar.game;

import java.util.ArrayList;
import java.util.List;

import com.kill3rtaco.tacowar.TacoWar;

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
		String message = kill.getKiller().getColorfulName() + " &7[&c" + kill.getWeapon() + "&7] " + kill.getVictim();
		game.sendMessageToPlayers(message);
	}
	
}
