package com.kill3rtaco.war.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;

import com.kill3rtaco.war.game.kill.KillFeed;
import com.kill3rtaco.war.game.map.WarMap;
import com.kill3rtaco.war.game.player.WarKit;
import com.kill3rtaco.war.game.player.WarPlayer;
import com.kill3rtaco.war.game.player.WarTeam;
import com.kill3rtaco.war.util.WarUtil;

public class Game {

	private boolean						_running	= false;
	private ArrayList<WarPlayer>		_players	= new ArrayList<WarPlayer>();
	private KillFeed					_killfeed	= new KillFeed();
	private WarMap							_map;
	private GameType					_gametype;
	private WarKit						_kit;
	private HashMap<WarTeam, Integer>	_scores		= new HashMap<WarTeam, Integer>();

	public Game() {

	}

	public WarMap getMap() {
		return _map;
	}

	public GameType getGameType() {
		return _gametype;
	}

	public WarKit getKit() {
		return _kit;
	}

	public void addPlayersFromQueue() {
		//get players from queue
		//spawn them at lobby
	}

	public void decideMap() {
		//get a random map from Playlist
	}

	public void decideGameType() {
		//get a random GameType from the supported gametypes of current map
	}

	public void decideTeams() {
		//if teams enabled, create teams and add players
		//otherwise, create a team for each player and add said player
	}

	public void decideKit() {
		//if gametype requests a kit, use it
		//otherwise get a random kit from Playlist
	}

	public void start() {
		_running = true;
	}

	public void end() {
		_running = false;
		//spawn all players at lobby
		//remove all players from the game
	}

	public boolean isRunning() {
		return _running;
	}

	public void addPlayerToGame(String name) {
		WarPlayer player = new WarPlayer(name);
		//spawn at base
		//give items
	}

	//can add negative points to remove poitnts
	public void addPoints(WarTeam team, int points) {
		_scores.put(team, getScore(team) + points);
	}

	public void addPoints(HashMap<WarTeam, Integer> points) {
		for (WarTeam t : points.keySet()) {
			addPoints(t, points.get(t));
		}
	}

	public int getScore(WarTeam team) {
		if (!_scores.containsKey(team))
			return 0;
		return _scores.get(team);
	}

	public boolean isPlaying(Player p) {
		return isPlaying(p.getName());
	}

	public boolean isPlaying(String name) {
		return getPlayer(name) != null;
	}

	public void removePlayer(Player p) {
		removePlayer(p.getName());
	}

	public void removePlayer(String name) {
		WarPlayer player = WarUtil.removePlayer(_players, name);
		if (player != null)
			player.clearInventory();
	}

	public WarPlayer getPlayer(Player p) {
		return getPlayer(p.getName());
	}

	public WarPlayer getPlayer(String name) {
		return WarUtil.getPlayer(_players, name);
	}

	public void broadcast(String message) {
		WarUtil.broadcast(_players, message);
	}

	public KillFeed getKillFeed() {
		return _killfeed;
	}

	public WarTeam getTeamInLead() {
		List<WarTeam> teams = new ArrayList<WarTeam>(_scores.keySet());
		Collections.sort(teams, new Comparator<WarTeam>() {

			@Override
			public int compare(WarTeam t1, WarTeam t2) {
				return _scores.get(t2).compareTo(_scores.get(t1));
			}

		});
		return teams.get(0);
	}

}
