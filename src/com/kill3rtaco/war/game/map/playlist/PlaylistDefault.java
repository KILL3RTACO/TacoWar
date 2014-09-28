package com.kill3rtaco.war.game.map.playlist;

import org.bukkit.configuration.file.YamlConfiguration;

import com.kill3rtaco.war.TacoWar;
import com.kill3rtaco.war.game.map.Playlist;
import com.kill3rtaco.war.game.map.PlaylistEntry;
import com.kill3rtaco.war.game.map.WarMap;

public class PlaylistDefault extends Playlist {
	
	public static final String	ID	= "default";
	
	public PlaylistDefault() {
		super(new YamlConfiguration());
		_valid = true;
	}
	
	public void reload() {
		for (WarMap m : TacoWar.getMaps()) {
			_maps.add(new PlaylistEntry(m));
		}
		_genericGameTypes = TacoWar.getGameTypes();
		_genericKits = TacoWar.getKits();
	}
	
	public void save() {}
	
}
