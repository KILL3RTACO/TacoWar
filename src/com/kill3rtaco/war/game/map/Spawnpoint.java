package com.kill3rtaco.war.game.map;

import org.bukkit.Location;

import com.kill3rtaco.war.game.GameType;

public class Spawnpoint {

	private WarMap _map;
	private String _id, _team;
	private Location _location;

	public Spawnpoint(WarMap map, String id, String team, Location location) {
		_map = map;
		_id = id;
		_team = team;
		_location = location;
	}

	public WarMap getMap() {
		return _map;
	}

	public String getId() {
		return _id;
	}

	public String getTeam() {
		return _team;
	}

	public Location getLocation() {
		return _location;
	}

	public boolean appliesTo(int gametype) {
		switch (gametype) {
			case GameType.FFA:
				return _team == null || _team.isEmpty();
			case GameType.TDM:
			case GameType.KOTH:
				return _team != null && !_team.isEmpty();
			default:
				return false;
		}
	}
}
