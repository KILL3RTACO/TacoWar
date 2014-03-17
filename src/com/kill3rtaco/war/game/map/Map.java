package com.kill3rtaco.war.game.map;

import static com.kill3rtaco.war.TWConstants.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import com.kill3rtaco.war.TacoWar;
import com.kill3rtaco.war.ValidatedConfig;
import com.kill3rtaco.war.game.GameType;
import com.kill3rtaco.war.game.map.options.FFAMapOptions;
import com.kill3rtaco.war.game.map.options.HideAndSeekMapOptions;
import com.kill3rtaco.war.game.map.options.InfectionMapOptions;
import com.kill3rtaco.war.game.map.options.JuggernautMapOptions;
import com.kill3rtaco.war.game.map.options.KOTHMapOptions;
import com.kill3rtaco.war.game.map.options.TDMMapOptions;
import com.kill3rtaco.war.game.player.TeamColor;

public class Map extends ValidatedConfig {
	
	private String								_id, _name, _author, _timeName;
	private String								_messageGameStart,
												_messageLobbySpawn;
	private Location							_origin, _lobby;
	private File								_file	= null;
	private List<String>						_perms;
	private long								_timeTicks;
	private List<Teleporter>					_teleporters;
	private List<GameType>						_supported;
	private HashMap<TeamColor, List<Location>>	_spawns;
	
	//map options
	private FFAMapOptions						_ffaOptions;
	private HideAndSeekMapOptions				_hasOptions;
	private InfectionMapOptions					_infOptions;
	private JuggernautMapOptions				_jugOptions;
	private KOTHMapOptions						_kothOptions;
	private TDMMapOptions						_tdmOptions;
	
	public Map(String id) {
		super(new YamlConfiguration());
		_id = id;
		_name = "";
		_author = "";
		_origin = null;
		_lobby = null;
		_perms = new ArrayList<String>();
		_supported = new ArrayList<GameType>();
	}
	
	public Map(File file) {
		super(YamlConfiguration.loadConfiguration(file));
		_supported = new ArrayList<GameType>();
		_file = file;
		_id = getString(M_ID, true);
		if(_id != null && (_id.isEmpty() || _id.contains(" "))) {
			_valid = false;
		}
		_name = getString(M_NAME, true);
		_author = getString(M_AUTHOR, false);
		_origin = getLocation(M_ORIGIN, false, true);
		_lobby = getLocation(M_LOBBY, true, true);
		_spawns = new HashMap<TeamColor, List<Location>>();
		for(String s : section.getConfigurationSection("team_spawns").getKeys(false)) {
			TeamColor c = TeamColor.getTeamColor(s);
			if(c == null) {
				continue;
			}
			List<Location> locs = map.getLocationList(section.getStringList("team_spawns." + s));
			if(!locs.isEmpty()) {
				_spawns.put(c, locs);
			}
		}
		if(_spawns.size() < 2) {
			_valid = false;
		}
		initMapOptions();
		_teleporters = new ArrayList<Teleporter>();
		for(String s : _config.getConfigurationSection(M_TELEPORTERS).getKeys(false)) {
			Teleporter t = getTeleporter(s);
			if(t != null && getTeleporter(t.getSource()) == null) {
				_teleporters.add(t);
			}
		}
		_perms = new ArrayList<String>(_config.getStringList(M_PERMS));
		_timeName = _config.getString(M_WORLD_TIME, "day");
		_messageGameStart = getString(M_M_GAME_START, false);
		_messageLobbySpawn = getString(M_M_LOBBY_SPAWN, false);
		setTime(_config.getString(M_WORLD_TIME, "day"));
	}
	
	private void initMapOptions() {
		if(_config.isConfigurationSection("ffa")) {
			_ffaOptions = new FFAMapOptions(this, _config.getConfigurationSection("ffa"));
			if(_ffaOptions.isValid())
				_supported.add(GameType.FFA);
		}
		if(_config.isConfigurationSection("hide_and_seek")) {
			_hasOptions = new HideAndSeekMapOptions(this, _config.getConfigurationSection("hide_and_seek"));
			if(_hasOptions.isValid())
				_supported.add(GameType.HIDE_AND_SEEK);
		}
		if(_config.isConfigurationSection("infection")) {
			_infOptions = new InfectionMapOptions(this, _config.getConfigurationSection("infection"));
			if(_infOptions.isValid())
				_supported.add(GameType.INFECTION);
		}
		if(_config.isConfigurationSection("juggernaut")) {
			_jugOptions = new JuggernautMapOptions(this, _config.getConfigurationSection("juggernaut"));
		}
		if(_config.isConfigurationSection("koth")) {
			_kothOptions = new KOTHMapOptions(this, _config.getConfigurationSection("koth"));
		}
		if(_config.isConfigurationSection("tdm")) {
			_tdmOptions = new TDMMapOptions(this, _config.getConfigurationSection("tdm"));
			if(_tdmOptions.isValid())
				_supported.add(GameType.TDM);
		}
	}
	
	private Teleporter getTeleporter(String name) {
		String root = M_TELEPORTERS + "." + name + ".";
		String channel = _config.getString(root + "channel", "default");
		boolean transmitter = _config.getBoolean(root + "transmitter", true);
		boolean receiver = _config.getBoolean(root + "receiver", true);
		Location src = getLocation(root + "src", true, true);
		if(src == null) {
			return null;
		} else {
			src.setY(src.getBlockY()); //remove the .5 from the y
			return new Teleporter(this, name, src, channel, transmitter, receiver);
		}
	}
	
	private Location getLocation(String path, boolean relative, boolean req) {
		if(_config.isString(path)) {
			Location loc = getPoint(_config.getString(path));
			Location value;
			if(relative) {
				value = getPointRelative(loc);
			} else {
				value = loc;
			}
			if(value == null && req) {
				_valid = false;
			} else {
				return value;
			}
		} else if(req) {
			_valid = false;
		}
		return null;
	}
	
	public void setTime(String timeName) {
		if(timeName == null)
			timeName = "day";
		if(timeName.equalsIgnoreCase("dawn") || _timeName.equalsIgnoreCase("sunrise")) {
			_timeTicks = 0;
		} else if(_timeName.equalsIgnoreCase("day")) {
			_timeTicks = 1000;
		} else if(_timeName.equalsIgnoreCase("midday") || _timeName.equalsIgnoreCase("noon")) {
			_timeTicks = 6000;
		} else if(_timeName.equalsIgnoreCase("dusk") || _timeName.equalsIgnoreCase("sunset")) {
			_timeTicks = 1200;
		} else if(_timeName.equalsIgnoreCase("night")) {
			_timeTicks = 14000;
		} else if(_timeName.equalsIgnoreCase("midnight")) {
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
		if(_origin == null || loc == null) {
			return null;
		}
		double x = _origin.getX() + loc.getX() + .5; //add .5 to get center of block
		double y = _origin.getY() + loc.getY() + .5; //a little bit higher just in case
		double z = _origin.getZ() + loc.getZ() + .5;
		return new Location(TacoWar.config.getWarWorld(), x, y, z, loc.getYaw(), loc.getPitch());
	}
	
	public Location getPointRelative(String str) {
		return getPointRelative(getPoint(str));
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
		if(_origin == null) {
			return;
		}
		_lobby = loc.subtract(_origin);
	}
	
	public void save() {
		if(_file == null) {
			_file = new File(TacoWar.plugin.getDataFolder(), "maps/map_" + _id + "/map.yml");
		}
		save(_file);
	}
	
	public List<Location> getLocationList(List<String> list) {
		ArrayList<Location> locations = new ArrayList<Location>();
		for(String s : list) {
			Location loc = getPointRelative(s);
			if(loc != null) {
				locations.add(loc);
			}
		}
		return locations;
	}
	
	private String getLocationString(Location loc) {
		return loc.getBlockX() + " "
				+ loc.getBlockY() + " "
				+ loc.getBlockZ() + " "
				+ TacoWar.getNearestDegree(loc.getYaw(), 45) + " "
				+ TacoWar.getNearestDegree(loc.getPitch(), 45);
	}
	
	public boolean addPerm(String perm) {
		if(_perms.contains(perm))
			return false;
		_perms.add(perm);
		return true;
	}
	
	public List<String> getPerms() {
		return _perms;
	}
	
	public boolean hasPerm(String perm) {
		return _perms.contains(perm);
	}
	
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
		for(Teleporter t : _teleporters) {
			if(t.getChannel().equals(channel)) {
				teleporters.add(t);
			}
		}
		return teleporters;
	}
	
	public List<Teleporter> getReceiverTeleporters(String channel) {
		ArrayList<Teleporter> teleporters = new ArrayList<Teleporter>();
		for(Teleporter t : getTeleporters(channel)) {
			if(t.isReceiver()) {
				teleporters.add(t);
			}
		}
		return teleporters;
	}
	
	//get recevier teleporters but exclude the teleporter with the given name
	public List<Teleporter> getReceiverTeleporters(String channel, String name) {
		List<Teleporter> teleporters = getReceiverTeleporters(channel);
		for(Teleporter t : teleporters) {
			if(t.getName().equals(name)) {
				teleporters.remove(t);
				break;
			}
		}
		return teleporters;
	}
	
	public List<Teleporter> getTransmitterTeleporters(String channel) {
		ArrayList<Teleporter> teleporters = new ArrayList<Teleporter>();
		for(Teleporter t : getTeleporters(channel)) {
			if(t.isTransmitter()) {
				teleporters.add(t);
			}
		}
		return teleporters;
	}
	
	public Teleporter getTeleporter(Location src) {
		for(Teleporter t : _teleporters) {
			Location tsrc = t.getSource();
			if(tsrc.getBlockX() == src.getBlockX() &&
					tsrc.getBlockY() == src.getBlockY() &&
					tsrc.getBlockZ() == src.getBlockZ()) {
				return t;
			}
		}
		return null;
	}
	
	public boolean gameTypeSupported(GameType gameType) {
		return _supported.contains(gameType);
	}
	
	public FFAMapOptions ffa() {
		return _ffaOptions;
	}
	
	public HideAndSeekMapOptions has() {
		return _hasOptions;
	}
	
	public InfectionMapOptions inf() {
		return _infOptions;
	}
	
	public JuggernautMapOptions jug() {
		return _jugOptions;
	}
	
	public KOTHMapOptions koth() {
		return _kothOptions;
	}
	
	public TDMMapOptions tdm() {
		return _tdmOptions;
	}
	
}
