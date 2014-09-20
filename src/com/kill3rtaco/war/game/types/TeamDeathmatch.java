package com.kill3rtaco.war.game.types;

import com.kill3rtaco.war.game.GameType;

public class TeamDeathmatch extends GameType {

	public TeamDeathmatch() {
		super();
		_config.set(KEY_ID, "tdm");
		_config.set(KEY_NAME, "Team Deathmatch");
		_config.set(KEY_AUTHOR, "KILL3RTACO");
	}

}
