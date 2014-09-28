package com.kill3rtaco.war.util;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import com.kill3rtaco.tacoapi.TacoAPI;
import com.kill3rtaco.war.game.player.WarTeam;

/**
 * A wrapper for org.bukkit.scoreboard.Scoreboard
 * 
 * @author taco
 *
 */
public class CustomScoreboard {
	
	private Scoreboard			_sb;
	private Objective			_objective;
	private Map<String, String>	_teams;
	
	public CustomScoreboard(String objective, String displayName) {
		_teams = new HashMap<String, String>();
		_sb = Bukkit.getScoreboardManager().getNewScoreboard();
		_objective = _sb.registerNewObjective(objective, "dummy");
		_objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		_objective.setDisplayName(TacoAPI.getChatUtils().formatMessage(displayName));
	}
	
	public void addTeam(WarTeam team) {
		addTeam(team.getId(), team.getName());
	}
	
	public void addTeam(String id, String name) {
		_teams.put(id, name);
		_sb.registerNewTeam(name);
	}
	
	private Score getScoreboardScore(String team) {
		return _objective.getScore(Bukkit.getOfflinePlayer(team));
	}
	
	public void setScore(String teamId, int points) {
		if (!_teams.containsKey(teamId))
			throw new IllegalArgumentException(teamId + " is has not been registered");
		getScoreboardScore(_teams.get(teamId)).setScore(points);
	}
	
	public void setScoreRelative(String teamId, int points) {
		int score = getScore(teamId);
		getScoreboardScore(_teams.get(teamId)).setScore(score + points);
	}
	
	public int getScore(String teamId) {
		if (!_teams.containsKey(teamId))
			throw new IllegalArgumentException(teamId + " is has not been registered");
		return getScoreboardScore(_teams.get(teamId)).getScore();
	}
	
	public Scoreboard getBukkitScoreboard() {
		return _sb;
	}
	
}
