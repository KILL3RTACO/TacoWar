package com.kill3rtaco.war.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.kill3rtaco.tacoapi.util.Time;
import com.kill3rtaco.war.TacoWar;

public class Game {
	
	private Map				_map;
	private List<Player>	_players;
	private long			_startTime, _endTime;
	private int				_maxKills, _checkerId;
	private long			_timeLimit, _idleTimeLimit, _lastAction;
	private GameState		_state;
	private Scoreboard		_board;
	private KillFeed		_killFeed;
	private Playlist		_playlist;
	
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
	
	public void setTimeLimits() {
		long minute = 1000 * 60 * 60;
		_timeLimit = TacoWar.config.getTimeLimit() * minute;
		_idleTimeLimit = TacoWar.config.getIdleTimeLimit() * minute;
	}
	
	public Map selectRandomMap() {
		List<Map> maps = TacoWar.plugin.getMaps();
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
	
	public void addPlayer(org.bukkit.entity.Player p) {
		addPlayer(p, _map.randomTeam());
	}
	
	public void addPlayer(org.bukkit.entity.Player p, TeamColor team) {
		if(isPlaying(p)) {
			return;
		}
		Player player = new Player(p);
		player.setTeam(team);
		launchPlayer(player);
	}
	
	private void launchPlayer(Player p) {
		p.getBukkitPlayer().setScoreboard(_board);
		p.teleport(_map.getTeamSpawn(p.getTeam()));
		p.addArmor();
		p.giveItems();
		p.setCanFly(false);
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
		List<TeamColor> teams = new ArrayList<TeamColor>(_map.getTeamSpawns().keySet());
		Objective obj = _board.registerNewObjective("tw.game.kills", "");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName(ChatColor.RED + "War");
		for(TeamColor t : teams) {
			Team team = _board.registerNewTeam(t.getName());
			team.setAllowFriendlyFire(TacoWar.config.friendlyFireEnabled());
			team.setCanSeeFriendlyInvisibles(true);
			setScore(t, 0);
		}
		for(Player p : _players) {
			if(teams.isEmpty()) {
				teams = new ArrayList<TeamColor>(_map.getTeamSpawns().keySet());
			}
			TeamColor color = teams.remove(new Random().nextInt(teams.size()));
			p.setTeam(color);
			_board.getTeam(color.getName()).addPlayer(p.getBukkitPlayer());
		}
		
	}
	
	private Score getScoreboardScore(TeamColor team) {
		return _board.getObjective("tw.game.kills").getScore(Bukkit.getOfflinePlayer(team.getColorfulName() + "     "));
	}
	
	public void setScore(TeamColor team, int score) {
		getScoreboardScore(team).setScore(score);
		_lastAction = System.currentTimeMillis();
	}
	
	public int getScore(TeamColor team) {
		return getScoreboardScore(team).getScore();
	}
	
	public void addToScore(TeamColor team, int add) {
		Score s = getScoreboardScore(team);
		s.setScore(s.getScore() + add);
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
				Team team = _board.getTeam(p.getTeam().getName());
				team.removePlayer(p.getBukkitPlayer());
				p.getBukkitPlayer().setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
				p.teleport(_map.getLobbyLocation());
				p.clearInventory();
			}
			TeamColor winningTeam = getTeamInLead();
			sendMessageToPlayers("&aThe game is over! " + winningTeam.getColorfulName() + " Team &ais the winner!");
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
		_maxKills = _players.size() * (2 / 3);
	}
	
	public void sendScores(org.bukkit.entity.Player player) {
		List<TeamColor> teams = new ArrayList<TeamColor>(_map.getTeamSpawns().keySet());
		Collections.sort(teams, new Comparator<TeamColor>() {
			
			@Override
			public int compare(TeamColor t1, TeamColor t2) {
				Integer score1 = getScore(t1);
				Integer score2 = getScore(t2);
				return score1.compareTo(score2);
			}
			
		});
		String scores = "";
		for(TeamColor t : teams) {
			scores += t.getColorfulName() + " &7- " + getScore(t) + " ";
		}
		TacoWar.plugin.chat.sendPlayerMessageNoHeader(player, scores.trim());
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
		_maxKills = max <= 0 ? 1 : max;
	}
	
	public int getMaxKills() {
		return _maxKills;
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
	
	public TeamColor getTeamInLead() {
		int max = -1;
		TeamColor winning = null;
		for(TeamColor t : _map.getTeamSpawns().keySet()) {
			int score = getScore(t);
			if(score > max) {
				max = score;
				winning = t;
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
	
	public boolean teamExists(TeamColor color) {
		for(TeamColor c : _map.teams()) {
			if(c == color) {
				return true;
			}
		}
		return false;
	}
	
	public enum GameState {
		IN_LOBBY, IN_PROGRESS, POST_GAME;
	}
	
	public int getWinningScore() {
		int max = -1;
		for(TeamColor t : _map.getTeamSpawns().keySet()) {
			int score = getScore(t);
			if(score > max) {
				max = score;
			}
		}
		return max;
	}
	
}
