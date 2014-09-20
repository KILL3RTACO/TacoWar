package com.kill3rtaco.war.game.types;

import com.kill3rtaco.war.game.GameType;

public class KingOfTheHill extends GameType {

	public KingOfTheHill() {
		super();
		_config.set(KEY_ID, "koth");
		_config.set(KEY_NAME, "King of the Hill");
		_config.set(KEY_AUTHOR, "KILL3RTACO");
	}

}
