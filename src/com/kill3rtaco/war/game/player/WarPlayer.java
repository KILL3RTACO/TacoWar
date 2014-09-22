package com.kill3rtaco.war.game.player;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.kill3rtaco.war.TacoWar;

public class WarPlayer {

	private String	_name;
	private WarTeam	_team;
	private WarKit	_kit;
	private boolean	_invincible	= false;

	public WarPlayer(String name) {
		_name = name;
	}

	public String getName() {
		return _name;
	}

	public WarKit getKit() {
		return _kit;
	}

	public void setInvicible(boolean invincible) {
		_invincible = invincible;
	}

	public boolean isInvincible() {
		return _invincible;
	}

	public void giveKit() {
		Player p = getBukkitPlayer();
		if (p != null) {

		}
	}

	public String getColorfulName() {
		return _team.getColor() + getName();
	}

	public Player getBukkitPlayer() {
		return Bukkit.getPlayer(_name);
	}

	public void setTeam(WarTeam team) {
		if (_team != null)
			_team.removePlayer(this);
		_team = team;
		team.addPlayer(this);
	}

	public WarTeam getTeam() {
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

	public Weapon getHeldWeapon() {
		Player p = getBukkitPlayer();
		if (p != null)
			return _kit.getWeapon(p.getInventory().getHeldItemSlot());
		else
			return null;
	}

	public void updateAmmoCount() {
		Player p = getBukkitPlayer();
		Weapon w = getHeldWeapon();
		if (p != null)
			p.setLevel(w.getAmmo());
	}

}
