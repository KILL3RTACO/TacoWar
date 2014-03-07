package com.kill3rtaco.war.game.map;

import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Sound;

import com.kill3rtaco.tacoapi.TacoAPI;
import com.kill3rtaco.war.game.player.Player;

public class Teleporter {
	
	//cannot change map or name
	
	private Map			_map;
	private Location	_src;
	private String		_name, _channel;
	private boolean		_transmitter, _receiver;
	
	public Teleporter(Map map, String name, Location src, String channel, boolean transmitter, boolean receiver) {
		_src = src;
		_name = name;
		_channel = channel;
		_transmitter = transmitter;
		_receiver = receiver;
		_map = map;
	}
	
	public Teleporter(Map map, String name, Location src, String channel) {
		this(map, name, src, channel, true, true);
	}
	
	public Map getMap() {
		return _map;
	}
	
	public String getName() {
		return _name;
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
	
	public Location getTeleportLocation(Player player) {
		List<Teleporter> teleporters = _map.getReceiverTeleporters(_channel, _name);
		if(teleporters.isEmpty()) {
			player.sendMessage("&cThis teleporter has no receivers");
			return null;
		}
		Teleporter t = teleporters.get(new Random().nextInt(teleporters.size()));
		Location out = t.getSource().clone();
		Location playerLoc = player.getBukkitPlayer().getLocation();
		
		double o = out.getYaw();
		double p = playerLoc.getYaw();
		double s = _src.getYaw();
		
		float newYaw = (float) (o + (p - s) - 180);
		
		out.setPitch(playerLoc.getPitch());
		out.setYaw(newYaw);
		
		return out;
	}
	
	public void teleportPlayer(Player player) {
		teleportPlayer(player, getTeleportLocation(player));
	}
	
	public void teleportPlayer(Player player, Location location) {
		Location playerLoc = player.getBukkitPlayer().getLocation();
		
		//show smoke and play sound as an extra effect
		TacoAPI.getEffectAPI().showSmoke(playerLoc);
		player.teleport(location);
		TacoAPI.getEffectAPI().showSmoke(playerLoc);
		player.getBukkitPlayer().playSound(playerLoc, Sound.ENDERMAN_TELEPORT, 1, new Random().nextInt(5) + 1);
	}
	
	public String toString() {
		return "Teleporter{channel=" + _channel + ", transmitter=" + _transmitter + ", receiver=" + _receiver + ", src=" + _src + "}";
	}
}
