package com.kill3rtaco.war.game.types;

import com.kill3rtaco.war.game.GameType;

public class FreeForAll extends GameType {

	public FreeForAll() {
		super();
		_config.set(KEY_ID, "ffa");
		_config.set(KEY_NAME, "Free for All");
		_config.set(KEY_AUTHOR, "KILL3RTACO");
		_config.set(KEY_TEAMS_ENABLED, false);
	}

}
