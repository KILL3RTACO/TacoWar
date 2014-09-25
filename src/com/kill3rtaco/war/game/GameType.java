package com.kill3rtaco.war.game;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import com.kill3rtaco.war.Identifyable;
import com.kill3rtaco.war.ValidatedConfig;
import com.kill3rtaco.war.game.player.WarPlayer;
import com.kill3rtaco.war.game.types.FFA;
import com.kill3rtaco.war.game.types.KOTH;
import com.kill3rtaco.war.game.types.TDM;

public abstract class GameType extends ValidatedConfig implements Identifyable {

	public static final int			TDM							= 1;
	public static final int			FFA							= 1 << 1;
	public static final int			KOTH						= 1 << 2;
//	public static final int HAS = 1 << 3;
//	public static final int INF = 1 << 4;
	public static final String		KEY_AUTHOR					= "author";

	public static final String		KEY_BASE_TYPE				= "gametype";
	public static final String		KEY_FORCE_KIT				= "force_kit";
	public static final String		KEY_ID						= "id";
	public static final String		KEY_MAX_HEALTH				= "player.max_health";		//half-hearts
	public static final String		KEY_MAX_SCORE				= "max_score";
	public static final String		KEY_MVP_KILL_BONUS			= "kill_bonus.mvp";
	public static final String		KEY_NAME					= "name";
	public static final String		KEY_PENALTY_FRIENDLY_FIRE	= "penalty.friendly_fire";	//-1 is off; 0 is on, no pen; 1 is on, with pren
	public static final String		KEY_PENALTY_SUICIDE			= "penalty.suicide";		//0 means can't harm self, otherwise abs()
	public static final String		KEY_PLAYER_SPEED			= "player.speed";			//percent
	public static final String		KEY_TEAMS_ENABLED			= "teams_enabled";
	public static final String		KEY_TIME_LIMIT				= "time_limit";

	public static final int			DEF_PENALTY_FRIENDLY_FIRE	= -1;
	public static final int			DEF_PENALTY_SUICIDE			= 0;
	public static final int			DEF_PLAYER_SPEED			= 100;
	public static final int			DEF_MAX_HEALTH				= 20;
	public static final int			DEF_TIME_LIMIT				= 0;
	public static final boolean		DEF_TEAMS_ENABLED			= true;

	public static final GameType	freeForAll					= new FFA();
	public static final GameType	teamDeathmatch				= new TDM();
	public static final GameType	kingOfTheHill				= new KOTH();

	protected int					_type						= 0;

	public GameType() {
		this(getDefaults());
	}

	public GameType(ConfigurationSection config) {
		super(config);
	}

	public String getId() {
		return _config.getString(KEY_ID);
	}

	public void reload() {}

	public abstract boolean onKill();

	public abstract void onMove(WarPlayer player, Location from, Location to);

	public abstract int getType();

	public static int getGameType(String id) {
		if (id.equalsIgnoreCase("ffa"))
			return FFA;
		if (id.equalsIgnoreCase("tdm"))
			return TDM;
		if (id.equalsIgnoreCase("koth"))
			return KOTH;
		return 0;
	}

	public static ConfigurationSection getDefaults() {
		YamlConfiguration config = new YamlConfiguration();
		config.set(KEY_PENALTY_FRIENDLY_FIRE, DEF_PENALTY_FRIENDLY_FIRE);
		config.set(KEY_PENALTY_SUICIDE, DEF_PENALTY_SUICIDE);
		config.set(KEY_PLAYER_SPEED, DEF_PLAYER_SPEED);
		config.set(KEY_MAX_HEALTH, DEF_MAX_HEALTH);
		config.set(KEY_TIME_LIMIT, DEF_TIME_LIMIT);
		config.set(KEY_TEAMS_ENABLED, DEF_TEAMS_ENABLED);
		return config;
	}

	public static Integer[] randomOrderPreference() {
		List<Integer> types = Arrays.asList(TDM, FFA, KOTH);
		Collections.shuffle(types);
		return types.toArray(new Integer[] {});
	}

}
