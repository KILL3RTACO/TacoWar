package com.kill3rtaco.war.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.kill3rtaco.war.game.player.WarPlayer;

public class WarPlayerList implements Iterable<WarPlayer> {

	protected List<WarPlayer>	_players;

	public WarPlayerList() {
		this(new ArrayList<WarPlayer>());
	}

	public WarPlayerList(List<WarPlayer> players) {
		_players = players;
	}

	public List<WarPlayer> getPlayers() {
		return _players;
	}

	public boolean contains(Player p) {
		return contains(p.getName());
	}

	public boolean contains(WarPlayer player) {
		return contains(player.getName());
	}

	public boolean contains(String name) {
		return WarUtil.hasPlayer(_players, name);
	}

	public void broadcast(String message) {
		WarUtil.broadcast(_players, message);
	}

	public void teleportAll(Location location) {
		WarUtil.teleport(_players, location);
	}

	public void respawnAll() {
		WarUtil.respawn(_players);
	}

	public void add(WarPlayer player) {
		if (!contains(player.getName()))
			_players.add(player);
	}

	public WarPlayer remove(Player p) {
		return remove(p.getName());
	}

	public WarPlayer remove(WarPlayer player) {
		return remove(player.getName());
	}

	public WarPlayer remove(String name) {
		return WarUtil.removePlayer(_players, name);
	}

	public WarPlayer get(Player player) {
		if (player == null)
			return null;
		return get(player.getName());
	}

	public WarPlayer get(String name) {
		return WarUtil.getPlayer(_players, name);
	}

	public WarPlayer get(int index) {
		return _players.get(index);
	}

	public int size() {
		return _players.size();
	}

	@Override
	public Iterator<WarPlayer> iterator() {
		return _players.iterator();
	}

}
