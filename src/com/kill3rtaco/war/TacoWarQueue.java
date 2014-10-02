package com.kill3rtaco.war;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.kill3rtaco.war.game.player.WarPlayer;
import com.kill3rtaco.war.util.WarPlayerList;

//did not use a queue class because they are all confusing
public class TacoWarQueue {
	
	private static List<String>	_queue	= new ArrayList<String>();
	
	public static int playersOnline() {
		int count = 0;
		for (String s : _queue) {
			if (!isOnline(s))
				continue;
			
			count++;
		}
		return count;
	}
	
	public static void addPlayer(String name) {
		_queue.add(name);
	}
	
	public static String removeFirstOnline() {
		String firstOnline = firstOnline();
		if (firstOnline == null)
			return null;
		return remove(firstOnline);
	}
	
	public static String firstOnline() {
		if (_queue.isEmpty())
			return null;
		for (String s : _queue) {
			if (!isOnline(s))
				continue;
			return s;
		}
		return null;
	}
	
	public static WarPlayer removeFirstOnlineAsPlayer() {
		if (playersOnline() == 0)
			return null;
		String name = removeFirstOnline();
		if (name == null)
			return null;
		return new WarPlayer(name);
	}
	
	public static WarPlayerList removeOnlineAsPlayers() {
		WarPlayerList players = new WarPlayerList();
		for (String s : _queue) {
			players.add(new WarPlayer(s));
		}
		return players;
	}
	
	private static boolean isOnline(String name) {
		Player p = Bukkit.getPlayer(name);
		return p != null && p.isOnline();
	}
	
	public static String remove(String str) {
		for (int i = 0; i < _queue.size(); i++) {
			if (_queue.get(i).equalsIgnoreCase(str)) {
				return _queue.remove(i);
			}
		}
		return null;
	}
	
	public static boolean contains(String str) {
		for (String s : _queue) {
			if (s.equalsIgnoreCase(str))
				return true;
		}
		return false;
	}
	
	//returns whether they are in the queue anymore
	public static boolean toggle(String name) {
		if (contains(name)) {
			remove(name);
			return false;
		} else {
			addPlayer(name);;
			return true;
		}
	}
	
}
