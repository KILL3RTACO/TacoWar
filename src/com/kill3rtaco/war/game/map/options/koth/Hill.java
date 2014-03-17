package com.kill3rtaco.war.game.map.options.koth;

import org.bukkit.Location;

public class Hill {
	
	private Location	_loc;
	private int			_radius;
	
	public Hill(Location loc, int radius) {
		_loc = loc;
		_radius = radius;
	}
	
	public Location location() {
		return _loc;
	}
	
	public int radius() {
		return _radius;
	}
	
}
