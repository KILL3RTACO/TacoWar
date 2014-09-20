package com.kill3rtaco.war.game.map;

import org.bukkit.Location;

public class Hill {

	public static final int DELTA_UP = 2;
	public static final int DELTA_DOWN = 1;

	private Map _map;
	private String _id;
	private Location _location;
	private int _radius;

	public Hill(Map map, String id, Location location, int radius) {
		_map = map;
		_id = id;
		_location = location;
		_radius = radius;
	}

	public Map getMap() {
		return _map;
	}

	public String getId() {
		return _id;
	}

	public Location getLocation() {
		return _location;
	}

	public int getRadius() {
		return _radius;
	}

	public boolean isInside(Location loc) {
		Location distanceTest = loc.clone();
		distanceTest.setY(_location.getBlockY());
		double xzDistance = distanceTest.distanceSquared(_location);

		if (xzDistance > _radius * _radius)
			return false;

		if (loc.getBlockY() > _location.getBlockY() + DELTA_UP
				|| loc.getBlockY() < _location.getBlockY() - DELTA_DOWN)
			return false;

		return true;

	}

}
