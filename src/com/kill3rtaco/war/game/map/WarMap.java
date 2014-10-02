package com.kill3rtaco.war.game.map;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import com.kill3rtaco.war.TW;
import com.kill3rtaco.war.TacoWar;
import com.kill3rtaco.war.game.GameType;
import com.kill3rtaco.war.game.player.WarTeam;
import com.kill3rtaco.war.util.Identifyable;
import com.kill3rtaco.war.util.MapUtil;
import com.kill3rtaco.war.util.ValidatedConfig;

public class WarMap extends ValidatedConfig implements Identifyable {
	
	private String				_id, _name, _author, _timeName;
	private String				_messageGameStart,
								_messageLobbySpawn;
	private Location			_origin, _lobby;
	private File				_file				= null;
	private long				_timeTicks;
	private List<Teleporter>	_teleporters;
	private List<Spawnpoint>	_spawns;
	private List<Hill>			_hills;
	private List<Integer>		_supportedGameTypes;
	
	public static final String	KEY_AUTHOR			= "author";
	public static final String	KEY_HILLS			= "hills";
	public static final String	KEY_ID				= "id";
	public static final String	KEY_NAME			= "name";
	public static final String	KEY_ORIGIN			= "origin";
	public static final String	KEY_LOBBY			= "lobby_spawn";
	public static final String	KEY_READY			= "ready";
	public static final String	KEY_SPAWNPOINTS		= "spawnpoints";
	public static final String	KEY_TELEPORTERS		= "teleporters";
	public static final String	KEY_WORLD_TIME		= "time";
	public static final String	KEY_MAX_PLAYERS		= "max_players";		//-1 means no limit
	public static final String	KEY_MIN_PLAYERS		= "min_players";		//-1 means no limit
	public static final String	KEY_MSG_GAME_START	= "msg.game_start";
	public static final String	KEY_MSG_LOBBY_SPAWN	= "msg.lobby_spawn";
	public static final String	KEY_SIZE_X			= "size.x";
	public static final String	KEY_SIZE_Z			= "size.z";
	
	public WarMap(String id) {
		super(new YamlConfiguration());
		_id = id;
		_name = "";
		_author = "";
		_origin = null;
		_lobby = null;
//		_perms = new ArrayList<String>();
	}
	
	public WarMap(ConfigurationSection config) {
		super(config);
	}
	
	public void reload() {
		_id = getString(KEY_ID, true);
		_file = new File(TW.MAPS_FOLDER, _id + ".yml");
		if (_id != null && (_id.isEmpty() || _id.contains(" "))) {
			_valid = false;
		}
		_name = getString(KEY_NAME, true);
		_author = getString(KEY_AUTHOR, false);
		_origin = getLocation(KEY_ORIGIN, false, true);
		_lobby = getLocation(KEY_LOBBY, true, true);
		_supportedGameTypes = new ArrayList<Integer>();
		initSpawns();
		initTeleporters();
		initHills();
		setTime(_config.getString(KEY_WORLD_TIME, "day"));
	}
	
	public boolean isReady() {
		return _config.getBoolean(KEY_READY);
	}
	
	public boolean canHoldPlayers(int playerCount) {
		int min = getInt(KEY_MIN_PLAYERS, -1);
		int max = getInt(KEY_MAX_PLAYERS, -1);
		return playerCount > min && (max == -1 || playerCount <= max);
	}
	
	private void initSpawns() {
		_spawns = new ArrayList<Spawnpoint>();
		if (!_config.isConfigurationSection(KEY_SPAWNPOINTS)) {
			_valid = false; //can't play without spawns
			return;
		}
		
		for (String s : _config.getConfigurationSection(KEY_SPAWNPOINTS).getKeys(false)) {
			Spawnpoint sp = getSpawnpoint(s);
			if (sp == null)
				continue;
			_spawns.add(sp);
		}
		
		List<String> teamsFound = new ArrayList<String>();
		for (Spawnpoint s : _spawns) {
			if (s.appliesTo(GameType.FFA)) {
				_supportedGameTypes.add(GameType.FFA);
				continue;
			}
			String team = s.getTeam();
			if (!TW.validTeamId(team))
				continue;
			
			if (!teamsFound.contains(team))
				teamsFound.add(team);
		}
		
		if (teamsFound.size() >= 2)
			_supportedGameTypes.add(GameType.TDM);
	}
	
	private void initTeleporters() {
		_teleporters = new ArrayList<Teleporter>();
		if (!_config.isConfigurationSection(KEY_TELEPORTERS)) {
			return;
		}
		for (String s : _config.getConfigurationSection(KEY_TELEPORTERS).getKeys(false)) {
			Teleporter t = getTeleporter(s);
			if (t == null)
				continue;
			_teleporters.add(t);
		}
	}
	
	private void initHills() {
		_hills = new ArrayList<Hill>();
		if (!_config.isConfigurationSection(KEY_HILLS)) {
			return;
		}
		for (String s : _config.getConfigurationSection(KEY_HILLS).getKeys(false)) {
			Hill h = getHill(s);
			if (h == null)
				continue;
			_hills.add(h);
		}
		if (!_hills.isEmpty())
			_supportedGameTypes.add(GameType.KOTH);
	}
	
	private Teleporter getTeleporter(String name) {
		String root = KEY_TELEPORTERS + "." + name + ".";
		String channel = _config.getString(root + "channel", "default");
		boolean transmitter = _config.getBoolean(root + "transmitter", true);
		boolean receiver = _config.getBoolean(root + "receiver", true);
		Location src = getLocation(root + "src", true, false);
		
		if (src == null)
			return null;
		
		src.setY(src.getBlockY()); //remove the .5 from the y
		return new Teleporter(this, name, src, channel, transmitter, receiver);
	}
	
	private Spawnpoint getSpawnpoint(String name) {
		String root = KEY_SPAWNPOINTS + "." + name + ".";
		String team = _config.getString(root + "team", "");
		Location location = getLocation(root + "location", true, false);
		
		if (location == null)
			return null;
		
		return new Spawnpoint(this, name, team, location);
	}
	
	private Hill getHill(String name) {
		String root = KEY_HILLS + "." + name + ".";
		int radius = _config.getInt(root + "radius", 5);
		Location location = getLocation(root + "location", true, false);
		
		if (location == null)
			return null;
		
		return new Hill(this, name, location, radius);
	}
	
	public List<Hill> getHills() {
		return _hills;
	}
	
	private Location getLocation(String path, boolean relative, boolean req) {
		if (_config.isString(path)) {
			Location loc = MapUtil.getLocation(_config.getString(path));
			Location value;
			if (relative) {
				value = getPointRelative(loc);
			} else {
				value = loc;
			}
			if (value == null && req) {
				_valid = false;
			} else {
				return value;
			}
		} else if (req) {
			_valid = false;
		}
		return null;
	}
	
	public void setTime(String timeName) {
		if (timeName == null)
			timeName = "day";
		if (timeName.equalsIgnoreCase("dawn") || timeName.equalsIgnoreCase("sunrise")) {
			_timeTicks = 0;
		} else if (timeName.equalsIgnoreCase("day")) {
			_timeTicks = 1000;
		} else if (timeName.equalsIgnoreCase("midday") || timeName.equalsIgnoreCase("noon")) {
			_timeTicks = 6000;
		} else if (timeName.equalsIgnoreCase("dusk") || timeName.equalsIgnoreCase("sunset")) {
			_timeTicks = 1200;
		} else if (timeName.equalsIgnoreCase("night")) {
			_timeTicks = 14000;
		} else if (timeName.equalsIgnoreCase("midnight")) {
			_timeTicks = 18000;
		} else {
			setTime("day");
			return;
		}
		_timeName = timeName;
	}
	
	public boolean isValid() {
		return _valid;
	}
	
	public String getId() {
		return _id;
	}
	
	public String getName() {
		return _name;
	}
	
	public String getAuthor() {
		return _author != null ? _author : "Unknown";
	}
	
	public Location getPointRelative(Location loc) {
		if (_origin == null || loc == null) {
			return null;
		}
		double x = _origin.getX() + loc.getX() + .5; //add .5 to get center of block
		double y = _origin.getY() + loc.getY() + .5; //a little bit higher just in case
		double z = _origin.getZ() + loc.getZ() + .5;
		return new Location(TacoWar.config.getWarWorld(), x, y, z, loc.getYaw(), loc.getPitch());
	}
	
	public Location getPointRelative(String str) {
		return getPointRelative(MapUtil.getLocation(str));
	}
	
	public Location getOrigin() {
		return _origin;
	}
	
	public Location getLobbyLocation() {
		return _lobby;
	}
	
	public String toMessage() {
		return "&aMap&7: &3" + getName() + " &aby &b" + getAuthor();
	}
	
	public boolean isOriginSet() {
		return _origin != null;
	}
	
	public void setOrigin(Location loc) {
		_origin = loc;
		_config.set("origin", getLocationString(loc));
		save();
	}
	
	public void setLobbyLocationRelative(Location loc) {
		if (_origin == null) {
			return;
		}
		_lobby = loc.subtract(_origin);
	}
	
	public void save() {
		if (_file == null) {
			_file = new File(TacoWar.plugin.getDataFolder(), "maps/map_" + _id + "/map.yml");
		}
		save(_file);
	}
	
	public List<Location> getLocationList(List<String> list) {
		ArrayList<Location> locations = new ArrayList<Location>();
		for (String s : list) {
			Location loc = getPointRelative(s);
			if (loc != null) {
				locations.add(loc);
			}
		}
		return locations;
	}
	
	private String getLocationString(Location loc) {
		return loc.getBlockX() + " "
				+ loc.getBlockY() + " "
				+ loc.getBlockZ() + " "
				+ TW.getNearestDegree(loc.getYaw(), 45) + " "
				+ TW.getNearestDegree(loc.getPitch(), 45);
	}
	
//	public boolean addPerm(String perm) {
//		if (_perms.contains(perm))
//			return false;
//		_perms.add(perm);
//		return true;
//	}
//
//	public List<String> getPerms() {
//		return _perms;
//	}
//
//	public boolean hasPerm(String perm) {
//		return _perms.contains(perm);
//	}
	
	public String getTimeName() {
		return _timeName;
	}
	
	public long getTimeTicks() {
		return _timeTicks;
	}
	
	public String getGameStartMessage() {
		return _messageGameStart;
	}
	
	public String getLobbySpawnMessage() {
		return _messageLobbySpawn;
	}
	
	public List<Teleporter> getTeleporters() {
		return _teleporters;
	}
	
	public List<Teleporter> getTeleporters(String channel) {
		ArrayList<Teleporter> teleporters = new ArrayList<Teleporter>();
		for (Teleporter t : _teleporters) {
			if (t.getChannel().equals(channel)) {
				teleporters.add(t);
			}
		}
		return teleporters;
	}
	
	public List<Teleporter> getReceiverTeleporters(String channel) {
		ArrayList<Teleporter> teleporters = new ArrayList<Teleporter>();
		for (Teleporter t : getTeleporters(channel)) {
			if (t.isReceiver()) {
				teleporters.add(t);
			}
		}
		return teleporters;
	}
	
	//get recevier teleporters but exclude the teleporter with the given name
	public List<Teleporter> getReceiverTeleporters(String channel, String name) {
		List<Teleporter> teleporters = getReceiverTeleporters(channel);
		for (Teleporter t : teleporters) {
			if (t.getName().equals(name)) {
				teleporters.remove(t);
				break;
			}
		}
		return teleporters;
	}
	
	public List<Teleporter> getTransmitterTeleporters(String channel) {
		ArrayList<Teleporter> teleporters = new ArrayList<Teleporter>();
		for (Teleporter t : getTeleporters(channel)) {
			if (t.isTransmitter()) {
				teleporters.add(t);
			}
		}
		return teleporters;
	}
	
	public Teleporter getTeleporter(Location src) {
		for (Teleporter t : _teleporters) {
			Location tsrc = t.getSource();
			if (tsrc.getBlockX() == src.getBlockX() &&
					tsrc.getBlockY() == src.getBlockY() &&
					tsrc.getBlockZ() == src.getBlockZ()) {
				return t;
			}
		}
		return null;
	}
	
	public boolean gameTypeSupported(int gametype) {
//		System.out.println("[DEBUG] -> _supportedGametypes: " + _supportedGameTypes);
//		System.out.println("[DEBUG] -> gameTypeSupported? " + gametype + ": " + _supportedGameTypes.contains(gametype));
		return _supportedGameTypes.contains(gametype);
	}
	
	public boolean allGameTypesSupported() {
		return gameTypeSupported(GameType.FFA) && gameTypeSupported(GameType.TDM) && gameTypeSupported(GameType.KOTH);
	}
	
	public Location getRandomSpawn(String team) {
		if (!TacoWar.currentGame().getGameType().getConfig().getBoolean(GameType.KEY_TEAMS_ENABLED))
			team = ""; //hack, players in ffa have a team id of their own name
		ArrayList<Spawnpoint> locs = new ArrayList<Spawnpoint>();
		for (Spawnpoint s : _spawns) {
			if (s.appliesTo(TacoWar.currentGame().getGameType().getType(), team))
				locs.add(s);
		}
		if (locs.isEmpty())
			return null;
		if (locs.size() == 1)
			return locs.get(0).getLocation();
		return locs.get(new Random().nextInt(locs.size())).getLocation();
	}
	
	public Location getRandomSpawn(WarTeam team) {
		return getRandomSpawn(team.getId());
	}
	
	public List<Spawnpoint> getSpawns() {
		return _spawns;
	}
	
	//for gametypes with TEAMS_ENABLED set to true
	public List<WarTeam> getTeamsFromSpawnpoints() {
		List<WarTeam> teams = new ArrayList<WarTeam>();
		List<String> added = new ArrayList<String>();
		for (Spawnpoint s : _spawns) {
			String teamId = s.getTeam();
			if (teamId.isEmpty() || added.contains(teamId))
				continue;
			
			WarTeam team = TW.createTeam(teamId);
			if (team == null)
				continue;
			teams.add(team);
			added.add(teamId);
		}
		return teams;
	}
	
	//the origin should be at a corner
	public void launchCelebratoryRocket() {
		int deltaX = getInt(KEY_SIZE_X, 0);
		int deltaZ = getInt(KEY_SIZE_Z, 0);
		
		Random random = new Random();
		int x = random.nextInt(deltaX + 1);
		int z = random.nextInt(deltaZ + 1);
		
		Location partyLocation = new Location(_origin.getWorld(), _origin.getX() + x, 0, _origin.getZ() + z);
		partyLocation = MapUtil.getHighestBlock(partyLocation);
		
		Firework fw = _origin.getWorld().spawn(partyLocation, Firework.class);
		FireworkMeta meta = fw.getFireworkMeta();
		meta.setPower(random.nextInt(2) + 2); //2-3
		meta.addEffect(TW.randomFireworkEffect());
		
	}
}
