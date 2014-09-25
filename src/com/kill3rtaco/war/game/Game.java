package com.kill3rtaco.war.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;

import com.kill3rtaco.war.TacoWarQueue;
import com.kill3rtaco.war.game.kill.KillFeed;
import com.kill3rtaco.war.game.map.WarMap;
import com.kill3rtaco.war.game.player.WarKit;
import com.kill3rtaco.war.game.player.WarPlayer;
import com.kill3rtaco.war.game.player.WarTeam;
import com.kill3rtaco.war.util.WarPlayerList;

public class Game {

	private boolean						_running	= false;
	private WarPlayerList				_players;
	private KillFeed					_killfeed	= new KillFeed();
	private WarMap						_map;
	private GameType					_gametype;
	private WarKit						_kit;
	private HashMap<WarTeam, Integer>	_scores		= new HashMap<WarTeam, Integer>();

	public Game() {
		addPlayersFromQueue();
		decideMap();
		decideGameType();
		decideTeams();
		decideKit();
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
		_players = TacoWarQueue.removeOnlineAsPlayers();
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
		//respawn() all players
	}

	public void end() {
		_running = false;
		//spawn all players at lobby
		//remove all players from the game
		//re-add them to queue
	}

	public boolean isRunning() {
		return _running;
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
		return _players.get(name) != null;
	}

	public void removePlayerFromGame(Player p) {
		removePlayerFromGame(p.getName());
	}

	public void removePlayerFromGame(String name) {
		WarPlayer player = _players.remove(name);
		if (player != null)
			player.clearInventory();
	}

	public WarPlayerList getPlayers() {
		return _players;
	}

	public void broadcast(String message) {
		_players.broadcast(message);
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
