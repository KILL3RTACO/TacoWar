package com.kill3rtaco.war.game.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;

import com.kill3rtaco.war.game.player.WarPlayer;
import com.kill3rtaco.war.game.player.WarTeam;
import com.kill3rtaco.war.util.WarPlayerList;

public class Hill {
	
	public static final int	DELTA_UP	= 2;
	public static final int	DELTA_DOWN	= 1;
	
	private WarMap			_map;
	private String			_id;
	private Location		_location;
	private int				_radius;
	private WarPlayerList	_players;
	
	public Hill(WarMap map, String id, Location location, int radius) {
		_map = map;
		_id = id;
		_location = location;
		_radius = radius;
		_players = new WarPlayerList();
	}
	
	public WarMap getMap() {
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
	
	public WarPlayerList getPlayers() {
		return _players;
	}
	
	public boolean isContested() {
		WarTeam firstTeamFound = null;
		for (WarPlayer p : _players) {
			if (firstTeamFound == null) {
				firstTeamFound = p.getTeam();
				continue;
			}
			if (firstTeamFound != p.getTeam())
				return true;
		}
		return false;
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
	
	public WarTeam getControllingTeam() {
		final HashMap<WarTeam, Integer> teamDensity = getTeamDensities();
		List<WarTeam> teams = new ArrayList<WarTeam>(teamDensity.keySet());
		if (teams.isEmpty())
			return null;
		if (teams.size() > 1) {
			Collections.sort(teams, new Comparator<WarTeam>() {
				
				@Override
				public int compare(WarTeam t1, WarTeam t2) {
					return teamDensity.get(t2).compareTo(teamDensity.get(t1));
				}
				
			});
		}
		return teams.get(0);
	}
	
	public HashMap<WarTeam, Integer> getTeamDensities() {
		HashMap<WarTeam, Integer> teamDensity = new HashMap<WarTeam, Integer>();
		for (WarPlayer p : _players) {
			Integer density = teamDensity.get(p.getTeam());
			if (density == null)
				density = 0;
			teamDensity.put(p.getTeam(), density + 1);
		}
		return teamDensity;
	}
	
	public int getTeamDensity(WarTeam team) {
		return getTeamDensity(getTeamDensities(), team);
	}
	
	public int getTeamDensity(HashMap<WarTeam, Integer> densities, WarTeam team) {
		Integer density = getTeamDensities().get(team);
		if (density == null)
			return 0;
		return density;
	}
	
	public int getAdjustedTeamDensity(WarTeam team) {
		HashMap<WarTeam, Integer> teamDensity = getTeamDensities();
		int density = getTeamDensity(teamDensity, team);
		for (WarTeam t : teamDensity.keySet()) {
			if (t != team)
				density -= getTeamDensity(teamDensity, t);
		}
		return density;
	}
	
}
