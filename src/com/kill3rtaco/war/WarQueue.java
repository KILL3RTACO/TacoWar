package com.kill3rtaco.war;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.kill3rtaco.war.game.player.WarPlayer;

//did not use a queue class because they are all confusing
public class WarQueue {

	private List<String>	_queue	= new ArrayList<String>();

	public int playersOnline() {
		int count = 0;
		for (String s : _queue) {
			Player p = Bukkit.getPlayer(s);
			if (p == null || !p.isOnline())
				continue;

			count++;
		}
		return count;
	}

	public void addPlayer(String name) {
		_queue.add(name);
	}

	public String removeFirstOnline() {
		String firstOnline = firstOnline();
		if (firstOnline == null)
			return null;
		return remove(firstOnline);
	}

	public String firstOnline() {
		if (_queue.isEmpty())
			return null;
		for (int i = 0; i < _queue.size(); i++) {
			String s = _queue.get(i);
			Player p = Bukkit.getPlayer(s);
			if (p == null || !p.isOnline())
				continue;
			return _queue.get(i);
		}
		return null;
	}

	public WarPlayer removeFirstOnlineAsPlayer() {
		if (playersOnline() == 0)
			return null;
		String name = removeFirstOnline();
		if (name == null)
			return null;
		return new WarPlayer(name);
	}

	public String remove(String str) {
		for (int i = 0; i < _queue.size(); i++) {
			if (_queue.get(i).equalsIgnoreCase(str)) {
				return _queue.remove(i);
			}
		}
		return null;
	}

	public boolean contains(String str) {
		for (String s : _queue) {
			if (s.equalsIgnoreCase(str))
				return true;
		}
		return false;
	}

}
