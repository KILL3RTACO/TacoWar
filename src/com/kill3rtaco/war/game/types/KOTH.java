package com.kill3rtaco.war.game.types;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import com.kill3rtaco.war.TacoWar;
import com.kill3rtaco.war.game.GameType;
import com.kill3rtaco.war.game.map.Hill;
import com.kill3rtaco.war.game.player.WarPlayer;

public class KOTH extends GameType {

	public static final String	ID							= "koth";

	public static final int		SCORE_POLICY_TEAM			= 1;					//team gains (1 * mult) every second
	public static final int		SCORE_POLICY_PLAYER			= 2;					//team gains (num_players * mult) every second
	public static final int		SCORE_POLICY_TOTAL_PLAYERS	= 3;					//same as above, but when contested the gained score is reduced

	public static final String	KEY_SCORE_MULTIPLIER		= "score_multiplier";
	public static final String	KEY_SCORE_POLICY			= "score_policy";

	public static final int		DEF_SCORE_MULTIPLIER		= 1;
	public static final String	DEF_SCORE_POLICY			= "team";

	public KOTH() {
		super();
		set(KEY_ID, ID);
		set(KEY_NAME, "King of the Hill");
		set(KEY_AUTHOR, "KILL3RTACO");
		set(KEY_SCORE_MULTIPLIER, DEF_SCORE_MULTIPLIER);
		set(KEY_SCORE_POLICY, DEF_SCORE_POLICY);
		_type = GameType.KOTH;
	}

	public KOTH(ConfigurationSection config) {
		this();
		_config = config;
	}

	@Override
	public boolean onKill() {
		return false;
	}

	@Override
	public void onMove(WarPlayer player, Location from, Location to) {
		List<Hill> hills = TacoWar.currentGame().getMap().getHills();
		for (Hill h : hills) {
			if (!h.isInside(from) && h.isInside(to)) { //onEnter
				h.addPlayer(player);
			} else if (h.isInside(from) && !h.isInside(to)) { //onExit
				h.removePlayer(player);
			}
		}

	}

}
