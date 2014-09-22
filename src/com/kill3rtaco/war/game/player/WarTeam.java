package com.kill3rtaco.war.game.player;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.kill3rtaco.war.util.WarUtil;

public class WarTeam {

	private String					_id, _name;
	private ArrayList<WarPlayer>	_players	= new ArrayList<WarPlayer>();

	public WarTeam(String id, String name) {
		_id = id;
		_name = name;
	}

	public String getId() {
		return _id;
	}

	public String getName() {
		return _name;
	}

	public void addPlayer(WarPlayer player) {
		if (!hasPlayer(player.getName()))
			_players.add(player);
	}

	public void removePlayer(Player p) {
		removePlayer(p.getName());
	}

	public void removePlayer(WarPlayer player) {
		removePlayer(player.getName());
	}

	public void removePlayer(String name) {
		WarUtil.removePlayer(_players, name);
	}

	public String getColor() {
		if (_name.matches("\\&[0-9a-f].+")) //color code and at least on letter
			return _name.substring(0, 2);
		return "&f";
	}

	public List<WarPlayer> getPlayers() {
		return _players;
	}

	public boolean hasPlayer(Player p) {
		return hasPlayer(p.getName());
	}

	public boolean hasPlayer(WarPlayer player) {
		return hasPlayer(player.getName());
	}

	public boolean hasPlayer(String name) {
		return WarUtil.hasPlayer(_players, name);
	}

	public void broadcast(String message) {
		WarUtil.broadcast(_players, message);
	}

}
