package com.kill3rtaco.war.game;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import com.kill3rtaco.war.game.kill.KillFeed;
import com.kill3rtaco.war.game.map.Map;
import com.kill3rtaco.war.game.player.Kit;
import com.kill3rtaco.war.game.player.WarPlayer;
import com.kill3rtaco.war.util.WarUtil;

public class Game {

	private int _currentGametype = 0;
	private boolean _running = false;
	private ArrayList<WarPlayer> _players = new ArrayList<WarPlayer>();
	private KillFeed _killfeed = new KillFeed();
	private Map _map;
	private GameType _gametype;
	private Kit _kit;

	public Game() {

	}

	public Map currentMap() {
		return _map;
	}

	public GameType currentGameType() {
		return _gametype;
	}

	public Kit currentKit() {
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

	public int getGameType() {
		return _currentGametype;
	}

	public void addPlayerToGame(String name) {
		WarPlayer player = new WarPlayer(name);
		//spawn at base
		//give items
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

}
