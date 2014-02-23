package com.kill3rtaco.war.game.map;

import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Sound;

import com.kill3rtaco.tacoapi.TacoAPI;
import com.kill3rtaco.war.TacoWar;
import com.kill3rtaco.war.game.player.Player;

public class Teleporter {
	
	private Map			_map;
	private Location	_src;
	private String		_channel;
	private boolean		_transmitter, _receiver;
	
	public Teleporter(Map map, Location src, String channel, boolean transmitter, boolean receiver) {
		_src = src;
		_channel = channel;
		_transmitter = transmitter;
		_receiver = receiver;
		_map = map;
	}
	
	public Teleporter(Map map, Location src, String channel) {
		this(map, src, channel, true, true);
	}
	
	public Map getMap() {
		return _map;
	}
	
	public Location getSource() {
		return _src;
	}
	
	public String getChannel() {
		return _channel;
	}
	
	public boolean isTransmitter() {
		return _transmitter;
	}
	
	public boolean isReceiver() {
		return _receiver;
	}
	
	public boolean isTwoWay() {
		return _transmitter && _receiver;
	}
	
	public void setSource(Location src) {
		_src = src;
	}
	
	public void setChannel(String channel) {
		_channel = channel;
	}
	
	public void setIsTransmitter(boolean transmitter) {
		_transmitter = transmitter;
	}
	
	public void setIsReceiver(boolean receiver) {
		_receiver = receiver;
	}
	
	public void teleportPlayer(Player player) {
		List<Teleporter> teleporters = _map.getReceiverTeleporters(_channel);
		Teleporter t = teleporters.get(new Random().nextInt(teleporters.size()));
		Location src = t.getSource();
		double x = src.getBlockX() + 0.5;
		double y = src.getBlockY();
		double z = src.getBlockZ() + 0.5;
		Location playerLoc = player.getBukkitPlayer().getLocation();
		//keep pitch/yaw intact. If the player goes in backwards they come out backwards
		Location loc = new Location(TacoWar.config.getWarWorld(), x, y, z, playerLoc.getYaw(), playerLoc.getPitch());
		
		//show smoke and play sound as an extra effect
		TacoAPI.getEffectAPI().showSmoke(playerLoc);
		player.teleport(loc);
		TacoAPI.getEffectAPI().showSmoke(playerLoc);
		player.getBukkitPlayer().playSound(playerLoc, Sound.ENDERMAN_TELEPORT, 0, 0);
	}
}
