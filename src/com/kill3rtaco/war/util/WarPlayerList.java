package com.kill3rtaco.war.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import com.kill3rtaco.war.TW;
import com.kill3rtaco.war.game.player.WarPlayer;
import com.kill3rtaco.war.game.player.WarPlayer.PlayMode;
import com.kill3rtaco.war.game.player.kit.WarKit;

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
		return TW.hasPlayer(_players, name);
	}
	
	public void setScoreboard(Scoreboard scoreboard) {
		for (WarPlayer p : _players) {
			Player bukkitPlayer = p.getBukkitPlayer();
			if (p != null)
				bukkitPlayer.setScoreboard(scoreboard);
		}
	}
	
	public void setKits(WarKit kit) {
		for (WarPlayer p : _players) {
			p.setKit(kit);
		}
	}
	
	public void broadcast(String message) {
		TW.broadcast(_players, message);
	}
	
	public void teleportAll(Location location) {
		TW.teleport(_players, location);
	}
	
	public void respawnAll() {
		TW.respawn(_players);
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
		return TW.removePlayer(_players, name);
	}
	
	public WarPlayer get(Player player) {
		if (player == null)
			return null;
		return get(player.getName());
	}
	
	public WarPlayer get(String name) {
		return TW.getPlayer(_players, name);
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
	
	public void setPlayMode(PlayMode mode) {
		for (WarPlayer p : _players) {
			p.setPlayMode(mode);
		}
	}
}
