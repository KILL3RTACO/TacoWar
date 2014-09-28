package com.kill3rtaco.war.game.player;

import com.kill3rtaco.war.util.WarPlayerList;

public class WarTeam extends WarPlayerList {
	
	private String	_id, _name;
	private int		_score;
	
	public WarTeam(String id, String name) {
		this(id, name, 0);
	}
	
	public WarTeam(String id, String name, int startPoints) {
		_id = id;
		_name = name;
		_score = startPoints;
	}
	
	public String getId() {
		return _id;
	}
	
	public String getName() {
		return _name;
	}
	
	public String getColor() {
		if (_name.matches("\\&[0-9a-f].+")) //color code and at least on letter
			return _name.substring(0, 2);
		return "&f";
	}
	
	public void setScore(int points) {
		_score = points;
	}
	
	public void setScoreRelative(int points) {
		_score += points;
	}
	
	public int getScore() {
		return _score;
	}
	
}
