package com.kill3rtaco.war.game.tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.scheduler.BukkitRunnable;

import com.kill3rtaco.war.TacoWar;
import com.kill3rtaco.war.game.map.WarMap;
import com.kill3rtaco.war.util.WarPlayerList;

public class MapVoteSession {
	
	private static boolean				_running;
	private static List<String>			_voters;
	private static List<WarMap>			_maps;
	private static Map<WarMap, Integer>	_votes;
	
	public static void setMaps(List<WarMap> maps) {
		_voters = new ArrayList<String>();
		_maps = maps;
		_votes = new HashMap<WarMap, Integer>();
		for (WarMap m : maps) {
			_votes.put(m, 0);
		}
	}
	
	public static WarMap getMap(int map) {
		if (map < 0 || map >= _maps.size())
			return null;
		
		return _maps.get(map);
	}
	
	public static boolean hasVoted(String player) {
		return _voters.contains(player);
	}
	
	public static WarMap addVote(String player, int map) {
		_voters.add(player);
		WarMap voteFor = getMap(map);
		_votes.put(voteFor, getVotesFor(voteFor) + 1);
		return voteFor;
	}
	
	public static int getVotesFor(WarMap map) {
		if (!_votes.containsKey(map))
			return 0;
		return _votes.get(map);
	}
	
	public static WarMap getMostVoted() {
		int max = 0;
		List<WarMap> maps = new ArrayList<WarMap>();
		for (WarMap m : maps) {
			int votes = getVotesFor(m);
			if (votes == max) {
				maps.add(m);
			} else if (votes > max) {
				maps.clear();
				maps.add(m);
			}
		}
		
		if (maps.size() == 1)
			return maps.get(0);
		
		return maps.get(new Random().nextInt(maps.size()));
	}
	
	public static void start() {
		if (_running)
			return;
		_running = true;
		WarPlayerList players = TacoWar.currentGame().getPlayers();
		for (int i = 0; i < _maps.size(); i++) {
			WarMap map = _maps.get(i);
			players.broadcast("&e" + i + ". " + map.getName() + "&7[&a" + map.getAuthor() + "&7]");
		}
		players.broadcast("&e" + (_maps.size() + 1) + ". Random Map");
		players.broadcast("&eUse &a/war vote &eto vote for a map");
		new BukkitRunnable() {
			
			@Override
			public void run() {
				end();
			}
			
		}.runTaskLater(TacoWar.plugin, 30 * 20L);
	}
	
	public static boolean isRunning() {
		return _running;
	}
	
	public static boolean end() {
		if (!_running)
			return false;
		_running = false;
		TacoWar.currentGame().decideRest();
		return true;
	}
	
}
