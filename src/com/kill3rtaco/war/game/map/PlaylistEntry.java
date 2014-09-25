package com.kill3rtaco.war.game.map;

import java.util.ArrayList;
import java.util.List;

import com.kill3rtaco.war.game.GameType;
import com.kill3rtaco.war.game.player.WarKit;

public class PlaylistEntry {

	private WarMap			_map;
	private List<GameType>	_gametypes;
	private List<WarKit>	_kits;

	public PlaylistEntry(WarMap map) {
		_map = map;
		_gametypes = new ArrayList<GameType>();
		_kits = new ArrayList<WarKit>();
	}

	public WarMap getMap() {
		return _map;
	}

	public List<GameType> getGameTypes() {
		return _gametypes;
	}

	public List<WarKit> getKits() {
		return _kits;
	}

	public boolean equals(PlaylistEntry entry) {
		return _map == entry.getMap();
	}

}
