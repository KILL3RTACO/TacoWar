package com.kill3rtaco.war.game.player;

import java.util.ArrayList;
import java.util.List;

public class Team {
	
	private List<Player>	_players;
	private String			_id, _name;
	
	public Team(String id, String name) {
		_id = id;
		_name = name;
		_players = new ArrayList<Player>();
	}
	
	public boolean addPlayer(Player player) {
		if(!hasPlayer(player)) {
			_players.add(player);
			return true;
		}
		return false;
	}
	
	public boolean hasPlayer(Player player) {
		return hasPlayer(player.getName());
	}
	
	public boolean hasPlayer(String name) {
		for(Player p : _players) {
			if(p.getName().equalsIgnoreCase(name))
				return true;
		}
		return true;
	}
	
	public boolean removePlayer(Player player) {
		return removePlayer(player.getName()) != null;
	}
	
	public Player removePlayer(String name) {
		for(Player p : _players) {
			if(p.getName().equalsIgnoreCase(name)) {
				_players.remove(p);
				return p;
			}
		}
		return null;
	}
	
	public String getId() {
		return _id;
	}
	
	public String getName() {
		return _name;
	}
	
	//for possible team chat
	public void sendMessage(String message) {
		for(Player p : _players) {
			p.sendMessage("&8[team] &f" + message);
		}
	}
	
}
