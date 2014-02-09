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
	
	public Game() {
		_board = Bukkit.getScoreboardManager().getNewScoreboard();
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
		List<Map> maps = TacoWar.plugin.getMaps();
		for(Map m : maps) {
			if(m.getId().equals(id)) {
				Map lastMap = _map;
				_map = m;
				if(lastMap != _map) {
					teleportPlayersToLobby();
				}
				return m;
			}
		}
		return null; //no map with given id found
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
	
	public void teleportPlayersToLobby() {
		_state = GameState.IN_LOBBY;
		if(_players == null || _players.isEmpty()) {
			addPlayers();
		}
		for(Player p : _players) {
			p.teleport(_map.getLobbyLocation());
		}
		sendMessageToPlayers(_map.toMessage());
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
		_killFeed = new KillFeed();
		decideTeams();
		for(Player p : _players) {
			p.getBukkitPlayer().setScoreboard(_board);
			p.teleport(_map.getTeamSpawn(p.getTeam()));
			p.addArmor();
			p.giveItems();
			p.setCanFly(false);
		}
		_state = GameState.IN_PROGRESS;
		_startTime = System.currentTimeMillis();
		_lastAction = _startTime;
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
			sendMessageToPlayers("&aThe next game will start soon");
		}
		new BukkitRunnable() {
			
			@Override
			public void run() {
				TacoWar.plugin.startNewGame();
			}
			
		}.runTaskLater(TacoWar.plugin, 20L * TacoWar.config.getNextGameWait());
	}
	
	public void determineMaxKills() {
		_maxKills = _players.size() / 2;
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
		Time runtime = new Time(getGameRunTime());
		String time = "";
		if(runtime.getHours() > 0)
			time += runtime.getHours() + ":";
		return time + runtime.getHours() + ":" + runtime.getSeconds();
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
	
	public enum GameState {
		IN_LOBBY, IN_PROGRESS, POST_GAME;
	}
	
}
