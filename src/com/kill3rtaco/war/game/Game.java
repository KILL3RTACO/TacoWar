package com.kill3rtaco.war.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.kill3rtaco.war.TacoWar;
import com.kill3rtaco.war.TacoWarQueue;
import com.kill3rtaco.war.game.kill.KillFeed;
import com.kill3rtaco.war.game.map.Playlist;
import com.kill3rtaco.war.game.map.WarMap;
import com.kill3rtaco.war.game.player.PlayerComparators;
import com.kill3rtaco.war.game.player.WarKit;
import com.kill3rtaco.war.game.player.WarPlayer;
import com.kill3rtaco.war.game.player.WarTeam;
import com.kill3rtaco.war.game.tasks.LaunchCelebrationRocketTask;
import com.kill3rtaco.war.game.tasks.MapVoteSession;
import com.kill3rtaco.war.game.tasks.StartGameSoonTask;
import com.kill3rtaco.war.util.CustomScoreboard;
import com.kill3rtaco.war.util.WarPlayerList;

public class Game {
	
	private boolean				_running	= false;
	private List<WarTeam>		_teams;
	private WarPlayerList		_players;
	private Playlist			_playlist;
	private KillFeed			_killfeed	= new KillFeed();
	private WarMap				_map;
	private GameType			_gametype;
	private WarKit				_kit;
//	private HashMap<WarTeam, Integer>	_scores		= new HashMap<WarTeam, Integer>();
	private CustomScoreboard	_scoreboard;
	
	public Game() {
		this(TacoWar.getPlaylist(""));
	}
	
	public Game(Playlist playlist) {
		addPlayersFromQueue();
		decideMap(); //calls the voting session, when ended it calls all other decide methods
	}
	
	public void startGameSoon() {
		if (_running)
			return;
		new StartGameSoonTask().runTaskTimer(TacoWar.plugin, 0, 20L);
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
		_players = TacoWarQueue.removeOnlineAsPlayers();
		Location worldLobby = TacoWar.config.getWorldLobby();
		if (worldLobby != null)
			_players.teleportAll(worldLobby);
	}
	
	public void decideMap() {
		List<WarMap> maps = _playlist.getMaps(5);
		if (maps.size() == 1) {
			_map = maps.get(0);
			decideRest();
		} else {
			MapVoteSession.setMaps(maps);
			MapVoteSession.start();
		}
	}
	
	public void decideRest() {
		spawnAtMapLobby();
		decideGameType();
		decideTeams();
		decideTeams();
		startGameSoon();
	}
	
	public void spawnAtMapLobby() {
		_players.teleportAll(_map.getLobbyLocation());
		_players.broadcast("&eMap &7- &a" + _map.getName() + " &eby &a" + _map.getAuthor());
	}
	
	public void decideGameType() {
		if (_map == null)
			_map = MapVoteSession.getMostVoted();
		setGameType(_playlist.selectGameType());
	}
	
	public void setGameType(GameType gametype) {
		_gametype = gametype;
		_players.broadcast("&eGameType &7- " + gametype.getConfig().getString(GameType.KEY_NAME));
	}
	
	public void decideTeams() {
		_teams = new ArrayList<WarTeam>();
		if (_gametype.getConfig().getBoolean(GameType.KEY_TEAMS_ENABLED)) {
			ArrayList<WarPlayer> playersToAdd = new ArrayList<WarPlayer>(_players.getPlayers());
			_teams = _map.getTeamsFromSpawnpoints();
			for (WarTeam t : _teams) {
				if (playersToAdd.isEmpty())
					break;
				t.add(playersToAdd.remove(new Random().nextInt(playersToAdd.size())));
			}
		} else {
			for (WarPlayer p : _players) {
				WarTeam team = new WarTeam("ffa_" + p.getName().toLowerCase(), p.getName());
				_scoreboard.addTeam(team);
				_teams.add(team);
				p.setTeam(team);
			}
		}
	}
	
	public void decideKit() {
		WarKit kit = TacoWar.getKit(GameType.KEY_FORCE_KIT);
		if (kit != null)
			_kit = kit;
		else
			setKit(_playlist.selectKit());
	}
	
	public void setKit(WarKit kit) {
		_kit = kit;
		_players.broadcast("&Kit &7- " + kit.getConfig().getString(GameType.KEY_NAME));
	}
	
	public void start() {
		if (_running)
			return;
		_running = true;
		decideKit();
		_players.respawnAll();
		_players.broadcast("&aUsing Kit&7: &e" + _kit.getName());
	}
	
	public void end() {
		_running = false;
		_players.teleportAll(_map.getLobbyLocation());
		new LaunchCelebrationRocketTask().runTaskTimer(TacoWar.plugin, 0, 2L);
		if (_gametype.onKill()) {
			//get mvp
			Collections.sort(_players.getPlayers(), PlayerComparators.KDA);
			WarPlayer mvp = _players.get(0);
			_players.broadcast("&eMVP: " + mvp.getColorfulName() + "");
		}
		
		for (WarPlayer p : _players) {
			TacoWarQueue.addPlayer(p.getName());
		}
		_players.broadcast("&eThe next game will start in &aless than a minute");
		_players.broadcast("&eType &a/war &eto leave the war queue");
		_players = new WarPlayerList();
		new BukkitRunnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
			}
			
		}.runTaskLater(TacoWar.plugin, 60 * 20L);
	}
	
	public boolean isRunning() {
		return _running;
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
	
	public CustomScoreboard getScores() {
		return _scoreboard;
	}
	
	public void updateScoreboard() {
		for (WarTeam t : _teams) {
			_scoreboard.setScore(t.getId(), t.getScore());
		}
	}
	
	public WarTeam getTeamInLead() {
//		List<WarTeam> teams = new ArrayList<WarTeam>(_teams);
		Collections.sort(_teams, new Comparator<WarTeam>() {
			
			@Override
			public int compare(WarTeam t1, WarTeam t2) {
				return new Integer(t2.getScore()).compareTo(t1.getScore());
			}
			
		});
		return _teams.get(0);
	}
	
}
