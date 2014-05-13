package com.kill3rtaco.war.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import com.kill3rtaco.tacoapi.util.Time;
import com.kill3rtaco.war.TacoWar;
import com.kill3rtaco.war.game.kill.KillFeed;
import com.kill3rtaco.war.game.map.Map;
import com.kill3rtaco.war.game.player.Player;
import com.kill3rtaco.war.game.player.Team;
import com.kill3rtaco.war.game.types.FFAOptions;
import com.kill3rtaco.war.game.types.HideAndSeekOptions;
import com.kill3rtaco.war.game.types.InfectionOptions;
import com.kill3rtaco.war.game.types.JuggernautOptions;
import com.kill3rtaco.war.game.types.KOTHOptions;
import com.kill3rtaco.war.game.types.TDMOptions;

public class Game {
	
	private Map					_map;
	private List<Player>		_players;
	private long				_startTime, _endTime;
	private int					_maxScore, _checkerId;
	private long				_timeLimit, _idleTimeLimit, _lastAction;
	private GameState			_state;
	private Scoreboard			_board;
	private KillFeed			_killFeed;
	private Playlist			_playlist;
	
	private GameTypeOptions		_gameType;
	private FFAOptions			_ffaOptions;
	private HideAndSeekOptions	_hasOptions;
	private InfectionOptions	_infOptions;
	private JuggernautOptions	_jugOptions;
	private KOTHOptions			_kothOptions;
	private TDMOptions			_tdmOptions;
	
	public Game() {
		_board = Bukkit.getScoreboardManager().getNewScoreboard();
		_playlist = TacoWar.config.getCurrentPlaylist();
		selectRandomMap();
		setTimeLimits();
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if(_state == GameState.IN_LOBBY) {
					start();
				}
			}
			
		}.runTaskLater(TacoWar.plugin, 20L * TacoWar.config.getLobbyWaitTime());
	}
	
	private void selectRandomGameType() {
		
	}
	
	public Playlist getPlaylist() {
		return _playlist;
	}
	
	public void spawnPlayers(List<Location> locs, List<Player> players) {
		int index = 0;
		for(Player p : players) {
			if(index >= locs.size()) {
				index = 0;
			}
			p.teleport(locs.get(index));
			index++;
		}
	}
	
	public void setTimeLimits() {
		long minute = 1000 * 60 * 60;
		_timeLimit = _gameType.timeLimitTicks();
		_idleTimeLimit = TacoWar.config.getIdleTimeLimit() * minute;
	}
	
	public Map selectRandomMap() {
		List<Map> maps = (_playlist == null ? TacoWar.plugin.getMaps() : _playlist.getMaps());
		_map = maps.get(new Random().nextInt(maps.size()));
		teleportPlayersToLobby();
		return _map;
	}
	
	public Map selectMap(String id) {
		Map m = TacoWar.plugin.getMap(id);
		if(m != null) {
			Map lastMap = _map;
			_map = m;
			if(lastMap != _map) {
				teleportPlayersToLobby();
			}
		}
		return m;
	}
	
	public Player getPlayer(org.bukkit.entity.Player player) {
		if(player == null) {
			return null;
		}
		return getPlayer(player.getName());
	}
	
	public Player getPlayer(String playername) {
		for(Player p : _players) {
			if(p.getName().equalsIgnoreCase(playername)) {
				return p;
			}
		}
		return null;
	}
	
	private void addPlayers() {
		_players = new ArrayList<Player>();
		for(org.bukkit.entity.Player p : TacoWar.config.getWarWorld().getPlayers()) {
			Player warPlayer = new Player(p);
			_players.add(warPlayer);
			warPlayer.setAdventureMode();
			warPlayer.setCanFly(true);
		}
		
	}
	
	private void launchPlayer(Player p) {
		
	}
	
	public void teleportPlayersToLobby() {
		_state = GameState.IN_LOBBY;
		if(_players == null || _players.isEmpty()) {
			addPlayers();
		}
		for(Player p : _players) {
			p.clearInventory();
			p.teleport(_map.getLobbyLocation());
		}
		sendMessageToPlayers(_map.toMessage());
		String message = _map.getLobbySpawnMessage();
		if(message != null && !message.isEmpty()) {
			sendMessageToPlayers(message);
		}
	}
	
	private void decideTeams() {
		
	}
	
	private Score getScoreboardScore(String team) {
		return _board.getObjective("tw.game.score").getScore(Bukkit.getOfflinePlayer(team + "     "));
	}
	
	public void setScore(String team, int score) {
		getScoreboardScore(team).setScore(score);
		_lastAction = System.currentTimeMillis();
	}
	
	public int getScore(String team) {
		return getScoreboardScore(team).getScore();
	}
	
	public void updateScoreboard(String team, int points) {
		Score s = getScoreboardScore(team);
		s.setScore(s.getScore() + points);
	}
	
	public void start() {
		if(_players.isEmpty()) {
			end();
			return;
		}
		registerChecker();
		TacoWar.config.getWarWorld().setFullTime(_map.getTimeTicks());
		TacoWar.config.getWarWorld().setGameRuleValue("doDaylightCycle", "false");
		_killFeed = new KillFeed();
		decideTeams();
		for(Player p : _players) {
			launchPlayer(p);
		}
		_state = GameState.IN_PROGRESS;
		_startTime = System.currentTimeMillis();
		_lastAction = _startTime;
		String message = _map.getGameStartMessage();
		if(message != null && !message.isEmpty()) {
			sendMessageToPlayers(message);
		}
	}
	
	public void end() {
		TacoWar.plugin.getServer().getScheduler().cancelTask(_checkerId);
		_state = GameState.POST_GAME;
		_endTime = System.currentTimeMillis();
		if(!_players.isEmpty()) {
			for(Player p : _players) {
				org.bukkit.scoreboard.Team team = _board.getTeam(p.getTeam().getName());
				team.removePlayer(p.getBukkitPlayer());
				p.getBukkitPlayer().setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
				p.teleport(_map.getLobbyLocation());
				p.clearInventory();
			}
			//winner team message
			if(TacoWar.plugin.isAutomating()) {
				sendMessageToPlayers("&aThe next game will start soon");
			}
		}
		if(TacoWar.plugin.isAutomating()) {
			new BukkitRunnable() {
				
				@Override
				public void run() {
					TacoWar.plugin.startNewGame();
				}
				
			}.runTaskLater(TacoWar.plugin, 20L * TacoWar.config.getNextGameWait());
		}
	}
	
	public void determineMaxKills() {
		_maxScore = _players.size() * (2 / 3);
	}
	
	private void registerChecker() {
		_checkerId = new BukkitRunnable() {
			
			@Override
			public void run() {
				if(getGameRunTime() >= _timeLimit) {
					sendMessageToPlayers("&aGame has exceeded the time limit, ending game...");
					end();
				} else if(getGameIdleTime() >= _idleTimeLimit) {
					sendMessageToPlayers("&aNothing has happened in a while, ending game...");
					end();
				}
			}
			
		}.runTaskTimerAsynchronously(TacoWar.plugin, 0, 20L).getTaskId();
	}
	
	public long getGameRunTime() {
		switch(_state) {
			case IN_PROGRESS:
				return System.currentTimeMillis() - _startTime;
			case POST_GAME:
				return _endTime - _startTime;
			default:
				return 0;
		}
	}
	
	public String getGameRunTimeString() {
		return time(getGameRunTime());
	}
	
	public long getTimeLeft() {
		return _timeLimit - _startTime;
	}
	
	public String getTimeLeftString() {
		if(_timeLimit == 0) {
			return "Infinite";
		}
		return time(getTimeLeft());
	}
	
	private String time(long time) {
		Time t = new Time(time);
		String timeStr = "";
		if(t.getHours() > 0)
			timeStr += t.getHours() + ":";
		return timeStr + ":" + t.getSeconds();
	}
	
	public void setMaxKills(int max) {
		_maxScore = max <= 0 ? 1 : max;
	}
	
	public int getMaxScore() {
		return _maxScore;
	}
	
	public long getTimeLimit() {
		return _timeLimit;
	}
	
	public void setTimeLimit(long limit) {
		_timeLimit = limit;
	}
	
	public void sendMessageToPlayers(String message) {
		for(Player p : _players) {
			p.sendMessage(message);
		}
	}
	
	public GameState getGameState() {
		return _state;
	}
	
	public boolean isPlaying(org.bukkit.entity.Player player) {
		return getPlayer(player) != null;
	}
	
	public boolean isPlaying(String playername) {
		return getPlayer(playername) != null;
	}
	
	public Map getMap() {
		return _map;
	}
	
	public Team getTeamInLead() {
		int max = 0;
		Team winning = null;
		for(org.bukkit.scoreboard.Team t : _board.getTeams()) {
			int score = getScore(t.getName());
			if(score > max) {
				max = score;
				for(Player p : _players) {
					Team team = p.getTeam();
					if(team.getName().equalsIgnoreCase(t.getName()))
						winning = team;
				}
			}
		}
		return winning;
	}
	
	public long getGameIdleTime() {
		return System.currentTimeMillis() - _lastAction;
	}
	
	public boolean isRunning() {
		return _state == GameState.IN_PROGRESS;
	}
	
	public void updatePlayer(org.bukkit.entity.Player player) {
		Player p = getPlayer(player.getName());
		if(p != null) {
			p.setPlayer(player);
		}
	}
	
	public KillFeed getKillFeed() {
		return _killFeed;
	}
	
	//get if a team exists
	
	public int getWinningScore() {
		String winner = getTeamInLead().getName();
		return getScore(winner);
	}
	
	public GameTypeOptions options() {
		return _gameType;
	}
	
	public enum GameState {
		IN_LOBBY, IN_PROGRESS, POST_GAME;
	}
	
}
