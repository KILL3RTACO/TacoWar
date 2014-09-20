package com.kill3rtaco.war.game.kill;

import java.util.ArrayList;
import java.util.List;

import com.kill3rtaco.war.game.player.WarPlayer;

public class KillFeed {

	private List<PlayerKill> _feed;

	public KillFeed() {
		_feed = new ArrayList<PlayerKill>();
	}

	public PlayerKill addToFeed(WarPlayer killer, String weapon, WarPlayer victim) {
		PlayerKill kill = new PlayerKill(killer, weapon, victim);
		addToFeed(kill);
		return kill;
	}

	public void addToFeed(PlayerKill kill) {
		_feed.add(kill);
	}

}
