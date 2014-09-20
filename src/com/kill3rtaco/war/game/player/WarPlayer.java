package com.kill3rtaco.war.game.player;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.kill3rtaco.war.TacoWar;

public class WarPlayer {

	private String _name;
	private Team _team;
	private Kit _kit;

	public WarPlayer(String name) {
		_name = name;
	}

	public String getName() {
		return _name;
	}

	public Kit getKit() {
		return _kit;
	}

	public void giveKit() {
		Player p = getBukkitPlayer();
		if (p != null) {
			p.getInventory().setContents(_kit.getItems());
			//give colored armor and apply enchants, if any
		}
	}

	public String getColorfulName() {
		return _team.getColor() + getName();
	}

	public Player getBukkitPlayer() {
		return Bukkit.getPlayer(_name);
	}

	public void setTeam(Team team) {
		if (_team != null)
			_team.removePlayer(this);
		_team = team;
		team.addPlayer(this);
	}

	public Team getTeam() {
		return _team;
	}

	public boolean sameTeam(WarPlayer player) {
		return player == this || player.getTeam().hasPlayer(this);
	}

	public void sendMessage(String message) {
		Player p = getBukkitPlayer();
		if (p != null)
			TacoWar.chat.sendPlayerMessage(p, message);
	}

	public void respawn() {
		//teleport at base
		//clear inventory
		//give kit
	}

	public void teleport(Location location) {
		Player p = getBukkitPlayer();
		if (p != null)
			p.teleport(location);
	}

	public void clearInventory() {
		Player p = getBukkitPlayer();
		if (p != null)
			p.getInventory().clear();
	}

}
